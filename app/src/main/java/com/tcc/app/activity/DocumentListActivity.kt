package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.adapter.DocumentListAdapter
import com.tcc.app.dialog.LogoutDailog
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.showSnackBar
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.modal.DocumentListDataItem
import com.tcc.app.modal.DocumentListModal
import com.tcc.app.modal.SiteListItem
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_document_list.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject


class DocumentListActivity : BaseActivity(), DocumentListAdapter.OnItemSelected {
    var adapter: DocumentListAdapter? = null
    private val list: MutableList<DocumentListDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var siteListItem: SiteListItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_document_list)
        txtTitle.text = "Document"
        imgAdd.visible()
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }

        imgAdd.setOnClickListener {
            val i = Intent(this, AddUploadDocument::class.java)
            i.putExtra(Constant.DATA, siteListItem)
            startActivity(i)
            Animatoo.animateCard(this)
        }

        if (intent.hasExtra(Constant.DATA)) {
            siteListItem = intent.getSerializableExtra(Constant.DATA) as SiteListItem
        }

        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getDocumentList(page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getDocumentList(page)
        }
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = DocumentListAdapter(this, list, this)
        recyclerView.adapter = adapter

    }


    fun getDocumentList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("SitesID", siteListItem?.sitesID)
            jsonBody.put("CustomerID", siteListItem?.customerID)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_GET_DOCUMENT_LIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(this)
            .getServices()
            .getDocumentList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<DocumentListModal>() {
                override fun onSuccess(response: DocumentListModal) {
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
        getDocumentList(page)
        super.onResume()
    }

    override fun onItemSelect(position: Int, data: DocumentListDataItem) {


    }

    override fun editDocument(position: Int, data: DocumentListDataItem) {
        val i = Intent(this, EditUploadDocument::class.java)
        i.putExtra(Constant.DATA, data)
        startActivity(i)
        Animatoo.animateCard(this)
    }

    override fun deleteDocument(position: Int, data: DocumentListDataItem) {
        val dialog = LogoutDailog.newInstance(this,
            object : LogoutDailog.onItemClick {
                override fun onItemCLicked() {
                    removeDocument(data.customerSitesDocumentID.toString())
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, this.getString(R.string.alert))
        bundle.putString(Constant.TEXT, this.getString(R.string.msg_delete))
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "YesNO")
    }

    fun removeDocument(docId: String) {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("CustomerSitesDocumentID", docId)

            result = Networking.setParentJsonData(
                Constant.METHOD_REMOVE_CUSTOMER_SITE_DOCUMENT,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(this)
            .getServices()
            .deleteDocument(Networking.wrapParams(result))
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        page = 1
                        list.clear()
                        hasNextPage = true
                        recyclerView.isLoading = true
                        adapter?.notifyDataSetChanged()
                        getDocumentList(page)
                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    Logger.d("data", message.toString())
                }

            })


    }

}