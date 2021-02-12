package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class CityListModel(

        @field:SerializedName("data")
        val data: List<CityDataItem> = mutableListOf(),

        @field:SerializedName("error")
        val error: Int? = null,

        @field:SerializedName("message")
        val message: String? = null
)

data class CityDataItem(

        @field:SerializedName("Status")
        val status: String? = null,

        @field:SerializedName("Rno")
        val rno: String? = null,

        @field:SerializedName("CityID")
        val cityID: String? = null,

        @field:SerializedName("rowcount")
        val rowcount: String? = null,

        @field:SerializedName("CountryName")
        val countryName: String? = null,

        @field:SerializedName("StateName")
        val stateName: String? = null,

        @field:SerializedName("StateID")
        val stateID: String? = null,

        @field:SerializedName("CityName")
        val cityName: String? = null,

        @field:SerializedName("IsOpen")
        val isOpen: String? = null
)
