package com.ds.basetemplate.remote.responses

import com.google.gson.annotations.SerializedName

data class UserListResponse(

    @field:SerializedName("per_page")
    val perPage: Int? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("data")
    val userList: ArrayList<User>? = null,

    @field:SerializedName("page")
    val page: Int? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,

    @field:SerializedName("support")
    val support: Support? = null
)

data class Support(

    @field:SerializedName("text")
    val text: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)

data class User(

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)
