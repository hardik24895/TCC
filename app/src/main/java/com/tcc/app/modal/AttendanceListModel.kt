package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AttendanceListModel(

    @field:SerializedName("data")
    val data: List<AttendanceListDataItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class AttendanceListDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("MobileNo")
    val mobileNo: String? = null,

    @field:SerializedName("Attendance")
    val attendance: String? = null,

    @field:SerializedName("TeamdefinationID")
    val teamdefinationID: String? = null,

    @field:SerializedName("SitesID")
    val sitesID: String? = null,

    @field:SerializedName("EndTime")
    val endTime: String? = null,

    @field:SerializedName("EstimateNo")
    val estimateNo: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("StartTime")
    val startTime: String? = null,

    @field:SerializedName("QuotationID")
    val quotationID: String? = null,

    @field:SerializedName("EndDate")
    val endDate: String? = null,

    @field:SerializedName("StartDate")
    val startDate: String? = null,

    @field:SerializedName("Type")
    val type: String? = null,

    @field:SerializedName("AttendanceID")
    val attendanceID: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("Overtime")
    val overtime: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("EmployeeName")
    val employeeName: String? = null,


    ) : Serializable
