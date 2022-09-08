package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class ChangePasswordModal(

    @field:SerializedName("error")
    val error: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("Password")
    val password: String? = null
)
