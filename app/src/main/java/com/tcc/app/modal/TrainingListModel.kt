package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class TrainingListModel(

    @field:SerializedName("data")
    val data: List<TrainingDataItem> = mutableListOf(),

    @field:SerializedName("rowcount")
    val rowcount: Int? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class TrainingDataItem(

    @field:SerializedName("Training")
    val training: String? = null,

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("TrainingTime")
    val trainingTime: String? = null,

    @field:SerializedName("TrainingDateTimeID")
    val trainingDateTimeID: String? = null,

    @field:SerializedName("TrainingEmployeeID")
    val trainingEmployeeID: String? = null,

    @field:SerializedName("EmployeeID")
    val employeeID: String? = null,

    @field:SerializedName("EmployeeName")
    val employeeName: String? = null,

    @field:SerializedName("TrainingDate")
    val trainingDate: String? = null
)
