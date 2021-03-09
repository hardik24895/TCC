package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class DynemicSiteTabListModal(

	@field:SerializedName("data")
	val data: List<DynemicSiteTabDataItem> = mutableListOf(),

	@field:SerializedName("rowcount")
	val rowcount: Int = 0,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DynemicSiteTabDataItem(

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("Sites")
	val sites: MutableList<SiteListItem> = mutableListOf()
)


