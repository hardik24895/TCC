package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.showDateTimePicker
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_add_employee.*
import kotlinx.android.synthetic.main.activity_add_uniform.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class AddUniformActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_uniform)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.uniform)

        edtDate.setOnClickListener { showDateTimePicker(this@AddUniformActivity, edtDate) }

    }

}