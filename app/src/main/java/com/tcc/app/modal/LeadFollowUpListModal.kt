package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class LeadFollowUpListModal(

	@field:SerializedName("data")
	val data: List<LeadFollowUpDataItem> = mutableListOf(),

	@field:SerializedName("rowcount")
	val rowcount: Int? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LeadFollowUpDataItem(

	@field:SerializedName("MobileNo")
	val mobileNo: String? = null,

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("VisitorID")
	val visitorID: String? = null,

	@field:SerializedName("EmployeeFirstName")
	val employeeFirstName: String? = null,

	@field:SerializedName("EmailID")
	val emailID: String? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: String? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("ReminderDate")
	val reminderDate: String? = null,

	@field:SerializedName("ModifiedBy")
	val modifiedBy: String? = null,

	@field:SerializedName("ModifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("Name")
	val name: String? = null,

	@field:SerializedName("EmployeeLastName")
	val employeeLastName: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("ReminderBy")
	val reminderBy: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("PastDate")
	val pastDate: String? = null,

	@field:SerializedName("ReminderDateOrder")
	val reminderDateOrder: String? = null,

	@field:SerializedName("VisitorReminderID")
	val visitorReminderID: String? = null
)
