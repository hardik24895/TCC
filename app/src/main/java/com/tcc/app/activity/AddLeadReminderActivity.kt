package com.tcc.app.activity

import android.os.Bundle
import android.widget.EditText
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.GetRoleModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_lead_reminder.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject

class AddLeadReminderActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_lead_reminder)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.room_allocation)

        edtReminderDate.setOnClickListener {
            showDateTimePicker(this@AddLeadReminderActivity, edtReminderDate)
        }
        edtProcessDate.setOnClickListener {
            showDateTimePicker(
                this@AddLeadReminderActivity, edtProcessDate
            )
        }
        edtReminderDate.setText(getCurrentDate())
        edtProcessDate.setText(getCurrentDate())

        edtReminderTime.setOnClickListener {
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.reminder_time)
            }.build().apply {
                setListener { hour, minute ->
                    Logger.d("time", hour.toString() + ":" + minute.toString())
                    var edStartTime1: EditText = findViewById(R.id.edtReminderTime)
                    edStartTime1.setText(
                        convertIntoTowDigit(hour) + ":" + convertIntoTowDigit(
                            minute
                        )
                    )

                }
            }.show(supportFragmentManager, "")

        }
        edtProcessTime.setOnClickListener {
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.procces_time)
            }.build().apply {
                setListener { hour, minute ->
                    Logger.d("time", hour.toString() + ":" + minute.toString())


                    var edEndTime1: EditText = findViewById(R.id.edtProcessTime)
                    edEndTime1.setText(convertIntoTowDigit(hour) + ":" + convertIntoTowDigit(minute))
                }
            }.show(supportFragmentManager, "")

        }



        btnSubmit.setOnClickListener {
            validation()

        }
    }


    fun validation() {
        when {
            edtMessage.isEmpty() -> {
                root.showSnackBar("Enter Message")
                edtMessage.requestFocus()
            }
            else -> {
                AddLeadReminder()
            }

        }
    }


    fun AddLeadReminder() {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("VisitorID", intent.getStringExtra(Constant.VISITOR_ID))
            jsonBody.put("Message", edtMessage.getValue())
            jsonBody.put(
                "ReminderDate",
                formatDateFromString(edtReminderDate.text.toString()) + " " + edtReminderTime.getValue()
            )
            jsonBody.put(
                "PastDate",
                formatDateFromString(edtProcessDate.text.toString()) + " " + edtProcessTime.getValue()
            )


            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_LEAD_REMINDER,
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
                    showAlert(message)
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }

}