package com.tcc.app.activity

import android.os.Bundle
import android.util.Log
import android.view.Window
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.DeviceUtils
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.SessionManager.Companion.IsFirst
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

        getStateList()
        getCofigData()
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
                root.showSnackBar("Enter Minimum six character")
            }

            else -> {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    login(token.toString())
                    // Log and toast
                    // val msg = getString(R.string.msg_token_fmt, token)
                    Log.d("token", token.toString())
                    //  Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })

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
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
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
                            session.clearSession()
                        }

                    } else {
                        showAlert(response.message.toString())
                        session.clearSession()
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }

    fun getStateList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            // jsonBody.put("StateID", "-1")

            result = Networking.setParentJsonData(Constant.METHOD_STATE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .getStateList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<StateListModal>() {
                override fun onSuccess(response: StateListModal) {
                    session.stetList = response.data
                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

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
            session.storeDataByKey(SessionManager.KEY_ROLE_DATA, jsonData.toString())

            if (session.getDataByKeyBoolean(IsFirst, true)) {
                goToActivityAndClearTask<WelComeActivity>()
            } else {
                goToActivityAndClearTask<HomeActivity>()
            }
        } catch (e: Exception) {
        }
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
            .with(this@LoginActivity)
            .getServices()
            .getConfigData(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ConfigDataModel>() {
                override fun onSuccess(response: ConfigDataModel) {

                    session.configData = response


                }

                override fun onFailed(code: Int, message: String) {

                }

            }).addTo(autoDisposable)
    }
}