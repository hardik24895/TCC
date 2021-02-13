package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class TrainingSpinnerListModel(

    @field:SerializedName("data")
    val data: List<TrainingSpinnerDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class TrainingSpinnerDataItem(

    @field:SerializedName("Training")
    val training: String? = null,

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("TrainingID")
    val trainingID: String? = null,

    @field:SerializedName("TrainingTime")
    val trainingTime: String? = null,

    @field:SerializedName("TrainingDateTimeID")
    val trainingDateTimeID: String? = null,

    @field:SerializedName("TrainingDate")
    val trainingDate: String? = null
)
