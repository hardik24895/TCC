package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DocumentListModal(

	@field:SerializedName("data")
	val data: List<DocumentListDataItem> = mutableListOf(),

	@field:SerializedName("rowcount")
	val rowcount: Int? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DocumentListDataItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("CustomerSitesDocumentID")
	val customerSitesDocumentID: String? = null,

	@field:SerializedName("SitesID")
	val sitesID: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("Title")
	val title: String? = null,

	@field:SerializedName("CustomerID")
	val customerID: String? = null,

	@field:SerializedName("Document")
	val document: String? = null
):Serializable
