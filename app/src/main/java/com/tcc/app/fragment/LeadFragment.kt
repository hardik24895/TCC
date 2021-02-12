package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.Adapter.LeadAdapter
import com.tcc.app.R
import com.tcc.app.activity.AddVisitorActivity
import com.tcc.app.activity.LeadDetailActivity
import com.tcc.app.dialog.AddVisitorDailog
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.LeadItem
import com.tcc.app.modal.LeadListModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import org.json.JSONException
import org.json.JSONObject


class LeadFragment : BaseFragment(), LeadAdapter.OnItemSelected {

    var adapter: LeadAdapter? = null

    private val list: MutableList<LeadItem> = mutableListOf()
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
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getLeadList(page)

        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getLeadList(page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getLeadList(page)
        }
    }

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = LeadAdapter(requireContext(), list, session, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: LeadItem) {
        val intent = Intent(context, LeadDetailActivity::class.java)
        intent.putExtra(Constant.DATA, data)
        startActivity(intent)
        Animatoo.animateCard(context)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                showDialog()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun showDialog() {
        val dialog = AddVisitorDailog.newInstance(requireContext(),
                object : AddVisitorDailog.onItemClick {
                    override fun onItemCLicked() {
                        goToActivity<AddVisitorActivity>()
                    }
                })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))
//        bundle.putString(
//            Constant.TEXT,
//            getString(R.string.msg_get_data_from_server)
//        )
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }

    fun getLeadList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("Name", "")
            jsonBody.put("EmailID", "")
            jsonBody.put("CityID", -1)
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
                .getLeadList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackObserver<LeadListModal>() {
                    override fun onSuccess(response: LeadListModal) {
                        if (list.size > 0) {
                            progressbar.invisible()
                        }
                        swipeRefreshLayout.isRefreshing = false
                        list.addAll(response.data)
                        adapter?.notifyItemRangeInserted(
                                list.size.minus(response.data.size),
                                list.size
                        )
                        hasNextPage = list.size < response.rowcount

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