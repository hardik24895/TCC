package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InvoiceListModal(

	@field:SerializedName("data")
	val data: List<InvoiceDataItem> = mutableListOf(),

	@field:SerializedName("rowcount")
	val rowcount: Int = 0,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class InvoiceDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("SitesID")
    val sitesID: String? = null,

    @field:SerializedName("SiteUserFrindlyName")
    val siteUserFrindlyName: String? = null,

    @field:SerializedName("EstimateNo")
    val estimateNo: String? = null,

    @field:SerializedName("RemainingPayment")
    val remainingPayment: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("Terms")
    val terms: String? = null,

    @field:SerializedName("InvoiceNo")
    val invoiceNo: String? = null,

    @field:SerializedName("QuotationID")
    val quotationID: String? = null,

    @field:SerializedName("TotalAmount")
    val totalAmount: String? = null,

    @field:SerializedName("InvoiceID")
    val invoiceID: String? = null,

    @field:SerializedName("Document")
    val document: String? = null,

    @field:SerializedName("EndDate")
    val endDate: String? = null,

    @field:SerializedName("SubTotal")
    val subTotal: String? = null,

    @field:SerializedName("StartDate")
    val startDate: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("SGST")
    val sGST: String? = null,

    @field:SerializedName("RemainingGSTPayment")
    val remainingGSTPayment: String? = null,

    @field:SerializedName("CGST")
    val cGST: String? = null,

    @field:SerializedName("InvoiceDate")
    val invoiceDate: String? = null,

    @field:SerializedName("IGST")
    val iGST: String? = null,

    @field:SerializedName("MobileNo")
    val mobileNo: String? = null,

    @field:SerializedName("Notes")
    val notes: String? = null
) : Serializable
