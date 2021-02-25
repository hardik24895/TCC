package com.tcc.app.activity


import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.tcc.app.R
import com.tcc.app.extention.getValue
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.fragment.*
import com.tcc.app.modal.UserTypeDataItem
import com.tcc.app.modal.UserTypeListModel
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class SearchActivity : BaseActivity() {
    var type: String? = ""

    var userTypeNameList: ArrayList<String>? = null
    var adapterUserType: ArrayAdapter<String>? = null
    var userTypeListArray: ArrayList<UserTypeDataItem>? = null
    var itemUserType: List<SearchableItem>? = null
    var usertypeId: String = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }

        txtTitle.text = getString(R.string.filter)

        userTypeNameList = ArrayList()
        userTypeListArray = ArrayList()

        getBundleData()



        btnSubmit.setOnClickListener {
            filter()
        }
    }

    fun getBundleData() {
        type = intent.getStringExtra(Constant.DATA)
        if (type.equals(Constant.LEAD) || type.equals(Constant.CUSTOMER)) {
            inEmail.visible()
            inName.visible()
        }
        if (type.equals(Constant.EMPLOYEE)) {
            inEmail.visible()
            inName.visible()
            linlayUserType.visible()

            getUserTypeList()

            userTypeViewClick()

            userTypeSpinnerListner()
        }
        if (type.equals(Constant.SITE)) {
            inSiteName.visible()
        }
        if (type.equals(Constant.TICKET)) {
            inTicket.visible()
        }
    }

    fun filter() {
        if (type.equals(Constant.LEAD)) {
            LeadFragment.email = edtEmail.getValue()
            LeadFragment.name = edtName.getValue()
            finish()
        }
        if (type.equals(Constant.CUSTOMER)) {
            CustomerFragment.email = edtEmail.getValue()
            CustomerFragment.name = edtName.getValue()
            finish()
        }
        if (type.equals(Constant.SITE)) {
            CustomerSiteFragment.name = edtSiteName.getValue()
            finish()
        }
        if (type.equals(Constant.TICKET)) {
            TicketListFragment.Ticket = edtTicketName.getValue()
            finish()
        }
        if (type.equals(Constant.EMPLOYEE)) {
            EmployeeFragment.email = edtEmail.getValue()
            EmployeeFragment.name = edtName.getValue()
            EmployeeFragment.usertypeid = usertypeId
            finish()
        }
    }

    private fun userTypeViewClick() {

        view2.setOnClickListener {
            SearchableDialog(this@SearchActivity,
                itemUserType!!,
                getString(R.string.select_usertype), { item, _ ->
                    spUserType.setSelection(item.id.toInt())
                }).show()
        }

    }

    private fun userTypeSpinnerListner() {
        spUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && userTypeListArray!!.size > position) {
                    if (position == 0) {
                        usertypeId = "-1"
                    } else {
                        usertypeId = userTypeListArray!!.get(position - 1).usertypeID.toString()
                    }
                    Logger.d("userIDq", usertypeId)

                }

            }
        }
    }

    fun getUserTypeList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", "")

            result = Networking.setParentJsonData(Constant.METHOD_USERTYPE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@SearchActivity)
            .getServices()
            .getUserTypeList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<UserTypeListModel>() {
                override fun onSuccess(response: UserTypeListModel) {
                    userTypeListArray!!.addAll(response.data)

                    var myList: MutableList<SearchableItem> = mutableListOf()
                    userTypeNameList!!.add("Select User Type")
                    for (items in response.data.indices) {
                        userTypeNameList!!.add(response.data.get(items).usertype.toString())
                        myList.add(SearchableItem(items.toLong(), userTypeNameList!!.get(items)))
                    }
                    itemUserType = myList

                    adapterUserType = ArrayAdapter(
                        this@SearchActivity,
                        R.layout.custom_spinner_item,
                        userTypeNameList!!
                    )
                    spUserType.setAdapter(adapterUserType)


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }
}