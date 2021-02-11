package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.showDateTimePicker
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_add_employee.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class AddTicketActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_ticket)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Employee"



        edtJoiningDate.setOnClickListener {
            showDateTimePicker(this@AddTicketActivity, edtJoiningDate)
        }
    }

}