package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.CustomerAttendanceListAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.CustomerAttendanceDataItem
import com.tcc.app.modal.CustomerAttendanceListModal
import com.tcc.app.modal.CustomerDataItem
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_employee_training.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.imgNodata
import kotlinx.android.synthetic.main.reclerview_swipelayout.progressbar
import kotlinx.android.synthetic.main.reclerview_swipelayout.recyclerView
import kotlinx.android.synthetic.main.reclerview_swipelayout.swipeRefreshLayout
import org.json.JSONException
import org.json.JSONObject


class CustomerAttendanceListFragment() : BaseFragment(),
    CustomerAttendanceListAdapter.OnItemSelected {
    constructor(empData: CustomerDataItem?) : this() {
        this.empItemData = empData
    }

    var empItemData: CustomerDataItem? = null
    var adapter: CustomerAttendanceListAdapter? = null
    private val list: MutableList<CustomerAttendanceDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_employee_training, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (!session.roleData.data?.attendance?.isView.toString().equals("1")) {

            txtNoRight.visible()
            crd_data.invisible()

        } else {
            txtNoRight.invisible()
            crd_data.visible()



            recyclerView.setLoadMoreListener(object : LoadMoreListener {
                override fun onLoadMore() {
                    if (hasNextPage && !recyclerView.isLoading) {
                        progressbar.visible()
                        getAttendanceList(++page)
                    }
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                page = 1
                list.clear()
                hasNextPage = true
                recyclerView.isLoading = true
                adapter?.notifyDataSetChanged()
                getAttendanceList(page)
            }
        }
    }


    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = CustomerAttendanceListAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getAttendanceList(page)
        super.onResume()
    }

    override fun onItemSelect(position: Int, data: CustomerAttendanceDataItem) {
        //showBottomSheetDialog()
    }


    fun getAttendanceList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("SitesID", -1)
            jsonBody.put("QuotationID", -1)
            jsonBody.put("CustomerID", empItemData?.customerID.toString())
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_GETCUSTOMER_ATTEDANCE,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .getCustomerAttendanceList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CustomerAttendanceListModal>() {
                override fun onSuccess(response: CustomerAttendanceListModal) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    swipeRefreshLayout.isRefreshing = false
                    list.addAll(response.data)
                    adapter?.notifyItemRangeInserted(
                        list.size.minus(response.data.size),
                        list.size
                    )
                    hasNextPage = list.size < response.rowcount!!

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