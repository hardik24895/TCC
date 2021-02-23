package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class PenaltyListModel(

	@field:SerializedName("data")
	val data: List<PaneltyDataItem> = mutableListOf(),

	@field:SerializedName("rowcount")
	val rowcount: Int? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PaneltyDataItem(

	@field:SerializedName("PenaltyDate")
	val penaltyDate: String? = null,

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("PenaltyID")
	val penaltyID: String? = null,

	@field:SerializedName("Item")
	val item: List<PaleltyUserListItem> = mutableListOf(),

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("SitesID")
	val sitesID: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("SitesName")
	val sitesName: String? = null,

	@field:SerializedName("Reason")
	val reason: String? = null
)

data class PaleltyUserListItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("PenaltyEmployeeID")
	val penaltyEmployeeID: String? = null,

	@field:SerializedName("UserID")
	val userID: String? = null,

	@field:SerializedName("EmployeeName")
	val employeeName: String? = null,

	@field:SerializedName("Penalty")
	val penalty: String? = null
)
