package com.ds.basetemplate.ui.home.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ds.basetemplate.databinding.AdapterUserItemBinding
import com.ds.basetemplate.remote.responses.User

class UserListAdapter(val context: Context) :
    RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    private var userList: ArrayList<User>? = null
    private var adapterBinding: AdapterUserItemBinding? = null

    init {
        userList = arrayListOf()
    }

    fun setUserList(userList: ArrayList<User>?) {
        if (userList?.isNotEmpty()!!) {
            this.userList?.addAll(userList)
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        adapterBinding = AdapterUserItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return UserListViewHolder(adapterBinding)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bindDataToView(userList!![position])
    }

    override fun getItemCount(): Int {
        return userList!!.size
    }

    inner class UserListViewHolder(binding: AdapterUserItemBinding?) :
        RecyclerView.ViewHolder(binding?.root!!) {
        fun bindDataToView(user: User) {
            Glide.with(context).load(user.avatar).into(adapterBinding?.userImage!!)
            adapterBinding?.userName?.text = user.firstName + " " + user.lastName
        }
    }
}