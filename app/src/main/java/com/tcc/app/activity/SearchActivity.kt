package com.tcc.app.activity


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import com.tcc.app.R
import com.tcc.app.extention.*
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
import kotlinx.android.synthetic.main.activity_add_room_allocation.*

import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.btnSubmit
import kotlinx.android.synthetic.main.activity_search.edtEndDate
import kotlinx.android.synthetic.main.activity_search.edtStartDate
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

        txtTitle.text = getString(R.string.search)

        userTypeNameList = ArrayList()
        userTypeListArray = ArrayList()

        getBundleData()


        edtStartDate.setOnClickListener {
            showDateTimePicker(this@SearchActivity, edtStartDate)
        }


        edtEndDate.setOnClickListener {
            showNextFromStartDateTimePicker(
                this@SearchActivity,
                edtEndDate,
                edtStartDate.getValue()
            )
        }

        edtStartDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edtEndDate.setText(edtStartDate.getValue())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        edtStartDate.setText(getCurrentDate())
        edtEndDate.setText(getCurrentDate())


        btnSubmit.setOnClickListener {
            filter()
        }
    }

    fun getBundleData() {
        type = intent.getStringExtra(Constant.DATA)
        if (type.equals(Constant.LEAD) || type.equals(Constant.CUSTOMER)) {
            inEmail.visible()
            inName.visible()
            crdLeadType.visible()
        }
        if (type.equals(Constant.EMPLOYEE)) {
            inEmail.visible()
            inName.visible()
            linlayUserType.visible()

            getUserTypeList()
            userTypeViewClick()
            userTypeSpinnerListner()
        }
        if (type.equals(Constant.SITE) || type.equals(Constant.SITE_BY_TYPE)) {
            inSiteName.visible()
            inStartDate.visible()
            inEndDate.visible()
            crdSiteType.visible()
        }
        if (type.equals(Constant.TICKET)) {
            inTicket.visible()
        }
        if (type.equals(Constant.PAYMENT)) {
            inSiteName.visible()
            inStartDate.visible()
            inEndDate.visible()
            inInvoiceNum.visible()
        }


    }

    fun filter() {

        if (type.equals(Constant.LEAD)) {
            val rbType = findViewById<View>(rg.getCheckedRadioButtonId()) as? RadioButton
            LeadFragment.email = edtEmail.getValue()
            LeadFragment.name = edtName.getValue()
            LeadFragment.leadType = rbType?.text.toString()
        }
        if (type.equals(Constant.CUSTOMER)) {
            CustomerFragment.email = edtEmail.getValue()
            CustomerFragment.name = edtName.getValue()
        }
        if (type.equals(Constant.SITE)) {
            val rbType = findViewById<View>(rg1.getCheckedRadioButtonId()) as? RadioButton
            CustomerSiteFragment.name = edtSiteName.getValue()
            CustomerSiteFragment.startDate = edtStartDate.getValue()
            CustomerSiteFragment.endDate = edtEndDate.getValue()
            CustomerSiteFragment.siteType = rbType?.text.toString()
        }
        if (type.equals(Constant.SITE_BY_TYPE)) {
            val rbType = findViewById<View>(rg1.getCheckedRadioButtonId()) as? RadioButton
            SiteListMainFragment.name = edtSiteName.getValue()
            SiteListMainFragment.startDate = edtStartDate.getValue()
            SiteListMainFragment.endDate = edtEndDate.getValue()
            SiteListMainFragment.siteType = rbType?.text.toString()
        }
        if (type.equals(Constant.TICKET)) {
            TicketActivity.Ticket = edtTicketName.getValue()
        }
        if (type.equals(Constant.EMPLOYEE)) {
            EmployeeFragment.email = edtEmail.getValue()
            EmployeeFragment.name = edtName.getValue()
            EmployeeFragment.usertypeid = usertypeId
        }
        if (type.equals(Constant.PAYMENT)) {
            PaymentListFragment.startDate = edtStartDate.getValue()
            PaymentListFragment.endDate = edtEndDate.getValue()
            PaymentListFragment.invoiceNum = edtInvoiceNum.getValue()
            PaymentListFragment.siteName = edtSiteName.getValue()
        }


        finish()
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
                    userTypeNameList!!.add(getString(R.string.select_usertype))
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

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }
}