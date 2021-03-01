package com.tcc.app.activity

import android.os.Bundle
import android.view.View
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_payment.*
import kotlinx.android.synthetic.main.activity_add_salary.*
import kotlinx.android.synthetic.main.activity_add_salary.btnSubmit
import kotlinx.android.synthetic.main.activity_add_salary.rg
import kotlinx.android.synthetic.main.activity_add_salary.root
import kotlinx.android.synthetic.main.fragment_home.*
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

        GetUserSalaryDetail()



        btnSubmit.setOnClickListener {
            if (edtPayment.getValue().isEmpty()) {
                root.showSnackBar("Please enter Payable amount")
                edtPayment.requestFocus()
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
            jsonBody.put("UserID", Constant.PAGE_SIZE)
            jsonBody.put("StartDate", edtStartDate.getValue())
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
                        edtAdvanceAmount.setText("0")
                        edtPanaltyAmount.setText("0")
                        penalty_amount_title.append(
                            " : ${getString(R.string.RS)} ${
                                response.data.get(
                                    0
                                ).penalty
                            }"
                        )
                        adavance_amount_title.append(
                            " : ${getString(R.string.RS)} ${
                                response.data.get(
                                    0
                                ).advance
                            }"
                        )


                    } else {
                        showAlert(response.message.toString())
                    }


                }

                override fun onFailed(code: Int, message: String) {

                    hideProgressbar()

                    showAlert(message)

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



            if (rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId())) == 0) {
                payAmount = edtPayment.getValue().toFloat() - adavance - Penalty
            } else if (rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId())) == 1) {
                payAmount = edtPayment.getValue().toFloat() + adavance - Penalty
            } else {
                payAmount = edtPayment.getValue().toFloat() - Penalty
            }


            val rbType = findViewById<View>(rg.getCheckedRadioButtonId()) as? RadioButton


            val jsonBody = JSONObject()
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
            jsonBody.put("Type", rbType?.text.toString())


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

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }
}








