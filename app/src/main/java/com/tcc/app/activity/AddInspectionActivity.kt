package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.showDateTimePicker
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_add_inspection.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class AddInspectionActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_inspection)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Inspection"

        edtDate.setOnClickListener { showDateTimePicker(this@AddInspectionActivity, edtDate) }
    }

}