package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class AvailableEmployeeListModel(

    @field:SerializedName("data")
    val data: List<AvailableEmployeeDataItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class AvailableEmployeeDataItem(

    @field:SerializedName("MobileNo")
    val mobileNo: String? = null,

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("EmailID")
    val emailID: String? = null,

    @field:SerializedName("IsDeleted")
    val isDeleted: String? = null,

    @field:SerializedName("Address")
    val address: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("FirstName")
    val firstName: String? = null,

    @field:SerializedName("RoleID")
    val roleID: String? = null,

    @field:SerializedName("Availibility")
    val availibility: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("LastName")
    val lastName: String? = null,

    @field:SerializedName("UserType")
    val userType: String? = null
)
