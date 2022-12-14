package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.adapter.TeamDefinitionListAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.QuotationItem
import com.tcc.app.modal.TeamDefinitionDataItem
import com.tcc.app.modal.TeamDefinitionListModel
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject


class TeamDefinitionListActivity : BaseActivity(), TeamDefinitionListAdapter.OnItemSelected {


    private var adapter: TeamDefinitionListAdapter? = null
    private val list: MutableList<TeamDefinitionDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var quotationItem: QuotationItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_definition_list)

        txtTitle.setText(getString(R.string.team_definitions))

        if (intent.hasExtra(Constant.DATA)) {
            quotationItem = intent.getSerializableExtra(Constant.DATA) as QuotationItem

        }


        imgAdd.visible()
        imgBack.setOnClickListener {
            finish()
        }

        imgAdd.setOnClickListener {
            val i = Intent(this@TeamDefinitionListActivity, AddTeamDefinitionActivity::class.java)
            i.putExtra(Constant.DATA, quotationItem)
            startActivity(i)
            Animatoo.animateCard(this)
        }


        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getTeamDefinition(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getTeamDefinition(page)
        }


    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this@TeamDefinitionListActivity)
        recyclerView.layoutManager = layoutManager
        adapter = TeamDefinitionListAdapter(this@TeamDefinitionListActivity, list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: TeamDefinitionDataItem) {

    }

    fun getTeamDefinition(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("SitesID", quotationItem?.sitesID)
            jsonBody.put("QuotationID", quotationItem?.quotationID)
            jsonBody.put("CustomerID", quotationItem?.customerID)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))

            result = Networking.setParentJsonData(
                Constant.METHOD_TEAM_DEFINITION_LIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(this@TeamDefinitionListActivity)
            .getServices()
            .getTeamDefinationList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<TeamDefinitionListModel>() {
                override fun onSuccess(response: TeamDefinitionListModel) {
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
        getTeamDefinition(page)

        super.onResume()
    }

}