package com.tcc.app.activity


import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.getValue
import com.tcc.app.extention.visible
import com.tcc.app.fragment.CustomerFragment
import com.tcc.app.fragment.LeadFragment
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class SearchActivity : BaseActivity() {
    var type: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }

        txtTitle.text = getString(R.string.filter)

        getBundleData()

        btnSubmit.setOnClickListener {
            filter()
        }
    }

    fun getBundleData() {
        type = intent.getStringExtra(Constant.DATA)
        if (type.equals(Constant.LEAD) || type.equals(Constant.CUSTOMER)) {
            inEmail.visible()
            inName.visible()
        }
    }

    fun filter() {
        if (type.equals(Constant.LEAD)) {
            LeadFragment.email = edtEmail.getValue()
            LeadFragment.name = edtName.getValue()
            finish()
        }
        if (type.equals(Constant.CUSTOMER)) {
            CustomerFragment.email = edtEmail.getValue()
            CustomerFragment.name = edtName.getValue()
            finish()
        }

    }
}