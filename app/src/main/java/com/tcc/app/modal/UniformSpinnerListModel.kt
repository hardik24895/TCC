package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class UniformSpinnerListModel(

	@field:SerializedName("data")
	val data: List<UniformSpinnerDataItem> = mutableListOf(),

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UniformSpinnerDataItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("Uniformtype")
	val uniformtype: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("UniformTypeID")
	val uniformTypeID: String? = null
)
