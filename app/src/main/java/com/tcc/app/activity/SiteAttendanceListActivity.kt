package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.adapter.AttendanceListAdapter
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.AllEmpAttendanceDataItem
import com.tcc.app.modal.AllEmpAttendanceListModel
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.modal.QuotationItem
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp.formatDateFromString
import com.tcc.app.utils.TimeStamp.getStartDateRange
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_gloab_attedance_list.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject


class SiteAttendanceListActivity : BaseActivity(), AttendanceListAdapter.OnItemSelected {


    private val list: MutableList<AllEmpAttendanceDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true

    var empItemData: EmployeeDataItem? = null
    var adapter: AttendanceListAdapter? = null
    var startDate: String = getStartDateRange()
    var endDate: String = getCurrentDate()
    var quotationItem: QuotationItem? = null

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = AttendanceListAdapter(this, list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: AllEmpAttendanceDataItem) {

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.home, menu)
//        val filter = menu.findItem(R.id.action_filter)
//        filter.setVisible(true)
//
//        val add = menu.findItem(R.id.action_add)
//        add.setVisible(true)
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_add -> {
//                if (checkUserRole(
//                        session.roleData.data?.attendance?.isInsert.toString(),
//                        this
//                    )
//                ) {
//                    goToActivity<AddGlobalAttendanceActivity>()
//                }
//                return true
//            }
//            R.id.action_filter -> {
//                showDateFilteryDialog()
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_gloab_attedance_list)



        if (intent.hasExtra(Constant.DATA)) {
            quotationItem = intent.getSerializableExtra(Constant.DATA) as QuotationItem

            txtTitle.text = quotationItem?.siteName
        }

        toolbarView?.visible()
        imgAdd.visible()


        imgBack.setOnClickListener {
            finish()
        }

        imgAdd.setOnClickListener {


            if (checkUserRole(session.roleData.data?.attendance?.isInsert.toString(), this)) {
                val i = Intent(this, AddAttendanceActivity::class.java)
                i.putExtra(Constant.DATA, quotationItem)
                startActivity(i)
                Animatoo.animateCard(this)
            }
        }

//        if (b == true)
//            setHomeScreenTitle(requireActivity(), getString(R.string.nav_attendance))


        txtDateRage.text = startDate + " TO " + endDate

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

    override fun onResume() {
        super.onResume()
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getAttendenceList(page)
    }

//    fun showDateFilteryDialog() {
//        val dialog = DateFilterDailog.newInstance(this,
//            object : DateFilterDailog.onItemClick {
//                override fun onItemCLicked(strdate: String, enddate: String) {
//                    startDate = strdate
//                    endDate = enddate
//
//                    txtDateRage.text = startDate + " TO " + endDate
//                    page = 1
//                    list.clear()
//                    hasNextPage = true
//                    swipeRefreshLayout.isRefreshing = true
//                    recyclerView.isLoading = true
//                    getAttendenceList(page)
//
//                }
//            })
//        val bundle = Bundle()
//        bundle.putString(Constant.TITLE, getString(R.string.app_name))
////        bundle.putString(
////            Constant.TEXT,
////            getString(R.string.msg_get_data_from_server)
////        )
//        dialog.arguments = bundle
//        dialog.show(supportFragmentManager, "YesNO")
//    }

    fun getAttendenceList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("StartDate", formatDateFromString(startDate))
            jsonBody.put("EndDate", formatDateFromString(endDate))
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            jsonBody.put("SiteID", quotationItem?.sitesID)

            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_ALL_EMPLOYEE_ATTENDENCE,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(this)
            .getServices()
            .getAllEmpList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<AllEmpAttendanceListModel>() {
                override fun onSuccess(response: AllEmpAttendanceListModel) {
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
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                    refreshData(message, code)
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