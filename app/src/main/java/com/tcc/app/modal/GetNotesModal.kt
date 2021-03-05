package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class GetNotesModal(

	@field:SerializedName("data")
	val data: GetNotesData? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class GetNotesData(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("NoteID")
	val noteID: String? = null,

	@field:SerializedName("Type")
	val type: String? = null,

	@field:SerializedName("Note")
	val note: String? = null,

	@field:SerializedName("Term")
	val term: String? = null,

	@field:SerializedName("Title")
	val title: String? = null
)
