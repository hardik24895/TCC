package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.visible
import com.tcc.app.modal.LeadItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_add_visitor.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class AddLeadActivity : BaseActivity() {
    var leadItem: LeadItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_visitor)
        getBundleData()
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = resources.getText(R.string.visitor)
        btnSubmit.setOnClickListener { finish() }
        btnAddSite.setOnClickListener { goToActivity<AddSiteActivity>() }
    }

    fun getBundleData() {
        if (intent.hasExtra(Constant.DATA)) {
            leadItem = intent.getSerializableExtra(Constant.DATA) as LeadItem
        }
    }

    fun setData() {
        edtName.setText(leadItem?.name.toString())
        edtEmail.setText(leadItem?.emailID.toString())
        edtMobile.setText(leadItem?.mobileNo.toString())
        edtName.setText(leadItem?.name.toString())
        edtName.setText(leadItem?.name.toString())

    }
}