package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.*
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
import kotlinx.android.synthetic.main.activity_add_salary.edtEndDate
import kotlinx.android.synthetic.main.activity_add_salary.edtStartDate
import kotlinx.android.synthetic.main.dialog_date_filter.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.txtTitle
import org.json.JSONException
import org.json.JSONObject


class AddSalaryActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_salary)
        txtTitle.text = "Salary"
        imgBack.visible()

        imgBack.setOnClickListener {
            finish()
        }

        edtStartDate.setText(getCurrentDate())


        edtStartDate.setText(TimeStamp.getStartDateRange())
        edtEndDate.setText(getCurrentDate())

        edtStartDate.setOnClickListener { showDateTimePicker(this@AddSalaryActivity, edtStartDate) }

        edtEndDate.setOnClickListener { showDateTimePicker(this@AddSalaryActivity, edtEndDate) }

        GetUserSalaryDetail()
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

                        //  edtSalary.setText(response.data.get(0).salary)
                        //  edtPerDaySalary.setText(response.data.get(0))
                        //  edtAbsent.setText(response.data.get(0).salary)
                        //  edtHalfDay.setText(response.data.get(0).salary)
                        //  edtHalfDayOt.setText(response.data.get(0).salary)
                        //  edtFullDay.setText(response.data.get(0).salary)


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
            jsonBody.put("UserID", Constant.PAGE_SIZE)
            jsonBody.put("EmployeeID", Constant.PAGE_SIZE)
            jsonBody.put("SalaryDate", Constant.PAGE_SIZE)
            jsonBody.put("StartDate", Constant.PAGE_SIZE)
            jsonBody.put("EndDate", Constant.PAGE_SIZE)
            jsonBody.put("Present", Constant.PAGE_SIZE)
            jsonBody.put("Absent", Constant.PAGE_SIZE)
            jsonBody.put("HalfDay", Constant.PAGE_SIZE)
            jsonBody.put("HalfOverTime", Constant.PAGE_SIZE)
            jsonBody.put("FullOverTime", Constant.PAGE_SIZE)
            jsonBody.put("Rate", Constant.PAGE_SIZE)
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


                }

                override fun onFailed(code: Int, message: String) {

                    hideProgressbar()

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }
}








