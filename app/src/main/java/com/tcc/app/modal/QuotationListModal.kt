package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class QuotationListModal(

    @field:SerializedName("data")
    val data: List<QuotationItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class MaterialItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Usertype")
    val usertype: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("QuotationItemID")
    val quotationItemID: String? = null,

    @field:SerializedName("Rate")
    val rate: String? = null,

    @field:SerializedName("Qty")
    val qty: String? = null,

    @field:SerializedName("Days")
    val days: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("HSNNo")
    val hSNNo: String? = null,

    @field:SerializedName("QuotationID")
    val quotationID: String? = null,

    @field:SerializedName("UsertypeID")
    val usertypeID: String? = null
) : Serializable

data class Item(

    @field:SerializedName("User")
    val user: List<UserItem?>? = null,

    @field:SerializedName("Material")
    val material: List<MaterialItem?>? = null
) : Serializable

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

    @field:SerializedName("Item")
    val item: Item? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("SGST")
    val sGST: String? = null,

    @field:SerializedName("CGST")
    val cGST: String? = null,

    @field:SerializedName("IGST")
    val iGST: String? = null,

    @field:SerializedName("IsFixCost")
    val isFixCost: String? = null,

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
    val total: String? = null,

    @field:SerializedName("TeamSize")
    val teamSize: String? = null,

    @field: SerializedName("StartDate")
    val startDate: String? = null,

    @field: SerializedName("Note")
    val note: String? = null,

    @field: SerializedName("Term")
    val term: String? = null,

    @field:SerializedName("StartTime")
    val startTime: String? = null,

    @field:SerializedName("EndTime")
    val endTime: String? = null,

    @field:SerializedName("EndDate")
    val endDate: String? = null


) : Serializable

data class UserItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Usertype")
    val usertype: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("QuotationItemID")
    val quotationItemID: String? = null,

    @field:SerializedName("Rate")
    val rate: String? = null,

    @field:SerializedName("Qty")
    val qty: String? = null,

    @field:SerializedName("Days")
    val days: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("HSNNo")
    val hSNNo: String? = null,

    @field:SerializedName("QuotationID")
    val quotationID: String? = null,

    @field:SerializedName("UsertypeID")
    val usertypeID: String? = null
) : Serializable
