package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class UserTypeListModel(

    @field:SerializedName("data")
    val data: List<UserTypeDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class UserTypeDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Usertype")
    val usertype: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("Qty")
    val qty: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("Rate")
    val rate: String? = null,

    @field:SerializedName("HSNNo")
    val hSNNo: String? = null,

    @field:SerializedName("UsertypeID")
    val usertypeID: String? = null
)
