package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.activity.CustomerDetailActivity
import com.tcc.app.activity.SearchActivity
import com.tcc.app.adapter.CustomerListAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.setHomeScreenTitle
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.CustomerDataItem
import com.tcc.app.modal.CustomerListModal
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


class CustomerFragment : BaseFragment(), CustomerListAdapter.OnItemSelected {

    var adapter: CustomerListAdapter? = null
    var list: MutableList<CustomerDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true

    companion object {
        var email: String = ""
        var name: String = ""
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
        //  swipeRefreshLayout.isRefreshing = true
        setHomeScreenTitle(requireActivity(), getString(R.string.customer))



        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getCustomerList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            email = ""
            name = ""
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getCustomerList(page)
        }

    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = CustomerListAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: CustomerDataItem) {
        val intent = Intent(context, CustomerDetailActivity::class.java)
        intent.putExtra(Constant.DATA, data)
        startActivity(intent)
        Animatoo.animateCard(context)

    }

    fun getCustomerList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("Name", name)
            jsonBody.put("EmailID", email)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_CUSTOMER_LIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .getCustomerList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CustomerListModal>() {
                override fun onSuccess(response: CustomerListModal) {
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

    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getCustomerList(page)
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        val filter = menu.findItem(R.id.action_filter)
        filter.setVisible(true)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.CUSTOMER)
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

    override fun onDestroyView() {
        email = ""
        name = ""
        super.onDestroyView()
    }

    override fun onDestroy() {
        email = ""
        name = ""
        super.onDestroy()
    }

}