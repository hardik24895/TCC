package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class GetUserSalaryDetail(

	@field:SerializedName("data")
	val data: List<GetSalaryDataItem> = mutableListOf(),

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class GetSalaryDataItem(

    @field:SerializedName("StartDate")
    val startDate: String? = null,

    @field:SerializedName("MobileNo")
    val mobileNo: String? = null,

    @field:SerializedName("AbsentCount")
    val absentCount: String? = null,

    @field:SerializedName("Salary")
    val salary: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("HalfOverTime")
    val halfOverTime: String? = null,

    @field:SerializedName("FullOverTime")
    val fullOverTime: String? = null,

    @field:SerializedName("PresentCount")
    val presentCount: String? = null,

    @field:SerializedName("EndDate")
    val endDate: String? = null,

    @field:SerializedName("EmployeeName")
    val employeeName: String? = null,

    @field:SerializedName("HalfDayCount")
    val halfDayCount: String? = null,

    @field:SerializedName("Penalty")
    val penalty: String? = null,

    @field:SerializedName("AdvanceType")
    val advanceType: String? = null,

    @field:SerializedName("Advance")
    val advance: String? = null
)
