package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.adapter.TicketAdapter
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.TicketDataItem
import com.tcc.app.modal.TicketListMdal
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


class TicketActivity : BaseActivity(), TicketAdapter.OnItemSelected {


    var adapter: TicketAdapter? = null
    private val list: MutableList<TicketDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var filter: MenuItem? = null


    companion object {
        var Ticket: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_ticket_list)
        txtTitle.setText(getString(R.string.ticket))

        if (checkUserRole(session.roleData.data?.tickets?.isInsert.toString(), this)) {
            imgAdd.visible()
        }
        imgFilter.visible()
        imgBack.setOnClickListener {
            finish()
        }

        imgAdd.setOnClickListener {
            goToActivity<AddTicketActivity>()

        }

        imgFilter.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra(Constant.DATA, Constant.TICKET)
            startActivity(intent)
            Animatoo.animateCard(this)
        }
        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getTicketList(page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            Ticket = ""
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getTicketList(page)
        }
    }


    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = TicketAdapter(this, list, this)
        recyclerView.adapter = adapter

    }


    fun getTicketList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Name", Ticket)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_GET_TICKET,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(this)
            .getServices()
            .getTicketList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<TicketListMdal>() {
                override fun onSuccess(response: TicketListMdal) {
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
        getTicketList(page)
        super.onResume()
    }

    override fun onItemSelect(position: Int, data: TicketDataItem) {

    }

    override fun onDestroy() {
        Ticket = ""
        // setHasOptionsMenu(false);
        //filter?.setVisible(false)
        super.onDestroy()
    }


    override fun onPause() {
        Ticket = ""
        super.onPause()
    }

}