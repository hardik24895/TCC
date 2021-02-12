package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class GetRoleModal(

        @field:SerializedName("data")
        val data: List<RoleItem> = mutableListOf(),

        @field:SerializedName("error")
        val error: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)

data class RoleItem(

        @field:SerializedName("ParentID")
        val parentID: String? = null,

        @field:SerializedName("ModuleID")
        val moduleID: String? = null,

        @field:SerializedName("is_status")
        val isStatus: String? = null,

        @field:SerializedName("RoleMapID")
        val roleMapID: String? = null,

        @field:SerializedName("ModuleName")
        val moduleName: String? = null,

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_export")
        val isExport: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null,

        @field:SerializedName("RoleID")
        val roleID: String? = null
)
