package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class RoleListModel(

    @field:SerializedName("data")
    val data: List<RoleDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class RoleDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("RoleName")
    val roleName: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("Description")
    val description: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("RoleID")
    val roleID: String? = null
)
