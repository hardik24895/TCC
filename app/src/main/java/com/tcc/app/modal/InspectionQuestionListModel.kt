package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class InspectionQuestionListModel(

    @field:SerializedName("data")
    val data: List<InspectionQuesDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class InspectionQuesDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Type")
    val type: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("QuestionID")
    val questionID: String? = null,

    @field:SerializedName("Question")
    val question: String? = null,

    @field:SerializedName("Questionoption")
    val questionoption: String? = null,

    var Answer: String? = ""
)
