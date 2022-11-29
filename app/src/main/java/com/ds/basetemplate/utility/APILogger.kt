package com.ds.basetemplate.utility

import okhttp3.*
import okhttp3.internal.http.promisesBody
import okhttp3.logging.HttpLoggingInterceptor.*
import okhttp3.logging.HttpLoggingInterceptor.Logger.Companion.DEFAULT
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class APILogger : Interceptor {
    @Volatile
    private var level: Level = Level.NONE
    private val logger: Logger

    @JvmOverloads
    constructor(logger: Logger = DEFAULT) {
        this.logger = logger
    }

    /** Change the level at which this interceptor logs.  */
    fun setLevel(level: Level?): APILogger {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
        return this
    }

    fun getLevel(): Level {
        return level
    }

    constructor(level: Level, logger: Logger) {
        this.level = level
        this.logger = logger
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level: Level = level
        val request: Request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }
        val logBody = level == Level.BODY
        val logHeaders = logBody || level ==Level.HEADERS
        val requestBody = request.body
        val hasRequestBody = requestBody != null
        val connection: Connection? = chain.connection()
        val protocol = connection?.protocol() ?: Protocol.HTTP_1_1
        logger.log("*#*#*#*#*#*#*#*#* {{REQUEST START}} *#*#*#*#*#*#*#*#*")
        var requestStartMessage: String =
            "## " + request.method + ' ' + request.url + ' ' + protocol
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        logger.log(requestStartMessage)
        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    logger.log("*# Content-Type: " + requestBody.contentType())
                }
                if (requestBody.contentLength() != -1L) {
                    logger.log("*# Content-Length: " + requestBody.contentLength())
                }
            }
            val headers = request.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(
                        name,
                        ignoreCase = true
                    ) && !"Content-Length".equals(name, ignoreCase = true)
                ) {
                    logger.log("*# Header: " + name + ": " + headers.value(i))
                }
                i++
            }
            if (!logBody || !hasRequestBody) {
                logger.log("*# END " + request.method)
            } else if (bodyEncoded(request.headers)) {
                logger.log("*# END " + request.method + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)
                var charset: Charset? = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }
                logger.log("")
                if (isPlaintext(buffer)) {
                    logger.log("*# "+ charset?.let { buffer.readString(it) });
                    logger.log(
                        "*# END " + request.method
                                + " (" + requestBody.contentLength() + "-byte body)"
                    )
                } else {
                    logger.log(
                        ("*# END " + request.method + " (binary "
                                + requestBody.contentLength() + "-byte body omitted)")
                    )
                }
            }
            logger.log("*#*#*#*#*#*#*#*#* {{REQUEST END}} *#*#*#*#*#*#*#*#*")
        }
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("*# HTTP FAILED: $e")
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        logger.log("*#*#*#*#*#*#*#*#* [RESPONSE START] *#*#*#*#*#*#*#*#*")
        logger.log("*# Response Code: " + response.code)
        logger.log(
            ("*# " + response.message + ' '
                    + response.request.url + " (" + tookMs + "ms" + (if (!logHeaders) (", "
                    + bodySize + " body") else "") + ')')
        )
        if (logHeaders) {
            val headers = response.headers
            var i = 0
            val count = headers.size
            while (i < count) {
                logger.log("*# " + headers.name(i) + ": " + headers.value(i))
                i++
            }
            if (!logBody || !response.promisesBody()) {
                logger.log("*# END HTTP")
            } else if (bodyEncoded(response.headers)) {
                logger.log("*# END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()
                var charset: Charset? = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }
                if (!isPlaintext(buffer)) {
                    logger.log("")
                    logger.log("*# END HTTP (binary " + buffer.size + "-byte body omitted)")
                    return response
                }
                if (contentLength != 0L) {
                    logger.log("")
                    //                    L.log(LOG_TAG, "Response : "+buffer.clone().readString(charset));
                    logger.log("*# " + buffer.clone().readString((charset)!!))
                }
                logger.log("*# END HTTP (" + buffer.size + "-byte body)")
            }
            logger.log("*#*#*#*#*#*#*#*#* [RESPONSE END] *#*#*#*#*#*#*#*#*")
        }
        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    companion object {
        private val LOG_TAG = APILogger::class.java.simpleName
        private val UTF8 = Charset.forName("UTF-8")

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        fun isPlaintext(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = if (buffer.size < 64) buffer.size else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (e: EOFException) {
                return false // Truncated UTF-8 sequence.
            }
        }
    }
}