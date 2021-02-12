package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SiteListModal(

    @field:SerializedName("data")
    val data: List<SiteListItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class SiteListItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("VisitorID")
    val visitorID: String? = null,

    @field:SerializedName("SitesID")
    val sitesID: String? = null,

    @field:SerializedName("SiteName")
    val siteName: String? = null,

    @field:SerializedName("Address")
    val address: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("GSTNo")
    val gSTNo: String? = null,

    @field:SerializedName("StateName")
    val stateName: String? = null,

    @field:SerializedName("Address2")
    val address2: String? = null,

    @field:SerializedName("StateID")
    val stateID: String? = null,

    @field:SerializedName("CustomerID")
    val customerID: String? = null,

    @field:SerializedName("CityName")
    val cityName: String? = null,

    @field:SerializedName("EndDate")
    val endDate: String? = null,

    @field:SerializedName("Name")
    val name: String? = null,

    @field:SerializedName("PinCode")
    val pinCode: String? = null,

    @field:SerializedName("WorkingHours")
    val workingHours: String? = null,

    @field:SerializedName("StartDate")
    val startDate: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("CityID")
    val cityID: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("SiteType")
    val siteType: String? = null,

    @field:SerializedName("ProposedDate")
    val proposedDate: String? = null,

    @field:SerializedName("WorkingDays")
    val workingDays: String? = null
) : Serializable
