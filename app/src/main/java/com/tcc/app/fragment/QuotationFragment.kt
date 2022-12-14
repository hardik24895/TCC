package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.activity.AddInvoiceActivity
import com.tcc.app.activity.SiteAttendanceListActivity
import com.tcc.app.activity.TeamDefinitionListActivity
import com.tcc.app.adapter.QuotationAdapter
import com.tcc.app.dialog.AcceptReasonDailog
import com.tcc.app.dialog.RejectReasonDailog
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_document_list.*
import kotlinx.android.synthetic.main.fragment_quotation.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import org.json.JSONException
import org.json.JSONObject


class QuotationFragment() : BaseFragment(), QuotationAdapter.OnItemSelected {

    var customerId: Int? = -1
    var visitorId: Int? = -1

    constructor(customerData: CustomerDataItem?) : this() {
        customerId = customerData?.customerID?.toInt()
        visitorId = customerData?.visitorID?.toInt()
    }


    var adapter: QuotationAdapter? = null
    private val list: MutableList<QuotationItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var status: String = "Accept"
    var leadItem: LeadItem? = null



    companion object {
        fun getInstance(bundle: Bundle): QuotationFragment {
            val fragment = QuotationFragment()
            fragment.arguments = bundle
            return fragment
        }

        var isFromQuotation: Boolean = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_quotation, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()

        if (leadItem == null && customerId == -1) {
            setHomeScreenTitle(requireActivity(), getString(R.string.nav_quotations))
        }

        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getQuotationList(++page, status)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getQuotationList(page, status)
        }

        txtAccepted.isSelected = true


        txtAccepted.setOnClickListener {
            txtAccepted.isSelected = true
            txtRejected.isSelected = false
            txtOther.isSelected = false
            status = "Accept"
            setupRecyclerView()
            refershList(status)
        }
        txtRejected.setOnClickListener {
            txtAccepted.isSelected = false
            txtRejected.isSelected = true
            txtOther.isSelected = false
            status = "Reject"
            setupRecyclerView()
            refershList(status)
        }
        txtOther.setOnClickListener {
            txtAccepted.isSelected = false
            txtRejected.isSelected = false
            txtOther.isSelected = true
            status = "Pending"
            setupRecyclerView()
            refershList(status)
        }

    }

    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getQuotationList(page, status)
        super.onResume()
    }

    fun refershList(status: String) {
        swipeRefreshLayout.isRefreshing = true
        page = 1
        list.clear()
        hasNextPage = true
        recyclerView.isLoading = true
        //  adapter?.notifyDataSetChanged()
        getQuotationList(page, status)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.home, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_add -> {
//                //   goToActivity<AddEmployeeActivity>()
//
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            leadItem = bundle.getSerializable(Constant.DATA) as LeadItem
        }
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = QuotationAdapter(requireContext(), list, status, session, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: QuotationItem, action: String) {

        if (action.equals("ACCEPT")) {
            showAcceptDialog(data, position)
        } else if (action.equals("REJECT")) {
            showRejectDialog(data, position)
        } else if (action.equals("TEAM-DEFINATION")) {
            val i = Intent(requireContext(), TeamDefinitionListActivity::class.java)
            i.putExtra(Constant.DATA, data)
            startActivity(i)
            Animatoo.animateCard(requireContext())

        } else if (action.equals("INVOICE")) {
            val i = Intent(requireContext(), AddInvoiceActivity::class.java)
            i.putExtra(Constant.DATA, data)
            if (leadItem != null)
                i.putExtra(Constant.DATA1, leadItem)
            startActivity(i)
            Animatoo.animateCard(requireContext())

        } else if (action.equals("ATTENDANCE")) {
            if (checkUserRole(
                    session.roleData.data?.attendance?.isInsert.toString(),
                    requireContext()
                )
            ) {
                val i = Intent(requireContext(), SiteAttendanceListActivity::class.java)
                i.putExtra(Constant.DATA, data)
                startActivity(i)
                Animatoo.animateCard(requireContext())
            }

        } else if (action.equals("OPENPDF")) {
            if (data.document.equals("")) {
                root.showSnackBar("File not found")
            } else
                openPDF(Constant.PDF_QUOTATION_URL + data.document, requireContext())
        }

    }

    private fun showAcceptDialog(data: QuotationItem, position: Int) {
        AcceptReasonDailog(requireContext(), data)
        var data: QuotationItem = data
        val dialog = AcceptReasonDailog.newInstance(
            requireContext(), data,
            object : AcceptReasonDailog.onItemClick {
                override fun onItemCLicked(
                    startDate: String,
                    endDate: String,
                    startTime: String,
                    endTime: String
                ) {
                    AcceptQuotation(
                        data.visitorID!!,
                        startDate,
                        endDate,
                        data.quotationID!!,
                        data.customerID!!,
                        startTime,
                        endTime,
                        position
                    )
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))

        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }

    private fun showRejectDialog(data: QuotationItem, position: Int) {
        RejectReasonDailog(requireContext())
        var data: QuotationItem = data
        val dialog = RejectReasonDailog.newInstance(requireContext(),
            object : RejectReasonDailog.onItemClick {
                override fun onItemCLicked(reasonId: String) {
                    RejectQuotation(reasonId, data.quotationID!!, position)
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))

        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }

    fun getQuotationList(page: Int, status: String) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            if (leadItem != null)
                jsonBody.put("VisitorID", leadItem?.visitorID.toString())
            else
                jsonBody.put("VisitorID", visitorId.toString())
            jsonBody.put("CustomerID", customerId)
            jsonBody.put("SitesID", -1)
            jsonBody.put("QoutationStatus", status)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_QUOTATION_LIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .getQuationList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<QuotationListModal>() {
                override fun onSuccess(response: QuotationListModal) {
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

        if (isFromQuotation) {
            txtOther.performClick()
            isFromQuotation = false
        }
    }

    fun RejectQuotation(reasonId: String, QuotationId: String, position: Int) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("ReasonID", reasonId)
            jsonBody.put("QuotationID", QuotationId)

            result = Networking.setParentJsonData(
                Constant.METHOD_REJECT_REASON,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(requireContext())
            .getServices()
            .QuationReason(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
//                        list.removeAt(position.toInt())
//                        adapter?.notifyItemRemoved(position.toInt())
//                        adapter?.notifyDataSetChanged()
                        txtRejected.performClick()
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun AcceptQuotation(
        visitorId: String,
        startDate: String,
        endDate: String,
        QuotationId: String,
        customerId: String,
        startTime: String,
        endTime: String,
        position: Int
    ) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("QuotationID", QuotationId)
            jsonBody.put("StartDate", formatDateFromString(startDate))
            jsonBody.put("EndDate", formatDateFromString(endDate))
            jsonBody.put("StartTime", startTime)
            jsonBody.put("EndTime", endTime)
            jsonBody.put("CustomerID", customerId)
            if (leadItem != null)
                jsonBody.put("VisitorID", leadItem?.visitorID.toString())
            else
                jsonBody.put("VisitorID", visitorId)

            result = Networking.setParentJsonData(
                Constant.METHOD_ACCEPT_REASON,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(requireContext())
            .getServices()
            .QuationReason(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        // list.removeAt(position.toInt())
                        // adapter?.notifyItemRemoved(position.toInt())
                        // adapter?.notifyDataSetChanged()
                        txtAccepted.performClick()
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