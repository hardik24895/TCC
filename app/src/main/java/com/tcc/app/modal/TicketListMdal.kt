package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class TicketListMdal(

    @field:SerializedName("data")
    val data: List<TicketDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class TicketDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("TicketID")
    val ticketID: String? = null,

    @field:SerializedName("Description")
    val description: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("FirstName")
    val firstName: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("Priority")
    val priority: String? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("LastName")
    val lastName: String? = null,

    @field:SerializedName("Image")
    val image: String? = null,

    @field:SerializedName("EmployeeName")
    val employeeName: String? = null
)
