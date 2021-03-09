package com.tcc.app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.activity.AddQuotationActivity
import com.tcc.app.activity.DocumentListActivity
import com.tcc.app.activity.SearchActivity
import com.tcc.app.adapter.SiteListAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.setHomeScreenTitle
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.CustomerDataItem
import com.tcc.app.modal.LeadItem
import com.tcc.app.modal.SiteListItem
import com.tcc.app.modal.SiteListModal
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


class CustomerSiteFragment() : BaseFragment(), SiteListAdapter.OnItemSelected {

    var customerId: Int? = -1
    var visitorId: Int? = -1

    constructor(customerData: CustomerDataItem?) : this() {
        customerId = customerData?.customerID?.toInt()
        visitorId = customerData?.visitorID?.toInt()
    }

    var adapter: SiteListAdapter? = null
    private val list: MutableList<SiteListItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var leadItem: LeadItem? = null

    companion object {

        var name: String = ""
        fun getInstance(bundle: Bundle): CustomerSiteFragment {
            val fragment = CustomerSiteFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

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
        getBundleData()
        if (leadItem == null && customerId == -1 && visitorId == -1) {
            setHomeScreenTitle(requireActivity(), getString(R.string.nav_site))
        }

        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getSiteList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            name = ""
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getSiteList(page)
        }
    }


    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            leadItem = bundle.getSerializable(Constant.DATA) as LeadItem
            visitorId = leadItem?.visitorID?.toInt()

        }
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = SiteListAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: SiteListItem) {
        val i = Intent(requireContext(), AddQuotationActivity::class.java)
        i.putExtra(Constant.DATA, data)
        if (leadItem != null)
            i.putExtra(Constant.DATA1, leadItem)
        startActivity(i)
        Animatoo.animateCard(requireContext())
    }

    override fun onDocumentClick(position: Int, data: SiteListItem) {
        val i = Intent(requireContext(), DocumentListActivity::class.java)
        i.putExtra(Constant.DATA, data)
        startActivity(i)
        Animatoo.animateCard(requireContext())
    }

    fun getSiteList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("VisitorID", visitorId.toString())
            jsonBody.put("CustomerID", customerId)
            jsonBody.put("SiteName", name)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_SITE_LIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .getSiteList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<SiteListModal>() {
                override fun onSuccess(response: SiteListModal) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    if (response.error == 200) {
                        swipeRefreshLayout.isRefreshing = false
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        val add = menu.findItem(R.id.action_add)
        add.setVisible(false)
        val filter = menu.findItem(R.id.action_filter)
        filter.setVisible(true)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.action_filter -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.SITE)
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

    override fun onDestroy() {
        name = ""
        super.onDestroy()
    }

    override fun onDestroyView() {
        name = ""
        super.onDestroyView()
    }

    override fun onAttach(context: Context) {
        name = ""
        super.onAttach(context)
    }

    override fun onPause() {
        name = ""
        super.onPause()
    }

}