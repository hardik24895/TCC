package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class InspectionDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspection_detail)
        txtTitle.text = "Hardik Kanzariya"
        imgBack.visible()
        clickEvent()
    }


    private fun clickEvent() {
        imgBack.setOnClickListener { onBackPressed() }

    }


}