package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.showDateTimePicker
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_add_employee.*
import kotlinx.android.synthetic.main.activity_add_room_allocation.*
import kotlinx.android.synthetic.main.activity_add_uniform.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class AddRoomAllocationActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_room_allocation)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.room_allocation)

        edtStartDate.setOnClickListener {
            showDateTimePicker(
                this@AddRoomAllocationActivity,
                edtStartDate
            )
        }
        edtEndDate.setOnClickListener {
            showDateTimePicker(
                this@AddRoomAllocationActivity,
                edtEndDate
            )
        }
    }

}