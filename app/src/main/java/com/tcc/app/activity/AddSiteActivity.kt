package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tcc.app.R
import com.tcc.app.adapter.SiteAddressAdapter1
import com.tcc.app.extention.*
import com.tcc.app.fragment.CustomerSiteFragment
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.GSTINValidator.validGSTIN
import com.tcc.app.utils.Logger
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp
import com.tcc.app.utils.TimeStamp.formatServerDateToLocal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_site.*
import kotlinx.android.synthetic.main.bottom_dailog_attendance.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.util.*

class AddSiteActivity : BaseActivity(), SiteAddressAdapter1.OnItemSelected {

    var stateNameList: ArrayList<String> = ArrayList()
    var adapterState: ArrayAdapter<String>? = null
    var stateIteams: List<SearchableItem>? = null
    var stateID: String = "-1"

    var cityID: String = "-1"
    var cityNameList: ArrayList<String> = ArrayList()
    var cityListArray: ArrayList<CityDataItem> = ArrayList()
    var adapterCity: ArrayAdapter<String>? = null
    var cityIteams: List<SearchableItem>? = null
    var siteType: String = ""
    var customerData: CustomerDataItem? = null

    var leadItem: LeadItem? = null


    var serviceNameList: ArrayList<String> = ArrayList()
    var adapterService: ArrayAdapter<String>? = null
    var serviceListArray: ArrayList<ServiceDataItem> = ArrayList()
    var itemService: List<SearchableItem> = ArrayList()
    var serviceId: String = ""

