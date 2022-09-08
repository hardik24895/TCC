package com.tcc.app.activity

import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.ForgotPasswordModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_forgot_password)

        btnSubmit.setOnClickListener {
            validation()
        }
    }

    fun validation() {
        when {
            edtEmail.isEmpty() -> {
                root.showSnackBar("Enter Email")
                edtEmail.requestFocus()
            }

            !isValidEmail(edtEmail.getValue()) -> {
                root.showSnackBar("Enter Valid Email")
                edtEmail.requestFocus()
            }
            else -> {
                forgotpwd()
            }

        }
    }

    fun forgotpwd() {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("EmailID", edtEmail.getValue())
            result = Networking.setParentJsonData(
                    Constant.METHOD_FORGOTPWD,
                    jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
                .with(this)
                .getServices()
                .forgotpwd(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackObserver<ForgotPasswordModal>() {
                    override fun onSuccess(response: ForgotPasswordModal) {
                        val data = response.data
                        hideProgressbar()
                        if (data != null) {
                            if (response.error == 200) {
                                // session.user = response
                                Animatoo.animateCard(this@ForgotPasswordActivity)
                                goToActivityAndClearTask<LoginActivity>()
                            } else {
                                showAlert(response.message.toString())
                            }

                        } else {
                            showAlert(response.message.toString())
                        }
                    }

                    override fun onFailed(code: Int, message: String) {
                        // showAlert(message)
                        showAlert(getString(R.string.show_server_error))
                        hideProgressbar()
                    }

                }).addTo(autoDisposable)
    }
}