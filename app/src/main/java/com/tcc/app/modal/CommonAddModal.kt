package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class CommonAddModal(

	@field:SerializedName("data")
	val data: List<CoomonData> = mutableListOf(),

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CoomonData(

	@field:SerializedName("ID")
	val iD: String? = null
)
