package com.tcc.app.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.activity.AddEmployeeActivity
import com.tcc.app.activity.EmployeeDetailActivity
import com.tcc.app.activity.SearchActivity
import com.tcc.app.adapter.EmployeeAdapter
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.modal.EmployeeListModel
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


class EmployeeFragment : BaseFragment(), EmployeeAdapter.OnItemSelected {

    var adapter: EmployeeAdapter? = null
    private val list: MutableList<EmployeeDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true

    companion object {
        var email: String = ""
        var name: String = ""
        var usertypeid = "-1"
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
        setHomeScreenTitle(requireActivity(), getString(R.string.nav_employee))
        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getEmployeeList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            email = ""
            name = ""
            usertypeid = "-1"
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getEmployeeList(page)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        val filter = menu.findItem(R.id.action_filter)
        filter.setVisible(true)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                goToActivity<AddEmployeeActivity>()

                return true
            }
            R.id.action_filter -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.EMPLOYEE)
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


    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = EmployeeAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: EmployeeDataItem) {
        val intent = Intent(context, EmployeeDetailActivity::class.java)
        intent.putExtra(Constant.DATA, data)
        startActivity(intent)
        Animatoo.animateCard(context)
    }


    fun getEmployeeList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("Name", name)
            jsonBody.put("EmailID", email)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            jsonBody.put("UsertypeID", usertypeid)


            result = Networking.setParentJsonData(Constant.METHOD_EMPLOYEE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .getEmployeeList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<EmployeeListModel>() {
                override fun onSuccess(response: EmployeeListModel) {
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
        getEmployeeList(page)
        super.onResume()
    }

    override fun onDestroyView() {
        email = ""
        name = ""
        usertypeid = "-1"
        super.onDestroyView()
    }

    override fun onDestroy() {
        email = ""
        name = ""
        usertypeid = "-1"
        super.onDestroy()
    }

    override fun onPause() {
        email = ""
        name = ""
        usertypeid = "-1"
        super.onPause()
    }

    override fun onAttach(context: Context) {
        email = ""
        name = ""
        usertypeid = "-1"
        super.onAttach(context)
    }

}