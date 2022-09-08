package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EmployeeListModel(

        @field:SerializedName("data")
        val data: List<EmployeeDataItem> = mutableListOf(),

        @field:SerializedName("rowcount")
        val rowcount: Int? = null,

        @field:SerializedName("error")
        val error: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)

data class EmployeeDataItem(

    @field:SerializedName("MobileNo")
    val mobileNo: String? = null,

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("BankName")
    val bankName: String? = null,

    @field:SerializedName("EmailID")
    val emailID: String? = null,

    @field:SerializedName("Salary")
    val salary: String? = null,

    @field:SerializedName("Usertype")
    val usertype: String? = null,

    @field:SerializedName("Address")
    val address: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("AccountNo")
    val accountNo: String? = null,

    @field:SerializedName("FirstName")
    val firstName: String? = null,

    @field:SerializedName("IFSCCode")
    val iFSCCode: String? = null,

    @field:SerializedName("CityName")
    val cityName: String? = null,

    @field:SerializedName("RoleID")
    val roleID: String? = null,

    @field:SerializedName("UsertypeID")
    val usertypeID: String? = null,

    @field:SerializedName("OfferLetter")
    val offerLetter: String? = null,

    @field:SerializedName("WorkingHours")
    val workingHours: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("JoiningDate")
    val joiningDate: String? = null,

    @field:SerializedName("UserID")
    val userID: String? = null,

    @field:SerializedName("Documents")
    val documents: String? = null,

    @field:SerializedName("ProfilePic")
    val profilePic: String? = null,

    @field:SerializedName("LastName")
    val lastName: String? = null,

    @field:SerializedName("BranchName")
    val branchName: String? = null
) : Serializable
