package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class ConfigDataModel(

    @field:SerializedName("data")
    val data: ConfigData? = null,

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ConfigData(

    @field:SerializedName("MailFromName")
    val mailFromName: String? = null,

    @field:SerializedName("AppVersionAndroid")
    val appVersionAndroid: String? = null,

    @field:SerializedName("TimeZone")
    val timeZone: String? = null,

    @field:SerializedName("AppVersionIOS")
    val appVersionIOS: String? = null,

    @field:SerializedName("SGST")
    val sGST: String? = null,

    @field:SerializedName("SupportEmail")
    val supportEmail: String? = null,

    @field:SerializedName("CGST")
    val cGST: String? = null,

    @field:SerializedName("ConfigID")
    val configID: String? = null,

    @field:SerializedName("LeadType")
    val leadType: String? = null,

    @field:SerializedName("Online")
    val online: String? = null,

    @field:SerializedName("IGST")
    val iGST: String? = null
)
