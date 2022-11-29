package com.ds.basetemplate.ui.home.domain.repository

import com.ds.basetemplate.base.AppResponse
import com.ds.basetemplate.remote.responses.UserListResponse

interface HomeRepository {
    suspend fun getUsersList():AppResponse<UserListResponse>
}