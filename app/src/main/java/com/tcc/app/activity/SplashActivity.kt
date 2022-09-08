package com.tcc.app.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import com.tcc.app.R
import com.tcc.app.extention.goToActivityAndClearTask
import com.tcc.app.modal.ConfigDataModel
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_quotation.*
import org.json.JSONException
import org.json.JSONObject

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


        getCofigData()

    }

    private fun validateRedirection() {
        if (session.isLoggedIn) {
            if (session.getDataByKeyBoolean(SessionManager.IsFirst, true)) {
                goToActivityAndClearTask<WelComeActivity>()
            } else {
                goToActivityAndClearTask<HomeActivity>()
            }
        } else
            goToActivityAndClearTask<LoginActivity>()
    }


    fun getCofigData() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", "")

            result = Networking.setParentJsonData(Constant.METHOD_CONFIG, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@SplashActivity)
            .getServices()
            .getConfigData(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ConfigDataModel>() {
                override fun onSuccess(response: ConfigDataModel) {

                    session.configData = response

                    Handler(Looper.getMainLooper()).postDelayed({
                        validateRedirection()
                    }, 1000)
                }

                override fun onFailed(code: Int, message: String) {


                    Handler(Looper.getMainLooper()).postDelayed({
                        validateRedirection()
                    }, 1000)

                }

            }).addTo(autoDisposable)
    }
}