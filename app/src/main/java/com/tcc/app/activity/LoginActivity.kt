package com.tcc.app.activity

import android.os.Bundle
import android.util.Log
import android.view.Window
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.GetRoleModal
import com.tcc.app.modal.LoginModal
import com.tcc.app.modal.RoleItem
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.DeviceUtils
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        txtForgotpwd.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
        }

        btnLogin.setOnClickListener {
            validation()
        }

    }

    fun validation() {
        when {
            edtUsername.isEmpty() -> {
                root.showSnackBar("Enter Mobile Number")
                edtUsername.requestFocus()
            }
            edtUsername.getValue().length < 10 -> {
                root.showSnackBar("Enter Valid Mobile Number")
                edtUsername.requestFocus()
            }
            edtPassword.isEmpty() -> {
                root.showSnackBar("Enter Password")
                edtPassword.requestFocus()
            }
            edtPassword.getValue().length < 6 -> {
                edtPassword.requestFocus()
                root.showSnackBar("Enter Minimum six charecter")
            }

            else -> {
                login("")
            }

        }
    }

    fun login(fcmTokan: String?) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("EmailID", edtUsername.getValue())
            jsonBody.put("Password", edtPassword.getValue())
            jsonBody.put("DeviceUID", DeviceUtils.getDeviceId(this))
            jsonBody.put("DeviceName", DeviceUtils.getDeviceName())
            jsonBody.put("DeviceOS", DeviceUtils.getDeviceOS())
            jsonBody.put("OSVersion", DeviceUtils.getDeviceOSNumber())
            jsonBody.put("DeviceTokenID", fcmTokan)
            jsonBody.put("DeviceType", "Android")
            jsonBody.put("UserType", "Andriod")

            result = Networking.setParentJsonData(
                    Constant.METHOD_LOGIN,
                    jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
                .with(this)
                .getServices()
                .login(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackObserver<LoginModal>() {
                    override fun onSuccess(response: LoginModal) {
                        val data = response.data
                        hideProgressbar()
                        if (data != null) {
                            if (response.error == 200) {
                                session.user = response
                                getRole(data.roleID)
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

    fun getRole(roleID: String?) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("RoleID", roleID)


            result = Networking.setParentJsonData(
                    Constant.METHOD_ROLE,
                    jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
                .with(this)
                .getServices()
                .getRole(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackObserver<GetRoleModal>() {
                    override fun onSuccess(response: GetRoleModal) {
                        val data = response.data
                        hideProgressbar()
                        if (data != null) {
                            if (response.error == 200) {
                                saveDataToSharedPreferencesforRole(response.data)
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

    private fun saveDataToSharedPreferencesforRole(roleList: List<RoleItem>) {
        try {
            val jsonData = JSONObject()
            val jsonObject = JSONObject()
            for (i in roleList.indices) {
                val jsonEmp = JSONObject()
                jsonEmp.put("is_view", roleList.get(i).isView)
                jsonEmp.put("is_insert", roleList.get(i).isInsert)
                jsonEmp.put("is_edit", roleList.get(i).isEdit)
                jsonObject.put(roleList.get(i).moduleName.toString(), jsonEmp)
                jsonData.put("data", jsonObject)
            }
            Log.d("json", jsonData.toString())
            session.storeDataByKey(SessionManager.KEY_ROLE, jsonData.toString())
            Animatoo.animateCard(this@LoginActivity)
            goToActivity<HomeActivity>()
        } catch (e: Exception) {
        }
    }
}