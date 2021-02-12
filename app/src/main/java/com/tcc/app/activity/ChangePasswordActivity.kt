package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.getValue
import com.tcc.app.extention.isEmpty
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.showSnackBar
import com.tcc.app.modal.ChangePasswordModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject


class ChangePasswordActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        txtTitle.setText(getString(R.string.change_password))

        imgBack.setOnClickListener {
            finish()
        }
        btnSubmit.setOnClickListener { validation() }

    }

    fun validation() {
        when {
            edtOldPassword.isEmpty() -> {
                root.showSnackBar("Enter OLD Password")
                edtOldPassword.requestFocus()
            }
            edtOldPassword.getValue().length < 6 -> {
                root.showSnackBar("Enter Minimum six charecter")
                edtOldPassword.requestFocus()
            }
            edtNewPassword.getValue().equals(edtOldPassword.getValue()) -> {
                edtConfirmPassword.requestFocus()
                root.showSnackBar("OLD Password and New Password can not be same")
            }
            edtNewPassword.isEmpty() -> {
                root.showSnackBar("Enter New Password")
                edtNewPassword.requestFocus()
            }
            edtNewPassword.getValue().length < 6 -> {
                edtNewPassword.requestFocus()
                root.showSnackBar("Enter Minimum six charecter")
            }
            !edtNewPassword.getValue().equals(edtConfirmPassword.getValue()) -> {
                edtConfirmPassword.requestFocus()
                root.showSnackBar("New Password and Confirm Password not matched")
            }

            else -> {
                changePWD()
            }

        }
    }

    fun changePWD() {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("OldPassword", edtOldPassword.getValue())
            jsonBody.put("Password", edtNewPassword.getValue())
            result = Networking.setParentJsonData(
                Constant.METHOD_CHANGE_PWD,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .changePassword(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ChangePasswordModal>() {
                override fun onSuccess(response: ChangePasswordModal) {
                    hideProgressbar()
                    if (response != null) {
                        if (response.error.equals("200")) {
                            root.showSnackBar(response.message.toString())
                            finish()
                        } else {
                            showAlert(response.message.toString())
                        }

                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }
}