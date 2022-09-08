package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class UniformListModel(

        @field:SerializedName("data")
        val data: List<UniformDataItem> = mutableListOf(),

        @field:SerializedName("rowcount")
        val rowcount: Int? = null,

        @field:SerializedName("error")
        val error: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)

data class UniformDataItem(

        @field:SerializedName("Status")
        val status: String? = null,

        @field:SerializedName("UniformDate")
        val uniformDate: String? = null,

        @field:SerializedName("Rno")
        val rno: String? = null,

        @field:SerializedName("Uniformtype")
        val uniformtype: String? = null,

        @field:SerializedName("rowcount")
        val rowcount: String? = null,

        @field:SerializedName("EmployeeUniformID")
        val employeeUniformID: String? = null,

        @field:SerializedName("UserID")
        val userID: String? = null,

        @field:SerializedName("EmployeeName")
        val employeeName: String? = null,

        @field:SerializedName("UniformTypeID")
        val uniformTypeID: String? = null
)
