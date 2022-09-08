package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class StateListModal(

    @field:SerializedName("data")
    val data: MutableList<StateItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class StateItem(

    @field:SerializedName("StateName")
    val stateName: String? = null,

    @field:SerializedName("StateID")
    val stateID: String? = null
)
