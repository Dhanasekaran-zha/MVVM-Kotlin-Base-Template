package com.ds.basetemplate.di

import androidx.viewbinding.BuildConfig
import com.ds.basetemplate.remote.ApiInterface
import com.ds.basetemplate.remote.ApiUrl
import com.ds.basetemplate.utility.APILogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    private const val CONNECTION_TIMEOUT = 30000

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
        if (BuildConfig.DEBUG) {
            val logger = APILogger()
            logger.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClient.addInterceptor(logger)
        }

        return okHttpClient.build()

    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitAPI(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

}