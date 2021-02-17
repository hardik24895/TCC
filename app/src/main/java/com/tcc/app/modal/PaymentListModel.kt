package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class PaymentListModel(

    @field:SerializedName("data")
    val data: List<PaymentListDataItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class PaymentListDataItem(

    @field:SerializedName("BankName")
    val bankName: String? = null,

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("SiteUserFrindlyName")
    val siteUserFrindlyName: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("PaymentDate")
    val paymentDate: String? = null,

    @field:SerializedName("AccountNo")
    val accountNo: String? = null,

    @field:SerializedName("GSTAmount")
    val gSTAmount: String? = null,

    @field:SerializedName("InvoiceNo")
    val invoiceNo: String? = null,

    @field:SerializedName("InvoiceID")
    val invoiceID: String? = null,

    @field:SerializedName("PaymentAmount")
    val paymentAmount: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("AmountType")
    val amountType: String? = null,

    @field:SerializedName("CustomerPaymentID")
    val customerPaymentID: String? = null,

    @field:SerializedName("IFCCode")
    val iFCCode: String? = null,

    @field:SerializedName("BranchName")
    val branchName: String? = null,

    @field:SerializedName("ChequeNo")
    val chequeNo: String? = null,

    @field:SerializedName("PaymentMode")
    val paymentMode: String? = null
)
