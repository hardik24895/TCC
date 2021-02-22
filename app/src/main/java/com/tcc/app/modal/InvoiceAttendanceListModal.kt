package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class InvoiceAttendanceListModal(

	@field:SerializedName("data")
	val data: List<InvoiceAttendanceDataItem> = mutableListOf(),

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class InvoiceAttendanceDataItem(

	@field:SerializedName("Usertype")
	val usertype: String? = null,

	@field:SerializedName("Rate")
	val rate: String? = null,

	@field:SerializedName("Qty")
	val qty: String? = null,

	@field:SerializedName("HSNNo")
	val hSNNo: String? = null,

	@field:SerializedName("UsertypeID")
	val usertypeID: String? = null
)
