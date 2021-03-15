package com.tcc.app.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.NotificationAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject


class NotificationActivity : BaseActivity(), NotificationAdapter.OnItemSelected {

    private var adapter: NotificationAdapter? = null
    lateinit var notificationArray: ArrayList<String>
    private val list: MutableList<NotificationDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var quotationItem: QuotationItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        txtTitle.setText(getString(R.string.notification))

        imgBack.setOnClickListener {
            finish()
        }
        notificationArray = ArrayList()

        imgBack.setOnClickListener {
            finish()
        }



        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getNotificaionList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getNotificaionList(page)
        }

    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this@NotificationActivity)
        recyclerView.layoutManager = layoutManager
        adapter = NotificationAdapter(this@NotificationActivity, list, this)
        recyclerView.adapter = adapter

    }


    override fun onItemSelect(position: Int, data: NotificationDataItem) {

    }


    fun getNotificaionList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("UserID", session.user.data?.userID)

            result = Networking.setParentJsonData(Constant.METHOD_GET_NOTIFICATION, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(this@NotificationActivity)
            .getServices()
            .getNotification(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<NotificationListModel>() {
                override fun onSuccess(response: NotificationListModel) {
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
        getNotificaionList(page)

        super.onResume()
    }

}