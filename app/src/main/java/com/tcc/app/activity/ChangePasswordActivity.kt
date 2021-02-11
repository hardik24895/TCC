package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*


class ChangePasswordActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)


        txtTitle.setText(getString(R.string.change_password))

        imgBack.setOnClickListener {
            finish()
        }

    }
}