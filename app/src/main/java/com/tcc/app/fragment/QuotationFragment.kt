package com.tcc.app.fragment

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.Adapter.QuotationAdapter
import com.tcc.app.R
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.LeadItem
import com.tcc.app.modal.QuotationItem
import com.tcc.app.modal.QuotationListModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_quotation.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import org.json.JSONException
import org.json.JSONObject


class QuotationFragment : BaseFragment(), QuotationAdapter.OnItemSelected {

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
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getQuotationList(page, status)

        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getQuotationList(page, status)
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
            refershList(status)
        }
        txtRejected.setOnClickListener {
            txtAccepted.isSelected = false
            txtRejected.isSelected = true
            txtOther.isSelected = false
            status = "Rejecte"
            refershList(status)
        }
        txtOther.setOnClickListener {
            txtAccepted.isSelected = false
            txtRejected.isSelected = false
            txtOther.isSelected = true
            status = "Pending"
            refershList(status)
        }

    }

    fun refershList(status: String) {
        swipeRefreshLayout.isRefreshing = true
        page = 1
        list.clear()
        hasNextPage = true
        recyclerView.isLoading = true
        adapter?.notifyDataSetChanged()
        getQuotationList(page, status)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                //   goToActivity<AddEmployeeActivity>()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            leadItem = bundle.getSerializable(Constant.DATA) as LeadItem
        }
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = QuotationAdapter(requireContext(), list, status, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: String) {

    }

    fun getQuotationList(page: Int, status: String) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("VisitorID", leadItem?.visitorID.toString())
            jsonBody.put("CustomerID", -1)
            jsonBody.put("SitesID", -1)
            jsonBody.put("QoutationStatus", status)
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
                    hasNextPage = list.size < response.rowcount

                    refreshData(getString(R.string.no_data_found))
                }

                override fun onFailed(code: Int, message: String) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    showAlert(message)
                    refreshData(message)
                }

            }).addTo(autoDisposable)
    }

    private fun refreshData(msg: String?) {
        recyclerView.setLoadedCompleted()
        swipeRefreshLayout.isRefreshing = false
        adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            tvInfo.invisible()
            recyclerView.visible()
        } else {
            tvInfo.text = msg
            tvInfo.visible()
            recyclerView.invisible()
        }
    }

}