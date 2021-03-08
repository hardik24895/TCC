package com.tcc.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LeadListModal(

	@field:SerializedName("data")
	val data: List<LeadItem> = mutableListOf(),

	@field:SerializedName("rowcount")
	val rowcount: Int = 0,

	@field:SerializedName("sites")
	val sites: List<SitesItem?>? = null,

	@field:SerializedName("error")
	val error: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LeadItem(

	@field:SerializedName("MobileNo")
	val mobileNo: String? = null,

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("VisitorID")
	val visitorID: String? = null,

	@field:SerializedName("EmailID")
	val emailID: String? = null,

	@field:SerializedName("SitesCount")
	val sitesCount: String? = null,

	@field:SerializedName("Address")
	val address: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("StateID")
	val stateID: String? = null,

	@field:SerializedName("LeadType")
	val leadType: String? = null,

	@field:SerializedName("CityName")
	val cityName: String? = null,

	@field:SerializedName("CustomerID")
	val customerID: String? = null,

	@field:SerializedName("Name")
	val name: String? = null,

	@field:SerializedName("PinCode")
	val pinCode: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("CityID")
	val cityID: String? = null,

	@field:SerializedName("UserID")
	val userID: String? = null
) : Serializable

data class SitesItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("VisitorID")
	val visitorID: String? = null,

	@field:SerializedName("SitesID")
	val sitesID: String? = null,

	@field:SerializedName("SiteName")
	val siteName: String? = null,

	@field:SerializedName("Address")
	val address: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("GSTNo")
	val gSTNo: String? = null,

	@field:SerializedName("StateName")
	val stateName: String? = null,

	@field:SerializedName("Address2")
	val address2: String? = null,

	@field:SerializedName("StateID")
	val stateID: String? = null,

	@field:SerializedName("CustomerID")
	val customerID: String? = null,

	@field:SerializedName("CityName")
	val cityName: String? = null,

	@field:SerializedName("EndDate")
	val endDate: String? = null,

	@field:SerializedName("Name")
	val name: String? = null,

	@field:SerializedName("PinCode")
	val pinCode: String? = null,

	@field:SerializedName("ServiceID")
	val serviceID: String? = null,

	@field:SerializedName("WorkingHours")
	val workingHours: String? = null,

	@field:SerializedName("StartDate")
	val startDate: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("CityID")
	val cityID: String? = null,

	@field:SerializedName("UserID")
	val userID: String? = null,

	@field:SerializedName("SiteType")
	val siteType: String? = null,

	@field:SerializedName("ProposedDate")
	val proposedDate: String? = null,

	@field:SerializedName("WorkingDays")
	val workingDays: String? = null
) : Serializable
