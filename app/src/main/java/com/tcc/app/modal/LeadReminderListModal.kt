package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class LeadReminderListModal(

	@field:SerializedName("data")
	val data: List<LeadReminderDataItem> = mutableListOf(),

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: Int = 0
)

data class LeadReminderDataItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("VisitorID")
	val visitorID: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("ReminderBy")
	val reminderBy: String? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("ReminderDate")
	val reminderDate: String? = null,

	@field:SerializedName("PastDate")
	val pastDate: String? = null,

	@field:SerializedName("EmployeeName")
	val employeeName: String? = null,

	@field:SerializedName("VisitorReminderID")
	val visitorReminderID: String? = null
)
