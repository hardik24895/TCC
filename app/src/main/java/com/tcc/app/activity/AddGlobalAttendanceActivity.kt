package com.tcc.app.activity

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.GlobalAttendanceAdapter
import com.tcc.app.extention.*
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.modal.GlobalEmployeeAttedanceDataItem
import com.tcc.app.modal.GlobalEmployeeAttedanceListModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp.formatDateFromString
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_attendance.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class AddGlobalAttendanceActivity : BaseActivity(), GlobalAttendanceAdapter.OnItemSelected {

    private val list: ArrayList<GlobalEmployeeAttedanceDataItem> = ArrayList()
    var selectedDateStr: String = getCurrentDate()
    var adapter: GlobalAttendanceAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_attendance)
        txtTitle.text = "Attendance"
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        horizontalCalender()

        calendarView.visible()

        btnSubmit.setOnClickListener {
            AddAttendence()
        }
        list.clear()
        setupRecyclerView()
        getGloablAttendanceList()
        chbAllPresent.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                chbAllAbsent.isChecked = false
                updateAttendance("1")
            }
        }

        chbAllAbsent.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                chbAllPresent.isChecked = false
                updateAttendance("0")
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            list.clear()
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getGloablAttendanceList()
        }

    }

    private fun updateAttendance(attendance: String) {
        for (i in list.indices) {
            list.set(
                i, GlobalEmployeeAttedanceDataItem(
                    userID = list.get(i).userID,
                    roleID = list.get(i).roleID,
                    emailID = list.get(i).emailID,
                    firstName = list.get(i).firstName,
                    lastName = list.get(i).lastName,
                    mobileNo = list.get(i).mobileNo,
                    address = list.get(i).address,
                    profilePic = list.get(i).profilePic,
                    status = list.get(i).status,
                    salary = list.get(i).salary,
                    usertypeID = list.get(i).usertypeID,
                    usertype = list.get(i).usertype,
                    workingHours = list.get(i).workingHours,
                    documents = list.get(i).documents,
                    offerLetter = list.get(i).offerLetter,
                    bankName = list.get(i).bankName,
                    branchName = list.get(i).branchName,
                    accountNo = list.get(i).accountNo,
                    iFSCCode = list.get(i).iFSCCode,
                    joiningDate = list.get(i).joiningDate,
                    cityName = list.get(i).cityName,
                    attendance = attendance,
                    overtime = list.get(i).overtime,
                    attendanceID = list.get(i).attendanceID,
                    rno = list.get(i).rno,
                    rowcount = list.get(i).rowcount
                )
            )
        }
        adapter?.notifyDataSetChanged()
    }

    fun horizontalCalender() {


        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 0)

        val horizontalCalendar: HorizontalCalendar =
            HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build()

        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                selectedDateStr = DateFormat.format("dd/MM/yyyy", date).toString()

                list.clear()

                getGloablAttendanceList()

            }
        }
    }


    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = GlobalAttendanceAdapter(this, list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(
        position: Int,
        data: GlobalEmployeeAttedanceDataItem,
        action: String,
        attendance: String,
        overtime: String
    ) {

        list.set(
            position, GlobalEmployeeAttedanceDataItem(
                userID = data.userID,
                roleID = data.roleID,
                emailID = data.emailID,
                firstName = data.firstName,
                lastName = data.lastName,
                mobileNo = data.mobileNo,
                address = data.address,
                profilePic = data.profilePic,
                status = data.status,
                salary = data.salary,
                usertypeID = data.usertypeID,
                usertype = data.usertype,
                workingHours = data.workingHours,
                documents = data.documents,
                offerLetter = data.offerLetter,
                bankName = data.bankName,
                branchName = data.branchName,
                accountNo = data.accountNo,
                iFSCCode = data.iFSCCode,
                joiningDate = data.joiningDate,
                cityName = data.cityName,
                attendance = attendance,
                overtime = overtime,
                attendanceID = data.attendanceID,
                rno = data.rno,
                rowcount = data.rowcount
            )
        )

    }

    fun getGloablAttendanceList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", -1)
            jsonBody.put("CurrentPage", 1)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            jsonBody.put("AttendanceDate", formatDateFromString(selectedDateStr))
            result = Networking.setParentJsonData(
                Constant.METHOD_GLOBAL_EMPLOYEE_LIST,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(this@AddGlobalAttendanceActivity)
            .getServices()
            .getGloablAttendanceList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<GlobalEmployeeAttedanceListModal>() {
                override fun onSuccess(response: GlobalEmployeeAttedanceListModal) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    swipeRefreshLayout.isRefreshing = false

                    if (response.error == 200) {
                        list.addAll(response.data)
                        adapter?.notifyDataSetChanged()

                    }

                    refreshData(getString(R.string.no_data_found), 1)
                }

                override fun onFailed(code: Int, message: String) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                    refreshData(message, code)
                }

            }).addTo(autoDisposable)
    }


    fun AddAttendence() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            val jsonArray = JSONArray()

            for (item in list.indices) {

                val jsonObj = JSONObject()
                jsonObj.put("EmployeeID", list.get(item).userID)
                jsonObj.put("Attendance", list.get(item).attendance)
                jsonObj.put("Overtime", list.get(item).overtime)

                jsonArray.put(jsonObj)

                Log.e("TAG", "AddAttendence: " + jsonObj.toString())
            }

            jsonBody.put("AttendanceDate", formatDateFromString(selectedDateStr))
            jsonBody.put("SitesID", "0")
            jsonBody.put("QuotationID", "0")
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Item", jsonArray)

            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_EMPLOYEE_ATTENDENCE,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(this@AddGlobalAttendanceActivity)
            .getServices()
            .AddAttendence(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        finish()
                    } else {
                        showAlert(response.message.toString())
                    }
                    hideProgressbar()
                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }

    private fun refreshData(msg: String?, code: Int) {
        recyclerView.setLoadedCompleted()
        swipeRefreshLayout.isRefreshing = false
        adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            imgNodata.invisible()
            recyclerView.visible()
        } else {
            imgNodata.visible()
            if (code == 0)
                imgNodata.setImageResource(R.drawable.no_internet_bg)
            else
                imgNodata.setImageResource(R.drawable.nodata)
            recyclerView.invisible()
        }
    }

}