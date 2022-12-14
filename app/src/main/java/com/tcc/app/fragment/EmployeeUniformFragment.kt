package com.tcc.app.fragment

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.UniformAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.modal.UniformDataItem
import com.tcc.app.modal.UniformListModel
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_employee_training.*
import org.json.JSONException
import org.json.JSONObject


class EmployeeUniformFragment() : BaseFragment(), UniformAdapter.OnItemSelected {
    constructor(empData: EmployeeDataItem?) : this() {
        this.empItemData = empData
    }

    var adapter: UniformAdapter? = null
    private val list: MutableList<UniformDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var empItemData: EmployeeDataItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_employee_training, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!session.roleData.data?.uniform?.isView.toString().equals("1")) {

            txtNoRight.visible()
            crd_data.invisible()

        } else {
            txtNoRight.invisible()
            crd_data.visible()

            recyclerView.setLoadMoreListener(object : LoadMoreListener {
                override fun onLoadMore() {
                    if (hasNextPage && !recyclerView.isLoading) {
                        progressbar.visible()
                        getUniformList(++page)
                    }
                }
            })

            swipeRefreshLayout.setOnRefreshListener {
                page = 1
                list.clear()
                hasNextPage = true
                recyclerView.isLoading = true
                adapter?.notifyDataSetChanged()
                getUniformList(page)
            }
        }

    }

    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getUniformList(page)

        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                // goToActivity<AddEmployeeActivity>()

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
        adapter = UniformAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: UniformDataItem) {

    }

    fun getUniformList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("EmployeeID", empItemData?.userID)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(Constant.METHOD_UNIFORM_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .getUniformList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<UniformListModel>() {
                override fun onSuccess(response: UniformListModel) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    swipeRefreshLayout.isRefreshing = false

                    if (response.error == 200) {

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