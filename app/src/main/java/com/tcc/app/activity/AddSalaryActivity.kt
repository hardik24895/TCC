package com.tcc.app.activity

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.RadioButton
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.modal.GetUserSalaryDetail
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp
import com.tcc.app.utils.TimeStamp.formatDateFromString
import com.tcc.app.widgets.DecimalDigitsInputFilter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_salary.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat


class AddSalaryActivity : BaseActivity() {

    var employeeDataItem: EmployeeDataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_salary)
        txtTitle.text = "Salary"
        imgBack.visible()
        if (intent.hasExtra(Constant.DATA)) {
            employeeDataItem = intent.getSerializableExtra(Constant.DATA) as EmployeeDataItem
        }

        imgBack.setOnClickListener {
            finish()
        }

        edtStartDate.setText(getCurrentDate())


        edtStartDate.setText(TimeStamp.getStartDateRange())
        edtEndDate.setText(getCurrentDate())

        edtStartDate.setOnClickListener { showDateTimePicker(this@AddSalaryActivity, edtStartDate) }

        edtEndDate.setOnClickListener { showDateTimePicker(this@AddSalaryActivity, edtEndDate) }

        edtPayment.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        edtAdvanceAmount.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))



        edtEndDate.setOnClickListener {
            showNextFromStartDateTimePicker(
                this@AddSalaryActivity,
                edtEndDate,
                edtStartDate.getValue()
            )
        }

        edtStartDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edtEndDate.setText(getCurrentDate())
                GetUserSalaryDetail()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        edtEndDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                GetUserSalaryDetail()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        GetUserSalaryDetail()

        btnSubmit.setOnClickListener {
            if (edtPayment.getValue().isEmpty()) {
                root.showSnackBar("Please enter Payable amount")
                edtPayment.requestFocus()
            } else if (edtPayment.getValue().toInt() <= 0 && edtAdvanceAmount.getValue()
                    .toInt() <= 0
            ) {
                root.showSnackBar("Salary or advance should not 0")
            } else {
                AddSalary()
            }
        }



        rg.setOnCheckedChangeListener({ group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals(getString(R.string.received))) {
                til11.visible()
            } else if (radio.text.equals(getString(R.string.paid))) {
                til11.visible()
            } else {
                til11.invisible()
            }
        })

    }

    fun GetUserSalaryDetail() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", employeeDataItem?.userID)
            jsonBody.put("StartDate", formatDateFromString(edtStartDate.getValue()))
            jsonBody.put("EndDate", formatDateFromString(edtEndDate.getValue()))
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_GET_SALARY_DATA,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(this@AddSalaryActivity)
            .getServices()
            .getUserSalaryList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<GetUserSalaryDetail>() {
                override fun onSuccess(response: GetUserSalaryDetail) {
                    hideProgressbar()
                    if (response.error == 200) {
                        var df = DecimalFormat("##.##")
                        edtSalary.setText(response.data.get(0).salary)
                        edtPerDaySalary.setText(df.format((response.data.get(0).salary?.toFloat()!! / 30)))
                        edtPresent.setText(response.data.get(0).presentCount)
                        edtAbsent.setText(response.data.get(0).absentCount)
                        edtHalfDay.setText(response.data.get(0).halfDayCount)
                        edtHalfDayOt.setText(response.data.get(0).halfOverTime)
                        edtFullDay.setText(response.data.get(0).fullOverTime)
                        edtAdvanceAmount.setText(response.data.get(0).advance)
                        edtPanaltyAmount.setText(response.data.get(0).penalty)
                        penalty_amount_title.text =
                            "${getString(R.string.panalty_amount)} : ${getString(R.string.RS)} ${
                                response.data.get(0).penalty
                            }"

                        adavance_amount_title.text =
                            "${getString(R.string.advance_amount)} : ${getString(R.string.RS)} ${
                                response.data.get(0).advance
                            }  (${response.data.get(0).advanceType})"


                        var tempTotalDay: Float =
                            response.data.get(0).presentCount?.toFloat()!! + response.data.get(
                                0
                            ).halfDayCount?.toFloat()!! + response.data.get(0).halfOverTime?.toFloat()!! + response.data.get(
                                0
                            ).fullOverTime?.toFloat()!!

                        edtPayment.setText(
                            df.format(
                                tempTotalDay * edtPerDaySalary.getValue().toFloat()
                            )
                        )


                    } else {
                        showAlert(response.message.toString())
                    }


                }

                override fun onFailed(code: Int, message: String) {

                    hideProgressbar()

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun AddSalary() {
        showProgressbar()
        var result = ""
        try {

            var payAmount: Float = 0f


            var adavance: Int = 0
            var Penalty: Int = 0

            if (!edtAdvanceAmount.getValue().equals("")) {
                adavance = edtAdvanceAmount.getValue().toInt()
            }

            if (!edtPanaltyAmount.getValue().equals("")) {
                Penalty = edtPanaltyAmount.getValue().toInt()
            }

            val jsonBody = JSONObject()

            if (rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId())) == 0) {
                payAmount = edtPayment.getValue().toFloat() - adavance - Penalty
                jsonBody.put("Type", "Received")
            } else if (rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId())) == 1) {
                payAmount = edtPayment.getValue().toFloat() + adavance - Penalty
                jsonBody.put("Type", "Paid")
            } else {
                payAmount = edtPayment.getValue().toFloat() - Penalty
                jsonBody.put("Type", "None")
            }

            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("EmployeeID", employeeDataItem?.userID.toString())
            jsonBody.put("SalaryDate", formatDateFromString(getCurrentDate()))
            jsonBody.put("StartDate", formatDateFromString(edtStartDate.getValue()))
            jsonBody.put("EndDate", formatDateFromString(edtEndDate.getValue()))
            jsonBody.put("Present", edtPresent.getValue())
            jsonBody.put("Absent", edtAbsent.getValue())
            jsonBody.put("HalfDay", edtHalfDay.getValue())
            jsonBody.put("HalfOverTime", edtHalfDayOt.getValue())
            jsonBody.put("FullOverTime", edtFullDay.getValue())
            jsonBody.put("Rate", edtPerDaySalary.getValue())
            jsonBody.put("Penalty", edtPanaltyAmount.getValue())
            jsonBody.put("PayAmount", payAmount)
            jsonBody.put("Amount", edtAdvanceAmount.getValue())

            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_SALARY,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(this@AddSalaryActivity)
            .getServices()
            .addSalary(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        finish()
                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {

                    hideProgressbar()

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }
}








