package com.ds.basetemplate.ui.home.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ds.basetemplate.base.AppResponse
import com.ds.basetemplate.remote.responses.UserListResponse
import com.ds.basetemplate.ui.home.domain.usecase.HomeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val state: SavedStateHandle, private val usecase: HomeUsecase) : ViewModel() {

    var userListLiveData: MutableLiveData<AppResponse<UserListResponse>>? = null

    fun getUserList(): MutableLiveData<AppResponse<UserListResponse>>? {
        userListLiveData = MutableLiveData<AppResponse<UserListResponse>>()
        GlobalScope.launch {
            userListLiveData?.postValue(usecase.getUserList())
        }
        return userListLiveData
    }
}