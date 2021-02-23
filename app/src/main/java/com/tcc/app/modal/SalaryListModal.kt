package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SalaryListModal(

	@field:SerializedName("data")
	val data: List<SalaryDataItem> = mutableListOf(),

	@field:SerializedName("rowcount")
	val rowcount: Int? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SalaryDataItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("MobileNo")
	val mobileNo: String? = null,

	@field:SerializedName("HalfDay")
	val halfDay: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("Rate")
	val rate: String? = null,

	@field:SerializedName("Salary")
	val salary: String? = null,

	@field:SerializedName("FirstName")
	val firstName: String? = null,

	@field:SerializedName("FullOverTime")
	val fullOverTime: String? = null,

	@field:SerializedName("EndDate")
	val endDate: String? = null,

	@field:SerializedName("SalaryID")
	val salaryID: String? = null,

	@field:SerializedName("StartDate")
	val startDate: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("SalaryDate")
	val salaryDate: String? = null,

	@field:SerializedName("UserID")
	val userID: String? = null,

	@field:SerializedName("HalfOverTime")
	val halfOverTime: String? = null,

	@field:SerializedName("Present")
	val present: String? = null,

	@field:SerializedName("LastName")
	val lastName: String? = null,

	@field:SerializedName("Absent")
	val absent: String? = null
): Serializable
