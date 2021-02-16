package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class ServiceListModel(

    @field:SerializedName("data")
    val data: List<ServiceDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ServiceDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("Rate")
    val rate: String? = null,

    @field:SerializedName("Qty")
    val qty: String? = null,

    @field:SerializedName("Service")
    val service: String? = null,

    @field:SerializedName("ServiceID")
    val serviceID: String? = null,

    @field:SerializedName("IsFixCost")
    val isFixCost: String? = null
)
