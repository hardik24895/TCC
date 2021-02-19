package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class CustomerAttendanceListModal(

	@field:SerializedName("data")
	val data: List<CustomerAttendanceDataItem> = mutableListOf(),

	@field:SerializedName("rowcount")
	val rowcount: Int? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CustomerAttendanceDataItem(

	@field:SerializedName("HalfDayount")
	val halfDayount: String? = null,

	@field:SerializedName("OverTime")
	val overTime: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("SitesID")
	val sitesID: String? = null,

	@field:SerializedName("SiteUserFrindlyName")
	val siteUserFrindlyName: String? = null,

	@field:SerializedName("EstimateNo")
	val estimateNo: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("Absentount")
	val absentount: String? = null,

	@field:SerializedName("AttendanceDate")
	val attendanceDate: String? = null,

	@field:SerializedName("PresentCount")
	val presentCount: String? = null,

	@field:SerializedName("QuotationID")
	val quotationID: String? = null
)
