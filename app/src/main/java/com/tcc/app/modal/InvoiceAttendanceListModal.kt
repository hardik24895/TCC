package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
data class InVoiceAttendanceListModal(

	@field:SerializedName("data")
	val data: InvoiceAttendanceDataItem? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserItemData(

	@field:SerializedName("Usertype")
	val usertype: String? = null,

	@field:SerializedName("Rate")
	val rate: String? = null,

	@field:SerializedName("Qty")
	val qty: String? = null,

	@field:SerializedName("HSNNo")
	val hSNNo: String? = null,

	@field:SerializedName("IsMaterial")
	val isMaterial: String? = null,

	@field:SerializedName("UsertypeID")
	val usertypeID: String? = null
)

data class MaterialItemData(

	@field:SerializedName("Usertype")
	val usertype: String? = null,

	@field:SerializedName("Rate")
	val rate: String? = null,

	@field:SerializedName("Qty")
	val qty: String? = null,

	@field:SerializedName("HSNNo")
	val hSNNo: String? = null,

	@field:SerializedName("IsMaterial")
	val isMaterial: String? = null,

	@field:SerializedName("UsertypeID")
	val usertypeID: String? = null
)

data class InvoiceAttendanceDataItem(

	@field:SerializedName("material")
	val material: List<MaterialItemData> = mutableListOf(),

	@field:SerializedName("user")
	val user: List<UserItemData> = mutableListOf()
)
