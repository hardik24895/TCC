package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class ProcessListModal(

    @field:SerializedName("data")
    val data: List<ProcessListDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: Int = 0
)

data class ProcessListDataItem(

    @field:SerializedName("ActionID")
    val actionID: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("ActionType")
    val actionType: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("CreatedDate")
    val createdDate: String? = null,

    @field:SerializedName("Discription")
    val discription: String? = null,

    @field:SerializedName("CustomerType")
    val customerType: String? = null,

    @field:SerializedName("VisitorName")
    val visitorName: String? = null,

    @field:SerializedName("CustomerProcessID")
    val customerProcessID: String? = null
)
