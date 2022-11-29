package com.ds.basetemplate.ui.home.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ds.basetemplate.base.AppResponse
import com.ds.basetemplate.base.BaseActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ds.basetemplate.databinding.ActivityHomeBinding
import com.ds.basetemplate.remote.responses.UserListResponse
import com.ds.basetemplate.ui.home.presentation.adapter.UserListAdapter
import com.ds.basetemplate.ui.home.presentation.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private var binding: ActivityHomeBinding? = null
    private var userListAdapter: UserListAdapter? = null
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        showLoading()
        binding?.userListRecyclerView?.layoutManager = LinearLayoutManager(this)
        userListAdapter = UserListAdapter(this)
        binding?.userListRecyclerView?.adapter = userListAdapter

        viewModel.getUserList()?.observe(this, object : Observer<AppResponse<UserListResponse>> {
            override fun onChanged(response: AppResponse<UserListResponse>?) {
                when (response) {
                    is AppResponse.Success -> {
                        Toast.makeText(
                            this@HomeActivity,
                            response.data.total.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        userListAdapter?.setUserList(response.data.userList)
                        hideLoading()
                    }
                    else -> {
                        Toast.makeText(this@HomeActivity, "Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
    }
}