package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class AdvanceListModal(

    @field:SerializedName("data")
    val data: List<AdvanceListDataItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class AdvanceListDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("MobileNo")
    val mobileNo: String? = null,

    @field:SerializedName("Salary")
    val salary: String? = null,

    @field:SerializedName("Type")
    val type: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("FirstName")
    val firstName: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("AdvanceID")
    val advanceID: String? = null,

    @field:SerializedName("AdvanceDate")
    val advanceDate: String? = null,

    @field:SerializedName("LastName")
    val lastName: String? = null
)