    var list: ArrayList<SiteListItem> = ArrayList()
    var bottomSheetBehavior: BottomSheetBehavior<CardView>? = null
    var adapter: SiteAddressAdapter1? = null
    var siteID: String = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_site)
        rbResidential.isChecked = true
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Site"
        btnSubmit.setOnClickListener { validation(false) }
        if (intent.hasExtra(Constant.DATA)) {
            customerData = intent.getSerializableExtra(Constant.DATA) as CustomerDataItem
            // edtCompanyName.setText(customerData!!.name)
        }

        if (intent.hasExtra(Constant.DATA_LEAD)) {
            leadItem = intent.getSerializableExtra(Constant.DATA_LEAD) as LeadItem
            //  edtCompanyName.setText(leadItem!!.name)

        }

        if (intent.hasExtra(Constant.CUSTOMER_NAME)) {

            //  edtCompanyName.setText(intent.getStringExtra(Constant.CUSTOMER_NAME))
        }
        setupRecyclerView()
        btnAddQuatation.setOnClickListener { validation(true) }
        getStateSppinerData()
        getCityList(stateID)
        stateSpinnerListner()
        citySpinnerListner()
        stateViewClick()
        cityViewClick()
        getServiceList()
        serviceSpinnerListner()
        serviceViewClick()
        edtSdate.setText(getCurrentDate())
        edtEdate.setText(getCurrentDate())
        edtPdate.setText(getCurrentDate())


        edtSdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtSdate) }
        edtEdate.setOnClickListener {
            showNextFromStartDateTimePicker(
                this@AddSiteActivity,
                edtEdate,
                edtSdate.getValue()
            )
        }
        edtPdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtPdate) }


        edtSdate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edtEdate.setText(edtSdate.getValue())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        getSiteList()



        bottomSheetBehavior = BottomSheetBehavior.from(bottom)


        txtExistSite.setOnClickListener {
            if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
            else
                bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }


    }


    fun stateSpinnerListner() {
        spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && stateNameList.size > position) {
                    stateID = session.stetList.get(position).stateID.toString()
                    Logger.d("state", stateID)
                    getCityList(stateID)
                }

            }
        }
    }

    fun citySpinnerListner() {
        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && cityListArray.size > position) {
                    cityID = cityListArray.get(position).cityID.toString()
                    Logger.d("city", cityID)
                }

            }
        }
    }

    fun stateViewClick() {
        view.setOnClickListener {

            SearchableDialog(this,
                stateIteams!!,
                getString(R.string.select_state),
                { item, _ ->
                    spState.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun cityViewClick() {
        view2.setOnClickListener {

            SearchableDialog(this,
                cityIteams!!,
                getString(R.string.select_city),
                { item, _ ->
                    spCity.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun getStateSppinerData() {
        var myList: MutableList<SearchableItem> = mutableListOf()
        for (i in session.stetList.indices) {
            stateNameList.add(session.stetList.get(i).stateName.toString())
            myList.add(SearchableItem(i.toLong(), stateNameList.get(i)))
        }
        stateIteams = myList

        adapterState = ArrayAdapter(this, R.layout.custom_spinner_item, stateNameList)
        spState.setAdapter(adapterState)

        for (i in session.stetList.indices) {
            if (session.stetList.get(i).stateID.toString().equals(stateID)) {
                spState.setSelection(i)
            }

        }

        if (stateID.equals("-1")) {
            spState.setSelection(11)
        }
    }

    fun getCityList(stateid: String) {
        showProgressbar()
        cityListArray.clear()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", stateid)

            result = Networking.setParentJsonData(Constant.METHOD_CITY_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .getCityList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CityListModel>() {
                override fun onSuccess(response: CityListModel) {
                    hideProgressbar()

                    if (response.error == 200) {
                        view2.isEnabled = true
                        cityListArray?.addAll(response.data)
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        for (items in response.data.indices) {
                            cityNameList.add(response.data.get(items).cityName.toString())
                            myList.add(SearchableItem(items.toLong(), cityNameList.get(items)))
                        }
                        cityIteams = myList

                        adapterCity = ArrayAdapter(
                            this@AddSiteActivity,
                            R.layout.custom_spinner_item,
                            cityNameList
                        )
                        spCity.setAdapter(adapterCity)

                        for (i in response.data.indices) {
                            if (response.data.get(i).cityID.equals(cityID)) {
                                spCity.setSelection(i)
                            }
                        }

                        if (cityID.equals("")) {
                            spCity.setSelection(-1)
                        }

                    } else {
                        cityListArray.clear()
                        cityNameList.clear()
                        cityID = "-1"
                        adapterCity?.clear()
                        // spCity.removeAllViews()
                        view2.isEnabled = false

                        /*if (cityID.equals("")) {
                            spCity.setSelection(-1)
                        }*/
                    }


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun validation(flag: Boolean) {
        val selectedId: Int = rg.getCheckedRadioButtonId()
        val rbLead = findViewById<View>(selectedId) as? RadioButton
        when {
            serviceId.equals("-1") -> {
                root.showSnackBar("Select Service")
            }

            edtSiteName.isEmpty() -> {
                root.showSnackBar("Enter Site Name")
                edtSiteName.requestFocus()
            }
//            edtCompanyName.isEmpty() -> {
//                root.showSnackBar("Enter Company Name or Individual Name")
//                edtCompanyName.requestFocus()
//            }

            inWorkingd.isVisible && edtWorkingHour.isEmpty() -> {
                root.showSnackBar("Enter Working Hours")
                edtWorkingHour.requestFocus()
            }
            inWorkingh.isVisible && edtWorkingDays.isEmpty() -> {
                root.showSnackBar("Enter Working Days")
                edtWorkingDays.requestFocus()
            }
            edtAddress.isEmpty() -> {
                root.showSnackBar("Enter Address")
                edtAddress.requestFocus()
            }

            !edtPincode.isEmpty() && edtPincode.getValue().length < 6 -> {
                root.showSnackBar("Enter Valid Pincode")
                edtPincode.requestFocus()
            }
            cityID == "-1" -> {
                root.showSnackBar("City Not Found")
            }
            !edtGST.isEmpty() && !validGSTIN(edtGST.getValue()) -> {
                root.showSnackBar("Enter Valid GST No.")
                edtGST.requestFocus()

            }


            else -> {
                addSite(rbLead?.text.toString(), flag)
            }

        }
    }

    fun addSite(siteType: String?, flag: Boolean) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            //  jsonBody.put("Name", edtCompanyName.getValue())
            jsonBody.put("VisitorID", intent.getStringExtra(Constant.VISITOR_ID))
            jsonBody.put("CustomerID", intent.getStringExtra(Constant.CUSTOMER_ID))
            jsonBody.put("Address", edtAddress.getValue())
            jsonBody.put("SiteName", edtSiteName.getValue())
            jsonBody.put("StateID", stateID)
            jsonBody.put("CityID", cityID)
            if (!serviceId.equals("1")) {
                jsonBody.put("WorkingHours", "1")
                jsonBody.put("WorkingDays", "1")
            } else {
                jsonBody.put("WorkingHours", edtWorkingHour.getValue())
                jsonBody.put("WorkingDays", edtWorkingDays.getValue())
            }
            jsonBody.put("PinCode", edtPincode.getValue())
            jsonBody.put("GSTNo", edtGST.getValue())
            jsonBody.put("Address2", edtAddress2.getValue())
            jsonBody.put("ProposedDate", TimeStamp.formatDateFromString(edtPdate.getValue()))
            jsonBody.put("StartDate", TimeStamp.formatDateFromString(edtSdate.getValue()))
            jsonBody.put("EndDate", TimeStamp.formatDateFromString(edtEdate.getValue()))
            jsonBody.put("SiteID", siteID)
            jsonBody.put("ServiceID", serviceId)
            val selectedId: Int = rg.getCheckedRadioButtonId()
            val rbSite = findViewById<View>(selectedId) as? RadioButton
            jsonBody.put("SiteType", rbSite?.text.toString())

            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_SITE,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .AddSite(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<SiteListModal>() {
                override fun onSuccess(response: SiteListModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        if (flag) {
                            val i = Intent(this@AddSiteActivity, AddQuotationActivity::class.java)
                            i.putExtra(Constant.DATA, data.get(0))
                            startActivity(i)
                        }
                        finish()
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

    private fun getServiceList() {
        var result = ""
        try {
            val jsonBody = JSONObject()


            result = Networking.setParentJsonData(Constant.METHOD_SERVICE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .getServiceList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ServiceListModel>() {
                override fun onSuccess(response: ServiceListModel) {
                    serviceListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    serviceNameList!!.add("Select Service")
                    myList.add(
                        SearchableItem(
                            0,
                            "Select Service"
                        )
                    )

                    for (items in response.data.indices) {
                        serviceNameList!!.add(response.data.get(items).service.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                serviceNameList.get(items + 1)
                            )
                        )

                    }
                    itemService = myList

                    adapterService = ArrayAdapter(
                        this@AddSiteActivity,
                        R.layout.custom_spinner_item,
                        serviceNameList!!
                    )
                    spService.setAdapter(adapterService)

                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun serviceSpinnerListner() {
        spService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && serviceListArray.size > position - 1) {
                    if (position == 0) {
                        serviceId = "-1"
                    } else {
                        serviceId = serviceListArray.get(position - 1).serviceID.toString()


                    }

                    if (serviceId.equals("1")) {
                        edtWorkingDays.visible()
                        edtWorkingHour.visible()
                    } else {
                        edtWorkingDays.invisible()
                        edtWorkingHour.invisible()
                    }

                }

            }
        }
    }

    fun serviceViewClick() {
        view5.setOnClickListener {
            if (itemService.size > 0)
                SearchableDialog(this,
                    itemService!!,
                    getString(R.string.select_service), { item, _ ->
                        spService.setSelection(item.id.toInt())
                    }).show()
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
            if (leadItem != null) {
                jsonBody.put("CustomerID", leadItem!!.customerID)
                jsonBody.put("VisitorID", leadItem!!.visitorID)
            }

            if (customerData != null) {
                jsonBody.put("CustomerID", customerData!!.customerID)
                jsonBody.put("VisitorID", customerData!!.visitorID)
            }

            jsonBody.put("SiteName", CustomerSiteFragment.name)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_SITE_LIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(this)
            .getServices()
            .getSiteList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<SiteListModal>() {
                override fun onSuccess(response: SiteListModal) {
                    if (response.error == 200) {

                        list.addAll(response.data)

                        list.add(response.data.get(0))
                        adapter?.notifyDataSetChanged()

                        if (list.size > 0) {
                            txtExistSite.visible()
                        } else {
                            txtExistSite.invisible()
                        }
                    }


                }

                override fun onFailed(code: Int, message: String) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recSiteAddress.layoutManager = layoutManager
        adapter = SiteAddressAdapter1(this, list, this)
        recSiteAddress.adapter = adapter

    }


    override fun onItemSelect(position: Int, data: SiteListItem) {
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        edtSiteName.setText(data.siteName)
        edtGST.setText(data.gSTNo)
        edtAddress.setText(data.address)
        edtAddress2.setText(data.address2)
        edtPincode.setText(data.pinCode)
        edtSdate.setText(formatServerDateToLocal(data.startDate.toString()))
        edtEdate.setText(formatServerDateToLocal(data.endDate.toString()))
        edtPdate.setText(formatServerDateToLocal(data.proposedDate.toString()))
        edtWorkingDays.setText(data.workingDays)
        edtWorkingHour.setText(data.workingHours)

        edtSiteName.isEnabled = false
        edtGST.isEnabled = false
        edtAddress.isEnabled = false
        edtAddress2.isEnabled = false
        edtPincode.isEnabled = false


        for (i in cityListArray.indices) {
            if (cityListArray.get(i).cityID.equals(data.cityID)) {
                spCity.setSelection(i)
                break
            }
        }

        for (i in session.stetList.indices) {
            if (session.stetList.get(i).stateID.toString().equals(data.stateID)) {
                spState.setSelection(i)
                break
            }

        }

        view.isClickable = false
        view2.isClickable = false
        linlayCity.isClickable = false
        linlayState.isClickable = false
        view.isEnabled = false
        view2.isEnabled = false
        linlayCity.isEnabled = false
        linlayState.isEnabled = false


        siteID = data.sitesID.toString()

    }

    override fun onItemSelect(position: Int) {
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        siteID = "-1"
        edtSiteName.setText("")
        edtGST.setText("")
        edtAddress.setText("")
        edtAddress2.setText("")
        edtPincode.setText("")
        edtWorkingDays.setText("")
        edtWorkingHour.setText("")


        edtSiteName.isEnabled = true
        edtGST.isEnabled = true
        edtAddress.isEnabled = true
        edtAddress2.isEnabled = true
        edtPincode.isEnabled = true

        edtSdate.setText(getCurrentDate())
        edtEdate.setText(getCurrentDate())
        edtPdate.setText(getCurrentDate())


        edtSdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtSdate) }
        edtEdate.setOnClickListener {
            showNextFromStartDateTimePicker(
                this@AddSiteActivity,
                edtEdate,
                edtSdate.getValue()
            )
        }
        edtPdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtPdate) }

        view.isClickable = true
        view2.isClickable = true
        linlayCity.isClickable = true
        linlayState.isClickable = true
    }
}