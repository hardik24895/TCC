package com.tcc.app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.rm.enterprise.modal.PaymentListDataItem
import com.rm.enterprise.modal.PaymentListModel
import com.tcc.app.R
import com.tcc.app.activity.SearchActivity
import com.tcc.app.adapter.PaymentListAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.setHomeScreenTitle
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.CustomerDataItem
import com.tcc.app.modal.LeadItem

import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_invoice.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import org.json.JSONException
import org.json.JSONObject

class PaymentListFragment() : BaseFragment(), PaymentListAdapter.OnItemSelected {
    var customerId: Int? = -1
    var visitorId: Int? = -1

    constructor(customerData: CustomerDataItem?) : this() {
        customerId = customerData?.customerID?.toInt()
        visitorId = customerData?.visitorID?.toInt()
    }

    var adapter: PaymentListAdapter? = null
    private val list: MutableList<PaymentListDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var leadItem: LeadItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_employee_training, container, false)
        return root
    }


    companion object {
        var startDate: String = ""
        var endDate: String = ""
        var invoiceNum: String = ""
        var siteName: String = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (customerId == -1)
            setHomeScreenTitle(requireActivity(), getString(R.string.nav_payment))

        if (!session.roleData.data?.payment?.isView.toString().equals("1")) {

            txtNoRight.visible()
            crd_data.invisible()

        } else {
            txtNoRight.invisible()
            crd_data.visible()
            recyclerView.setLoadMoreListener(object : LoadMoreListener {
                override fun onLoadMore() {
                    if (hasNextPage && !recyclerView.isLoading) {
                        progressbar.visible()
                        getSiteList(++page)
                    }
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                startDate = ""
                endDate = ""
                invoiceNum = ""
                siteName = ""
                page = 1
                list.clear()
                hasNextPage = true
                recyclerView.isLoading = true
                adapter?.notifyDataSetChanged()
                getSiteList(page)
            }
        }
    }


    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = PaymentListAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        val filter = menu.findItem(R.id.action_filter)
        filter.setVisible(true)

        val add = menu.findItem(R.id.action_add)
        add.setVisible(false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_filter -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.PAYMENT)
                startActivity(intent)
                Animatoo.animateCard(context)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onItemSelect(position: Int, data: PaymentListDataItem) {

//        val i = Intent(requireContext(), AddQuotationActivity::class.java)
//        i.putExtra(Constant.DATA, data)
//        if (leadItem != null)
//            i.putExtra(Constant.DATA1, leadItem)
//        startActivity(i)
//        Animatoo.animateCard(requireContext())

    }

    fun getSiteList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("InvoiceID", -1)
            jsonBody.put("CustomerID", customerId)
            jsonBody.put("StartDate", formatDateFromString(startDate))
            jsonBody.put("EndDate", formatDateFromString(endDate))
            jsonBody.put("InvoiceNumber", invoiceNum)
            jsonBody.put("SiteName", siteName)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_PAYMENT_LIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .PaymetList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<PaymentListModel>() {
                override fun onSuccess(response: PaymentListModel) {
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

    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getSiteList(page)
        super.onResume()
    }

    override fun onDestroyView() {
        startDate = ""
        endDate = ""
        invoiceNum = ""
        siteName = ""
        super.onDestroyView()
    }

    override fun onDestroy() {
        startDate = ""
        endDate = ""
        invoiceNum = ""
        siteName = ""
        super.onDestroy()
    }

    override fun onPause() {
        startDate = ""
        endDate = ""
        invoiceNum = ""
        siteName = ""
        super.onPause()
    }

    override fun onAttach(context: Context) {
        startDate = ""
        endDate = ""
        invoiceNum = ""
        siteName = ""
        super.onAttach(context)
    }

}