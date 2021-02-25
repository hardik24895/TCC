package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.activity.AddSalaryActivity
import com.tcc.app.activity.SalaryDetailActivity
import com.tcc.app.adapter.SalaryAdapter
import com.tcc.app.dialog.AddAdavanceDailog
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.modal.SalaryDataItem
import com.tcc.app.modal.SalaryListModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_employee_salary.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import org.json.JSONException
import org.json.JSONObject


class EmployeeSalaryFragment() : BaseFragment(), SalaryAdapter.OnItemSelected {
    constructor(empData: EmployeeDataItem?) : this() {
        this.empItemData = empData
    }

    var empItemData: EmployeeDataItem? = null
    var adapter: SalaryAdapter? = null
    private val list: MutableList<SalaryDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_employee_salary, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtAdavance.setOnClickListener { showAdavanceDialog() }
        txtSalary.setOnClickListener {

            val intent = Intent(requireContext(), AddSalaryActivity::class.java)
            intent.putExtra(Constant.DATA, empItemData)
            startActivity(intent)

        }
        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getSalaryList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getSalaryList(page)
        }

    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = SalaryAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getSalaryList(page)
    }

    override fun onItemSelect(position: Int, data: SalaryDataItem) {
        val intent = Intent(context, SalaryDetailActivity::class.java)
        intent.putExtra(Constant.DATA, data)
        startActivity(intent)
        Animatoo.animateCard(context)

    }

    fun getSalaryList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("UserID", empItemData?.userID)
            result = Networking.setParentJsonData(
                Constant.METHOD_GET_SLARYY,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(requireContext())
            .getServices()
            .getSalaryList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<SalaryListModal>() {
                override fun onSuccess(response: SalaryListModal) {
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

    fun showAdavanceDialog() {
        val dialog = AddAdavanceDailog.newInstance(requireContext(),
            object : AddAdavanceDailog.onItemClick {
                override fun onItemCLicked() {
                    // goToActivity<AddVisitorActivity>()
                }

            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))
        bundle.putString(
            Constant.DATA,
            empItemData?.userID.toString()
        )
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }
}