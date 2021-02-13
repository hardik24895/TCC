package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.goToActivityAndClearTask
import com.tcc.app.extention.showDateTimePicker
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_add_site.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import java.util.*

class AddSiteActivity : BaseActivity() {
    var cal = Calendar.getInstance()
    var year = cal.get(Calendar.YEAR)
    var month = cal.get(Calendar.MONTH)
    var day = cal.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_site)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Site"
        btnSubmit.setOnClickListener { goToActivityAndClearTask<HomeActivity>() }

        btnAddQuatation.setOnClickListener { goToActivity<AddQuotationActivity>() }


        edtSdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtSdate) }
        edtEdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtEdate) }
        edtPdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtPdate) }
    }

}