package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class EmployeeAttendanceListModel(

    @field:SerializedName("data")
    val data: List<EmployeeWiseDataItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class EmployeeWiseDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Attendance")
    val attendance: String? = null,

    @field:SerializedName("AttendanceID")
    val attendanceID: String? = null,

    @field:SerializedName("OverTime")
    val overTime: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("SitesID")
    val sitesID: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("AttendanceDate")
    val attendanceDate: String? = null,

    @field:SerializedName("QuotationID")
    val quotationID: String? = null,

    @field:SerializedName("EmployeeName")
    val employeeName: String? = null,

    @field:SerializedName("Name")
    val name: String? = null
)
