package com.tcc.app.modal

import com.google.gson.annotations.SerializedName

data class KeyRoleData(

        @field:SerializedName("data")
        val data: RoleData? = null
)

data class Uniform(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Payment(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Customer(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Tickets(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Penlty(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class RoleData(

        @field:SerializedName("Attendance")
        val attendance: Attendance? = null,

        @field:SerializedName("Training")
        val training: Training? = null,

        @field:SerializedName("Payment")
        val payment: Payment? = null,

        @field:SerializedName("Employee")
        val employee: Employee? = null,

        @field:SerializedName("Invoice")
        val invoice: Invoice? = null,

        @field:SerializedName("Visitor")
        val visitor: Visitor? = null,

        @field:SerializedName("Customer")
        val customer: Customer? = null,

        @field:SerializedName("Inspection")
        val inspection: Inspection? = null,

        @field:SerializedName("Tickets")
        val tickets: Tickets? = null,

        @field:SerializedName("Uniform")
        val uniform: Uniform? = null,

        @field:SerializedName("Penlty")
        val penlty: Penlty? = null,

        @field:SerializedName("Sites")
        val sites: Sites? = null
)

data class Sites(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Training(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Employee(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Attendance(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Visitor(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Invoice(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)

data class Inspection(

        @field:SerializedName("is_view")
        val isView: String? = null,

        @field:SerializedName("is_insert")
        val isInsert: String? = null,

        @field:SerializedName("is_edit")
        val isEdit: String? = null
)
