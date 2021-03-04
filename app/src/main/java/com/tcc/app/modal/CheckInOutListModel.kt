package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class CheckInOutListModel(

    @field:SerializedName("data")
    val data: List<CheckInOutDataItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("Checkintime")
    val checkintime: String? = null,

    @field:SerializedName("Checkouttime")
    val checkouttime: String? = null
)

data class CheckInOutDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("CheckincheckoutID")
    val checkincheckoutID: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("Checkintime")
    val checkintime: String? = null,

    @field:SerializedName("Outlatitude")
    val outlatitude: String? = null,

    @field:SerializedName("Outlongitude")
    val outlongitude: String? = null,

    @field:SerializedName("OutAddress")
    val outAddress: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("Inlatitude")
    val inlatitude: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("InAddress")
    val inAddress: String? = null,

    @field:SerializedName("Checkouttime")
    val checkouttime: String? = null,

    @field:SerializedName("EmployeeName")
    val employeeName: String? = null,

    @field:SerializedName("Inlongitude")
    val inlongitude: String? = null
)
