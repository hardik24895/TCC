package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.activity.AddLeadActivity
import com.tcc.app.activity.LeadDetailActivity
import com.tcc.app.adapter.LeadAdapter
import com.tcc.app.adapter.LeadFollowUpAdapter
import com.tcc.app.dialog.AddVisitorDailog
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.LeadFollowUpDataItem
import com.tcc.app.modal.LeadFollowUpListModal
import com.tcc.app.modal.LeadItem
import com.tcc.app.modal.LeadListModal
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


class LeadFollowupFragment : BaseFragment(), LeadFollowUpAdapter.OnItemSelected {

    var adapter: LeadFollowUpAdapter? = null

    private val list: MutableList<LeadFollowUpDataItem> = mutableListOf()
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
        setHomeScreenTitle(requireActivity(), getString(R.string.nav_visitor))
        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getLeadFollowupList(page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getLeadFollowupList(page)
        }
    }

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = LeadFollowUpAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: LeadFollowUpDataItem) {
        

    }

    
    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getLeadFollowupList(page)
        super.onResume()

    }

  

    fun getLeadFollowupList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("Name", "")
            jsonBody.put("EmailID", "")
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_LEADLIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .getLeadFollowupList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<LeadFollowUpListModal>() {
                override fun onSuccess(response: LeadFollowUpListModal) {
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