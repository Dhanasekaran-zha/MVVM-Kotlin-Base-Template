package com.ds.basetemplate.ui.home.domain.usecase

import com.ds.basetemplate.base.AppResponse
import com.ds.basetemplate.remote.responses.UserListResponse
import com.ds.basetemplate.ui.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeUsecase @Inject constructor(private val repository: HomeRepository) {

    suspend fun getUserList(): AppResponse<UserListResponse> {
        return repository.getUsersList()
    }
}