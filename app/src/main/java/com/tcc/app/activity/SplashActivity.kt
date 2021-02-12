package com.tcc.app.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import com.tcc.app.R
import com.tcc.app.extention.goToActivityAndClearTask

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)


        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            validateRedirection()
        }, 1000)
    }

    private fun validateRedirection() {
        if (session.isLoggedIn)
            goToActivityAndClearTask<HomeActivity>()
        else
            goToActivityAndClearTask<LoginActivity>()
    }
}