package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.gson.Gson
import com.tcc.app.R
import com.tcc.app.activity.AddLeadActivity
import com.tcc.app.activity.LeadDetailActivity
import com.tcc.app.activity.SearchActivity
import com.tcc.app.adapter.LeadAdapter
import com.tcc.app.dialog.AddLeadDailog
import com.tcc.app.dialog.SendMailDailog
import com.tcc.app.dialog.SendMessageDailog
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.CommonAddModal
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


class LeadFragment : BaseFragment(), LeadAdapter.OnItemSelected {

    var adapter: LeadAdapter? = null
    var add: MenuItem? = null
    var filter: MenuItem? = null
    lateinit var inflater: MenuInflater
    private val list: MutableList<LeadItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true

    companion object {
        var email: String = ""
        var name: String = ""
        var leadType: String = ""
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
        setHomeScreenTitle(requireActivity(), getString(R.string.nav_visitor))
        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getLeadList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            email = ""
            name = ""
            leadType = ""
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

    override fun onItemSelect(position: Int, data: LeadItem, action: String) {

        if (action.equals("MainView")) {
            val intent = Intent(context, LeadDetailActivity::class.java)
            intent.putExtra(Constant.DATA, data)
            startActivity(intent)
            Animatoo.animateCard(context)
        } else if (action.equals("SMS")) {
            sendMeassageDialog(data.mobileNo.toString())
        } else if (action.equals("Email")) {
            sendMailDialog(data.name.toString(), data.emailID.toString())
        } else if (action.equals("Edit")) {

            if (checkUserRole(
                    session.roleData.data?.visitor?.isInsert.toString(),
                    requireContext()
                )
            ) {
                val intent = Intent(context, AddLeadActivity::class.java)

                intent.putExtra("Edit", true)
                intent.putExtra(Constant.DATA, data)
                startActivity(intent)
                Animatoo.animateCard(context)

            }


//            if (session.roleData.data?.visitor?.toString().equals("1")) {
//                val intent = Intent(context, AddLeadActivity::class.java)
//                intent.putExtra(Constant.DATA, data)
//                startActivity(intent)
//                Animatoo.animateCard(context)
//            } else {
//                swipeRefreshLayout.showSnackBar(getString(R.string.you_dont_have_a_rights))
//            }
        }

    }


    private fun sendMailDialog(Lead: String, EMail: String) {
        SendMailDailog(requireContext())

        val dialog = SendMailDailog.newInstance(
            requireContext(),
            Lead,
            EMail,
            object : SendMailDailog.onItemClick {
                override fun onItemCLicked(Subject: String, Description: String) {
                    SendEmail(EMail, Subject, Description)
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))

        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }

    private fun sendMeassageDialog(contact: String) {
        SendMessageDailog(requireContext())

        val dialog = SendMessageDailog.newInstance(
            requireContext(),
            object : SendMessageDailog.onItemClick {
                override fun onItemCLicked(Message: String) {
                    SendMessage(contact, Message)
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))

        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home, menu)

        filter = menu.findItem(R.id.action_filter)
        filter?.setVisible(true)

        add = menu.findItem(R.id.action_add)
        add?.setVisible(true)

    }

    override fun onDestroyOptionsMenu() {

        add?.setVisible(false)
        filter?.setVisible(false)

        super.onDestroyOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                if (checkUserRole(
                        session.roleData.data?.visitor?.isEdit.toString(),
                        requireContext()
                    )
                )
                    showDialog()
                return true
            }
            R.id.action_filter -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.LEAD)
                startActivity(intent)
                Animatoo.animateCard(context)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getLeadList(page)
        super.onResume()

    }

    fun showDialog() {
        val dialog = AddLeadDailog.newInstance(requireContext(),
            object : AddLeadDailog.onItemClick {
                override fun onItemCLicked(mobile: String, serviceId: String) {
                    checkLead(mobile, serviceId)
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
            jsonBody.put("Name", name)
            jsonBody.put("LeadType", leadType)
            jsonBody.put("EmailID", email)
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


    fun checkLead(mobile: String, serviceId: String) {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", -1)
            jsonBody.put("CurrentPage", 1)
            jsonBody.put("Name", "")
            jsonBody.put("EmailID", mobile)
            jsonBody.put("LeadType", "")
            jsonBody.put("ServiceID", serviceId)
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
            .getLeadList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<LeadListModal>() {
                override fun onSuccess(response: LeadListModal) {
                    hideProgressbar()
                    val gson = Gson()
                    val jsonCars = gson.toJson(response.sites)
                    if (response.data.size > 0) {
                        val intent = Intent(context, AddLeadActivity::class.java)
                        intent.putExtra(Constant.DATA, response.data.get(0))
                        intent.putExtra(Constant.DATA_SITE, jsonCars)
                        intent.putExtra(Constant.MOBILE, mobile)
                        intent.putExtra(Constant.SERVICE_ID, serviceId)
                        startActivity(intent)
                        Animatoo.animateCard(context)
                    } else {
                        val intent = Intent(context, AddLeadActivity::class.java)
//
                        intent.putExtra(Constant.SERVICE_ID, serviceId)
                        intent.putExtra(Constant.MOBILE, mobile)
                        startActivity(intent)
                        Animatoo.animateCard(context)
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
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

    override fun onDestroyView() {
        email = ""
        name = ""
        leadType = ""
        super.onDestroyView()
    }

    override fun onDestroy() {
        email = ""
        name = ""
        leadType = ""
        super.onDestroy()
    }

    fun SendEmail(email: String, subject: String, Description: String) {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()

            jsonBody.put("EmailID", email)
            jsonBody.put("Subject", subject)
            jsonBody.put("Message", Description)
            result = Networking.setParentJsonData(Constant.METHOD_SEND_MAIL, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .SendMail(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        swipeRefreshLayout.showSnackBar(response.message.toString())
                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }

    fun SendMessage(Contact: String, Message: String) {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()

            jsonBody.put("MobileNo", Contact)
            jsonBody.put("Message", Message)

            result = Networking.setParentJsonData(Constant.METHOD_SEND_MESSAGE, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .SendMessage(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        swipeRefreshLayout.showSnackBar(response.message.toString())
                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }

}