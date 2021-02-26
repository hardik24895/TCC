package com.tcc.app.activity

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.AttendanceAdapter
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
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


class AddAttendanceActivity : BaseActivity(), AttendanceAdapter.OnItemSelected {

    private val list: ArrayList<AttendanceListDataItem> = ArrayList()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var selectedDateStr: String = getCurrentDate()
    var adapter: AttendanceAdapter? = null
    var quotationItem: QuotationItem? = null

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

        if (intent.hasExtra(Constant.DATA)) {
            quotationItem = intent.getSerializableExtra(Constant.DATA) as QuotationItem
            calendarView.invisible()
        } else {
            calendarView.visible()
        }

        btnSubmit.setOnClickListener {
            AddAttendence()
        }


        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getAttendenceList(page)


        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getAttendenceList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getAttendenceList(page)
        }

    }

    fun horizontalCalender() {


        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)
        val endDate: Calendar = Calendar.getInstance()

        val horizontalCalendar: HorizontalCalendar =
            HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build()
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                selectedDateStr = DateFormat.format("dd/MM/yyyy", date).toString()


            }
        }
    }


    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = AttendanceAdapter(this, list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(
        position: Int,
        data: AttendanceListDataItem,
        action: String,
        attendance: String,
        overtime: String
    ) {

        list.set(
            position, AttendanceListDataItem(
                status = data.status,
                mobileNo = data.mobileNo,
                attendance = attendance,
                teamdefinationID = data.teamdefinationID,
                sitesID = data.sitesID,
                endTime = data.endTime,
                estimateNo = data.estimateNo,
                rowcount = data.rowcount,
                startTime = data.startTime,
                quotationID = data.quotationID,
                endDate = data.endDate,
                startDate = data.startDate,
                type = data.type,
                attendanceID = data.attendanceID,
                rno = data.rno,
                overtime = overtime,
                userID = data.userID,
                employeeName = data.employeeName
            )
        )


    }

//    fun showBottomSheetDialog() {
//        val dialog = AttendanceBottomSheetDialog
//            .newInstance(
//                this,
//                object : AttendanceBottomSheetDialog.OnItemClick {
//                    override fun onItemClicked(message: String) {
//                        if (Constant.OVERTIME.equals(message)) {
//                            showOverTimeDialog()
//                        } else if (Constant.LATEFINE.equals(message)) {
//                            showLateFineDialog()
//                        }
//                    }
//
//                    override fun onError(message: String) {
//                        showAlert(message)
//                    }
//                })
//        dialog.show(supportFragmentManager, "ImagePicker")
//
//    }
//
//    fun showOverTimeDialog() {
//        val dialog = OverTimeBottomSheetDialog
//            .newInstance(
//                this,
//                object : OverTimeBottomSheetDialog.OnItemClick {
//                    override fun onItemClicked(message: String) {
//                        if (Constant.OVERTIME.equals(message)) {
//
//                        }
//                    }
//
//
//                    override fun onError(message: String) {
//                        showAlert(message)
//                    }
//                })
//        dialog.show(supportFragmentManager, "ImagePicker")
//    }
//
//    fun showLateFineDialog() {
//        val dialog = LateFineBottomSheetDialog
//            .newInstance(
//                this,
//                object : LateFineBottomSheetDialog.OnItemClick {
//                    override fun onItemClicked(message: String) {
//                        if (Constant.OVERTIME.equals(message)) {
//
//                        }
//                    }
//
//                    override fun onError(message: String) {
//                        showAlert(message)
//                    }
//                })
//        dialog.show(supportFragmentManager, "ImagePicker")
//    }


    fun getAttendenceList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("SitesID", quotationItem?.sitesID)
            jsonBody.put("CustomerID", quotationItem?.customerID)
            jsonBody.put("QuotationID", quotationItem?.quotationID)
            jsonBody.put("AttendanceDate", formatDateFromString(selectedDateStr!!))
            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_ATTEDANCE_LIST,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(this@AddAttendanceActivity)
            .getServices()
            .getAttendenceList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<AttendanceListModel>() {
                override fun onSuccess(response: AttendanceListModel) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    swipeRefreshLayout.isRefreshing = false

                    if (response.error == 200) {
                        list.addAll(response.data)
                        adapter?.notifyItemRangeInserted(
                            list.size.minus(response.data.size),
                            list.size
                        )
                        hasNextPage = list.size < response.rowcount!!
                    }

                  refreshData(getString(R.string.no_data_found), 1)
                }

                override fun onFailed(code: Int, message: String) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    showAlert(message)
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

                //   if (!list.get(item).attendance?.equals("")!!) {
                val jsonObj = JSONObject()
                jsonObj.put("EmployeeID", list.get(item).userID)
                jsonObj.put("Attendance", list.get(item).attendance)
                jsonObj.put("Overtime", list.get(item).overtime)

                jsonArray.put(jsonObj)

                Log.e("TAG", "AddAttendence: " + jsonObj.toString())
                //   }
            }

            jsonBody.put("AttendanceDate", formatDateFromString(selectedDateStr))
            jsonBody.put("SitesID", quotationItem?.sitesID)
            jsonBody.put("QuotationID", quotationItem?.quotationID)
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
            .with(this@AddAttendanceActivity)
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
                    showAlert(message)
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