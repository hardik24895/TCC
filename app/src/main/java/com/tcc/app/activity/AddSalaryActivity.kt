package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.modal.GetUserSalaryDetail
import com.tcc.app.modal.SalaryListModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.TimeStamp
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_salary.*
import kotlinx.android.synthetic.main.activity_add_salary.btnSubmit
import kotlinx.android.synthetic.main.activity_add_salary.root
import kotlinx.android.synthetic.main.activity_attendance.*
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
            calendarView.invisible()
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

    }

    fun GetUserSalaryDetail() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", Constant.PAGE_SIZE)
            jsonBody.put("StartDate", edtStartDate.getValue())
            jsonBody.put("EndDate", formatDateFromString(edtEndDate.getValue()))

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
            jsonBody.put("Rate", edtPayment.getValue())
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
            .getSalaryList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<SalaryListModal>() {
                override fun onSuccess(response: SalaryListModal) {
                    hideProgressbar()
                    if (response.error == 200) {

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








