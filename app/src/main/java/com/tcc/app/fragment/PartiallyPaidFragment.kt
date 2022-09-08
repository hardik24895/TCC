package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.activity.AddPaymentActivity
import com.tcc.app.adapter.InvoicePaidAdapter
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.InvoiceDataItem
import com.tcc.app.modal.InvoiceListModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import org.json.JSONException
import org.json.JSONObject

class PartiallyPaidFragment : BaseFragment(), InvoicePaidAdapter.OnItemSelected {

    var adapter: InvoicePaidAdapter? = null
    private val list: MutableList<InvoiceDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.reclerview_swipelayout, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getInvoiceList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getInvoiceList(page)
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
        getInvoiceList(page)
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = InvoicePaidAdapter(requireContext(), list, false, "PartiallyPaid", this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: InvoiceDataItem) {

        if (checkUserRole(session.roleData.data?.payment?.isInsert.toString(), requireContext())) {
            val intent = Intent(context, AddPaymentActivity::class.java)
            intent.putExtra(Constant.DATA, data)
            startActivity(intent)
            Animatoo.animateCard(context)
        }

    }

    override fun openSelectedPDF(position: Int, data: InvoiceDataItem) {
        if (data.document.equals("")) {
            swipeRefreshLayout.showSnackBar("File not found")
        } else
            openPDF(Constant.PDF_INVOICE_URL + data.document, requireContext())
    }

    fun getInvoiceList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("QuotationID", "-1")
            jsonBody.put("CustomerID", "-1")
            jsonBody.put("SitesID", "-1")
            jsonBody.put("FilterStatus", "PartialPaid")
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_GET_INVOICE,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .getInvoiceList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<InvoiceListModal>() {
                override fun onSuccess(response: InvoiceListModal) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    swipeRefreshLayout.isRefreshing = false
                    list.addAll(response.data)
                    adapter?.notifyItemRangeInserted(
                        list.size.minus(response.data.size),
                        list.size
                    )
                    hasNextPage = list.size < response.rowcount

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