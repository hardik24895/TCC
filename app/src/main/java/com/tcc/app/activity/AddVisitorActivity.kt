package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_add_visitor.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class AddVisitorActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_visitor)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = resources.getText(R.string.visitor)
        btnSubmit.setOnClickListener { finish() }
        btnAddSite.setOnClickListener { goToActivity<AddSiteActivity>() }
    }

}