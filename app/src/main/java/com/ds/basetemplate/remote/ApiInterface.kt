package com.ds.basetemplate.remote

import com.ds.basetemplate.remote.responses.UserListResponse
import retrofit2.http.GET

interface ApiInterface {

    @GET(ApiUrl.USER_LIST)
    suspend fun getUsersList() : UserListResponse
}