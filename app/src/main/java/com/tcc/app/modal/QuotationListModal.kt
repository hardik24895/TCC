package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class QuotationListModal(

    @field:SerializedName("data")
    val data: List<QuotationItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int = 0,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class QuotationItem(

    @field:SerializedName("CompanyID")
    val companyID: String? = null,

    @field:SerializedName("Address")
    val address: String? = null,

    @field:SerializedName("SiteName")
    val siteName: String? = null,

    @field:SerializedName("StateName")
    val stateName: String? = null,

    @field:SerializedName("QuotationID")
    val quotationID: String? = null,

    @field:SerializedName("CustomerID")
    val customerID: String? = null,

    @field:SerializedName("Document")
    val document: String? = null,

    @field:SerializedName("SubTotal")
    val subTotal: String? = null,

    @field:SerializedName("PinCode")
    val pinCode: String? = null,

    @field:SerializedName("Name")
    val name: String? = null,

    @field:SerializedName("CompanyName")
    val companyName: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("SGST")
    val sGST: String? = null,

    @field:SerializedName("CGST")
    val cGST: String? = null,

    @field:SerializedName("IGST")
    val iGST: String? = null,

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("VisitorID")
    val visitorID: String? = null,

    @field:SerializedName("SitesID")
    val sitesID: String? = null,

    @field:SerializedName("EstimateNo")
    val estimateNo: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("QuotationStatus")
    val quotationStatus: String? = null,

    @field:SerializedName("Address2")
    val address2: String? = null,

    @field:SerializedName("StateID")
    val stateID: String? = null,

    @field:SerializedName("Service")
    val service: String? = null,

    @field:SerializedName("CityName")
    val cityName: String? = null,

    @field:SerializedName("Rounding")
    val rounding: String? = null,

    @field:SerializedName("ServiceID")
    val serviceID: String? = null,

    @field:SerializedName("CityID")
    val cityID: String? = null,

    @field:SerializedName("Total")
    val total: String? = null
)
