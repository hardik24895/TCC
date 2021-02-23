package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class GetUserByType(

    @field:SerializedName("data")
    val data: List<UserByTypeDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class UserByTypeDataItem(

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("FirstName")
    val firstName: String? = null,

    @field:SerializedName("LastName")
    val lastName: String? = null,

    @field:SerializedName("UserType")
    val userType: String? = null
)
