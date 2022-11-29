package com.ds.basetemplate.ui.home.data.repository_impl

import com.ds.basetemplate.base.AppResponse
import com.ds.basetemplate.remote.responses.UserListResponse
import com.ds.basetemplate.ui.home.data.services.HomeComponentServices
import com.ds.basetemplate.ui.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val services: HomeComponentServices) :
    HomeRepository {
    override suspend fun getUsersList(): AppResponse<UserListResponse> {
        return services.getUsersList()
    }
}