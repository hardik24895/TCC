package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class RoomAllocationListModel(

        @field:SerializedName("data")
        val data: List<RoomDataItem> = mutableListOf(),

        @field:SerializedName("rowcount")
        val rowcount: Int? = null,

        @field:SerializedName("error")
        val error: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)

data class RoomDataItem(

        @field:SerializedName("StartDate")
        val startDate: String? = null,

        @field:SerializedName("Status")
        val status: String? = null,

        @field:SerializedName("RoomNo")
        val roomNo: String? = null,

        @field:SerializedName("Rno")
        val rno: String? = null,

        @field:SerializedName("rowcount")
        val rowcount: String? = null,

        @field:SerializedName("UserID")
        val userID: String? = null,

        @field:SerializedName("EmployeeRoomID")
        val employeeRoomID: String? = null,

        @field:SerializedName("RoomAddress")
        val roomAddress: String? = null,

        @field:SerializedName("EndDate")
        val endDate: String? = null,

        @field:SerializedName("EmployeeName")
        val employeeName: String? = null
)
