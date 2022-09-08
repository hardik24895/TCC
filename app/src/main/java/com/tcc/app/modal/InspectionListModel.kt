package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InspectionListModel(

    @field:SerializedName("data")
    val data: List<InspectionDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: Int? = null
)

data class InspectionDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("SitesID")
    val sitesID: String? = null,

    @field:SerializedName("InspectionDate")
    val inspectionDate: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("SitesName")
    val sitesName: String? = null,

    @field:SerializedName("InspectionID")
    val inspectionID: String? = null,

    @field:SerializedName("Image")
    val image: String? = null,

    @field:SerializedName("CompanyName")
    val companyName: String? = null,

    @field:SerializedName("Item")
    val item: List<InspectionItem> = mutableListOf(),

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("Remarks")
    val remarks: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("EmployeeName")
    val employeeName: String? = null,

    @field:SerializedName("UserType")
    val userType: String? = null
) : Serializable

data class InspectionItem(

    @field:SerializedName("Answer")
    val answer: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("QuestionID")
    val questionID: String? = null,

    @field:SerializedName("InspectionID")
    val inspectionID: String? = null,

    @field:SerializedName("InspectionAnswerID")
    val inspectionAnswerID: String? = null,

    @field:SerializedName("Question")
    val question: String? = null
) : Serializable
