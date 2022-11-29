package com.ds.basetemplate.base

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

abstract class BaseRepository {

    suspend fun <T> invokeApiCall(
        apiCall: suspend () -> T
    ): AppResponse<T> {

        return withContext(Dispatchers.IO) {
            try {
                AppResponse.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Log.d("BaseRepository->Exception", throwable.toString())
                when (throwable) {

                    is SocketTimeoutException -> {
                        AppResponse.Error(
                            1500,
                           "Timeout"
                        )
                    }

                    else -> {
                        AppResponse.Error(
                            1000,
                           "Something went wrong"
                        )
                    }

                }
            }
        }
    }

}