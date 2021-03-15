package com.tcc.app.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.modal.GetRoleModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_room_allocation.*
import kotlinx.android.synthetic.main.activity_add_room_allocation.btnSubmit
import kotlinx.android.synthetic.main.activity_add_room_allocation.edtEndDate
import kotlinx.android.synthetic.main.activity_add_room_allocation.edtStartDate
import kotlinx.android.synthetic.main.activity_add_room_allocation.root
import kotlinx.android.synthetic.main.activity_add_site.*
import kotlinx.android.synthetic.main.dialog_accept_reason.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.txtTitle
import org.json.JSONException
import org.json.JSONObject

class AddRoomAllocationActivity : BaseActivity() {


    var employeeData: EmployeeDataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_room_allocation)

        if (intent.hasExtra(Constant.DATA)) {
            employeeData = intent.getSerializableExtra(Constant.DATA) as EmployeeDataItem
        }


        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.room_allocation)

        edtStartDate.setOnClickListener {
            showDateTimePicker(this@AddRoomAllocationActivity, edtStartDate)
        }
//        edtEndDate.setOnClickListener {
//            showDateTimePicker(
//                this@AddRoomAllocationActivity, edtEndDate
//            )
//        }

        edtEndDate.setOnClickListener {
            showNextFromStartDateTimePicker(
                this@AddRoomAllocationActivity,
                edtEndDate,
                edtStartDate.getValue()
            )
        }

        edtStartDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edtEndDate.setText(edtStartDate.getValue())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        edtStartDate.setText(getCurrentDate())
        edtEndDate.setText(getCurrentDate())


        btnSubmit.setOnClickListener {
            validation()

        }
    }


    fun validation() {
        when {
            edtRoomNo.isEmpty() -> {
                root.showSnackBar("Enter Room Number")
                edtRoomNo.requestFocus()
            }
            edtRoomAddress.isEmpty() -> {
                root.showSnackBar("Enter Room Address")
                edtRoomAddress.requestFocus()
            }
            edtStartDate.isEmpty() -> {
                root.showSnackBar("Select Start Date")
                edtStartDate.requestFocus()
            }
            edtEndDate.isEmpty() -> {
                root.showSnackBar("Select End Date")
                edtEndDate.requestFocus()
            }
            else -> {
                AddEmployee()
            }

        }
    }


    fun AddEmployee() {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("RoomNo", edtRoomNo.text)
            jsonBody.put("RoomAddress", edtRoomAddress.text)
            jsonBody.put("StartDate", formatDateFromString(edtStartDate.text.toString()))
            jsonBody.put("EndDate", formatDateFromString(edtEndDate.text.toString()))
            jsonBody.put("UserID", employeeData?.userID)


            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_ROOM,
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
                            finish()

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