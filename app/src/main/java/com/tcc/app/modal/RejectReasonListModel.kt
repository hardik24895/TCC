package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class RejectReasonListModel(

    @field:SerializedName("data")
    val data: List<RejectReasonDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class RejectReasonDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("ReasonID")
    val reasonID: String? = null,

    @field:SerializedName("Reason")
    val reason: String? = null
)
