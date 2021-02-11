package com.tcc.app.activity

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.tcc.app.R
import com.tcc.app.extention.goToActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        txtForgotpwd.setOnClickListener({
            goToActivity<ForgotPasswordActivity>()
        })

        btnLogin.setOnClickListener({
            goToActivity<HomeActivity>()
        })

    }
}