package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class CompanyListModal(

	@field:SerializedName("data")
	val data: List<CompanyDataItem> = mutableListOf(),

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CompanyDataItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("CompanyName")
	val companyName: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("CompanyID")
	val companyID: String? = null,

	@field:SerializedName("Address")
	val address: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("ISGST")
	val iSGST: String? = null,

	@field:SerializedName("AccountNo")
	val accountNo: String? = null,

	@field:SerializedName("GSTNo")
	val gSTNo: String? = null,

	@field:SerializedName("IFSCCode")
	val iFSCCode: String? = null
)
