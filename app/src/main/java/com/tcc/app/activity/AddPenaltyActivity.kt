package com.tcc.app.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.ReasonListAdapter
import com.tcc.app.extention.*
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_penalty.*
import kotlinx.android.synthetic.main.row_penalty_employee.view.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import tech.hibk.searchablespinnerlibrary.SearchableSpinner

class AddPenaltyActivity : BaseActivity(), ReasonListAdapter.OnItemSelected {


    var siteNameList: ArrayList<String>? = ArrayList()
    var adapterSite: ArrayAdapter<String>? = null
    var siteListArray: ArrayList<SiteListItem>? = ArrayList()
    var itemSite: List<SearchableItem>? = null
    var siteId: String = ""

    var userTypeNameList: ArrayList<String> = ArrayList()
    var adapterUserType: ArrayAdapter<String>? = null
    var userTypeListArray: ArrayList<EmployeeDataItem> = ArrayList()
    var itemUserType: List<SearchableItem>? = null
    var usertypeId: String = ""

    var ReasonListArray: ArrayList<RejectReasonDataItem> = ArrayList()
    var adapter: ReasonListAdapter? = null
    var selectedReason: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_penalty)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Penalty"


        setupRecyclerView()
        getEmployeeList()
        btnAddEmployee.setOnClickListener { onAddField() }
        getSiteList()
        siteSpinnerListner()
        siteViewClick()
        getReasonList()

        btnSubmit.setOnClickListener {
            AddPanelty()
        }

    }


    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        recReason.layoutManager = layoutManager
        adapter = ReasonListAdapter(this, ReasonListArray, this)
        recReason.adapter = adapter

    }

    fun onAddField() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.row_penalty_employee, null, false)
        var btnRemove: TextView = rowView.findViewById(R.id.btnRemoveUser)
        var spEmployee: SearchableSpinner = rowView.findViewById(R.id.spEmployee)
        var viewChild: View = rowView.findViewById(R.id.view)
        var edtAmount: EditText = rowView.findViewById(R.id.edtAmount)

        btnRemove.setOnClickListener {
            lin_add_user.removeView(rowView)
        }

        adapterUserType = ArrayAdapter(
            this@AddPenaltyActivity,
            R.layout.custom_spinner_item,
            userTypeNameList!!
        )
        spEmployee.setAdapter(adapterUserType)


        spEmployee.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && userTypeListArray!!.size > position) {


                }

            }
        }



        viewChild.setOnClickListener {
            SearchableDialog(
                this@AddPenaltyActivity,
                itemUserType!!,
                getString(R.string.select_employee),
                { item, _ -> spEmployee.setSelection(item.id.toInt()) }).show()
        }

        lin_add_user!!.addView(rowView)
    }

    private fun siteViewClick() {
        view.setOnClickListener {
            SearchableDialog(this@AddPenaltyActivity,
                itemSite!!,
                getString(R.string.select_site), { item, _ ->
                    spSite.setSelection(item.id.toInt())
                }).show()
        }
    }

    private fun siteSpinnerListner() {
        spSite.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && siteListArray!!.size > position) {
                    siteId = siteListArray!!.get(position).sitesID.toString()


                }

            }
        }
    }

    fun getSiteList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", -1)
            jsonBody.put("CurrentPage", 1)
            jsonBody.put("VisitorID", -1)
            jsonBody.put("CustomerID", -1)
            jsonBody.put("SiteName", "")
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_SITE_LIST,
                jsonBody
            )


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddPenaltyActivity)
            .getServices()
            .getSiteList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<SiteListModal>() {
                override fun onSuccess(response: SiteListModal) {
                    siteListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    for (items in response.data.indices) {
                        siteNameList!!.add(response.data.get(items).siteName.toString())
                        myList.add(SearchableItem(items.toLong(), siteNameList!!.get(items)))

                    }
                    itemSite = myList

                    adapterSite = ArrayAdapter(
                        this@AddPenaltyActivity,
                        R.layout.custom_spinner_item,
                        siteNameList!!
                    )
                    spSite.setAdapter(adapterSite)


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)

    }

    fun getEmployeeList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", -1)
            jsonBody.put("CurrentPage", 1)
            jsonBody.put("Name", "")
            jsonBody.put("EmailID", "")
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            jsonBody.put("UsertypeID", "-1")


            result = Networking.setParentJsonData(Constant.METHOD_EMPLOYEE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddPenaltyActivity)
            .getServices()
            .getEmployeeList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<EmployeeListModel>() {
                override fun onSuccess(response: EmployeeListModel) {

                    if (response.error == 200) {
                        userTypeListArray!!.addAll(response.data)
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        for (items in response.data.indices) {
                            userTypeNameList!!.add(
                                "${response.data.get(items).firstName.toString()} ${
                                    response.data.get(
                                        items
                                    ).lastName.toString()
                                }"
                            )
                            myList.add(
                                SearchableItem(
                                    items.toLong(),
                                    userTypeNameList!!.get(items)
                                )
                            )

                        }
                        itemUserType = myList

                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                }

            }).addTo(autoDisposable)
    }

    fun AddPanelty() {
        var result = ""
        try {
            val jsonArray = JSONArray()

            if (lin_add_user.childCount > 0) {
                for (item in 0 until lin_add_user.childCount) {
                    val jsonObj1 = JSONObject()
                    jsonObj1.put(
                        "EmployeeID",
                        userTypeListArray.get(lin_add_user.getChildAt(item).spEmployee.selectedItemPosition).userID.toString()
                    )
                    jsonObj1.put("Penalty", lin_add_user.getChildAt(item).edtAmount.getValue())
                    jsonArray.put(jsonObj1)
                }
            }

            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Reason", selectedReason)
            jsonBody.put("SitesID", siteId)
            jsonBody.put("PenaltyDate", formatDateFromString(getCurrentDate()))
            jsonBody.put("Item", jsonArray)




            result = Networking.setParentJsonData(Constant.METHOD_ADD_PANELTLY, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddPenaltyActivity)
            .getServices()
            .addPanelty(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {

                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        finish()

                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                }

            }).addTo(autoDisposable)
    }


    fun getReasonList() {
        ReasonListArray.clear()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("ReasonType", "Employee")
            result = Networking.setParentJsonData(Constant.METHOD_GET_REASON, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddPenaltyActivity)
            .getServices()
            .getRejectReasonList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<RejectReasonListModel>() {
                override fun onSuccess(response: RejectReasonListModel) {

                    if (response.error == 200) {
                        ReasonListArray.addAll(response.data)
                        adapter?.notifyDataSetChanged()
                    } else {
                        showAlert(response.message.toString())
                    }


                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                }

            }).addTo(autoDisposable)
    }

    override fun onItemSelect(position: Int, data: RejectReasonDataItem, isAdd: Boolean) {
        if (isAdd) {
            if (selectedReason?.length!! == 0) {
                selectedReason = data.reason
            } else {
                selectedReason = selectedReason + "," + data.reason
            }
        } else {

            var stringArray: List<String> = selectedReason.toString().split(',');
            val arraytwo: ArrayList<String> = ArrayList()
            for (item in stringArray.indices) {
                if (!stringArray.get(item).equals(data.reason)) {
                    arraytwo.add(stringArray.get(item))
                }
            }
            selectedReason = arraytwo.joinToString(",")
        }
    }

}