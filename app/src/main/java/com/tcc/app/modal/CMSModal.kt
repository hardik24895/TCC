package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class CMSModal(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)
