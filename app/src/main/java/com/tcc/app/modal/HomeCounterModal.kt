package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class HomeCounterModal(

	@field:SerializedName("data")
	val data: List<HomeCounterDataItem> = mutableListOf(),

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class HomeCounterDataItem(

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("Count")
	val count: String? = null
)
