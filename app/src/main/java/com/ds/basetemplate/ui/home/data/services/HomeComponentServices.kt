package com.ds.basetemplate.ui.home.data.services

import com.ds.basetemplate.base.AppResponse
import com.ds.basetemplate.base.BaseRepository
import com.ds.basetemplate.remote.ApiInterface
import com.ds.basetemplate.remote.responses.UserListResponse
import javax.inject.Inject

class HomeComponentServices @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun getUsersList(): AppResponse<UserListResponse> {
        return invokeApiCall {
            apiInterface.getUsersList()
        }
    }
}