package com.tcc.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tcc.app.R
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_forgot_password)



        btnSubmit.setOnClickListener({
            finish()
        })
    }
}