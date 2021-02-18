package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeadListModal(

        @field:SerializedName("data")
        val data: List<LeadItem> = mutableListOf(),

        @field:SerializedName("error")
        val error: Int? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("rowcount")
        val rowcount: Int = 0
)

data class LeadItem(

    @field:SerializedName("MobileNo")
        val mobileNo: String? = null,

    @field:SerializedName("Status")
        val status: String? = null,

    @field:SerializedName("VisitorID")
        val visitorID: String? = null,

    @field:SerializedName("EmailID")
        val emailID: String? = null,

    @field:SerializedName("Address")
        val address: String? = null,

    @field:SerializedName("rowcount")
        val rowcount: String? = null,

    @field:SerializedName("StateID")
        val stateID: String? = null,

    @field:SerializedName("LeadType")
        val leadType: String? = null,

    @field:SerializedName("CityName")
        val cityName: String? = null,

    @field:SerializedName("CustomerID")
        val customerID: String? = null,

    @field:SerializedName("Name")
        val name: String? = null,

    @field:SerializedName("PinCode")
    val pinCode: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("CityID")
    val cityID: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null
) : Serializable
