package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class ForgotPasswordModal(

        @field:SerializedName("data")
        val data: ForgotData? = null,

        @field:SerializedName("error")
        val error: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)

data class ForgotData(

        @field:SerializedName("MobileNo")
        val mobileNo: String? = null,

        @field:SerializedName("EmailID")
        val emailID: String? = null,

        @field:SerializedName("Salary")
        val salary: String? = null,

        @field:SerializedName("Address")
        val address: String? = null,

        @field:SerializedName("FirstName")
        val firstName: String? = null,

        @field:SerializedName("RoleID")
        val roleID: String? = null,

        @field:SerializedName("UsertypeID")
        val usertypeID: String? = null,

        @field:SerializedName("UserID")
        val userID: String? = null,

        @field:SerializedName("RegistrationType")
        val registrationType: String? = null,

        @field:SerializedName("LastName")
        val lastName: String? = null,

        @field:SerializedName("PhotoURL")
        val photoURL: String? = null,

        @field:SerializedName("UserType")
        val userType: String? = null,

        @field:SerializedName("Password")
        val password: String? = null
)
