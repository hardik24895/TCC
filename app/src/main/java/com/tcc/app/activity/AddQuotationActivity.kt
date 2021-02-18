package com.tcc.app.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_quotation.*
import kotlinx.android.synthetic.main.row_dynamic_user.view.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import tech.hibk.searchablespinnerlibrary.SearchableSpinner
import java.text.DecimalFormat


class AddQuotationActivity : BaseActivity() {

    var compnyID: String = ""
    var siteListItem: SiteListItem? = null
    var leadItem: LeadItem? = null
    var companyNameList: ArrayList<String> = ArrayList()
    var companyListArray: ArrayList<CompanyDataItem> = ArrayList()
    var adaptercompany: ArrayAdapter<String>? = null
    var companyIteams: List<SearchableItem>? = null


    var serviceNameList: ArrayList<String> = ArrayList()
    var adapterService: ArrayAdapter<String>? = null
    var serviceListArray: ArrayList<ServiceDataItem> = ArrayList()
    var itemService: List<SearchableItem>? = null
    var serviceId: String = ""

    var userTypeNameList: ArrayList<String>? = null
    var adapterUserType: ArrayAdapter<String>? = null
    var userTypeListArray: ArrayList<UserTypeDataItem>? = null
    var itemUserType: List<SearchableItem>? = null
    var usertypeId: String = ""
    //  var usertypeChildId: String = ""


    var stateNameList: ArrayList<String> = ArrayList()
    var adapterState: ArrayAdapter<String>? = null
    var stateIteams: List<SearchableItem>? = null
    var stateID: String = ""
    var cityID: String = ""
    var cityNameList: ArrayList<String> = ArrayList()
    var cityListArray: ArrayList<CityDataItem> = ArrayList()
    var adapterCity: ArrayAdapter<String>? = null
    var cityIteams: List<SearchableItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_quotation)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Quotation"
        btnAddUser.setOnClickListener { onAddField() }

        if (intent.hasExtra(Constant.DATA)) {
            siteListItem = intent.getSerializableExtra(Constant.DATA) as SiteListItem
        }
        if (intent.hasExtra(Constant.DATA1)) {
            leadItem = intent.getSerializableExtra(Constant.DATA1) as LeadItem
        }
        userTypeNameList = ArrayList()
        userTypeListArray = ArrayList()

        serviceNameList = ArrayList()
        serviceListArray = ArrayList()

        edEstimationDate.setText(getCurrentDate())
        edEstimationDate.setOnClickListener {
            showDateTimePicker(
                this@AddQuotationActivity,
                edEstimationDate
            )
        }

        edRate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpdatedTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        edQty.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpdatedTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        btnSubmit.setOnClickListener {
            validation()
        }

        getCompanyList()
        getServiceList()
        getUserTypeList()
        getStateSppinerData()
        getCityList(stateID)

        companySpinnerListner()
        serviceSpinnerListner()
        userTypeSpinnerListner()
        stateSpinnerListner()
        citySpinnerListner()

        compnyViewClick()
        serviceViewClick()
        userTypeViewClick()
        stateViewClick()
        cityViewClick()
    }


    fun validation() {
        when {
            edAddress1.isEmpty() -> {
                root.showSnackBar("Enter Address 1")
                edAddress1.requestFocus()
            }
            edAddress2.isEmpty() -> {
                root.showSnackBar("Enter Address 2")
                edAddress2.requestFocus()
            }
            edPincode.isEmpty() -> {
                root.showSnackBar("Select Pincode")
                edPincode.requestFocus()
            }

            else -> {
                AddQuotation()
            }

        }
    }


    private fun getServiceList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", "")

            result = Networking.setParentJsonData(Constant.METHOD_SERVICE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddQuotationActivity)
            .getServices()
            .getServiceList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ServiceListModel>() {
                override fun onSuccess(response: ServiceListModel) {
                    serviceListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    for (items in response.data.indices) {
                        serviceNameList!!.add(response.data.get(items).service.toString())
                        myList.add(SearchableItem(items.toLong(), serviceNameList.get(items)))

                    }
                    itemService = myList

                    adapterService = ArrayAdapter(
                        this@AddQuotationActivity,
                        R.layout.custom_spinner_item,
                        serviceNameList!!
                    )
                    spService.setAdapter(adapterService)


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }

    private fun cityViewClick() {
        view3.setOnClickListener {

            SearchableDialog(this,
                cityIteams!!,
                getString(R.string.select_city),
                { item, _ ->
                    spCity.setSelection(item.id.toInt())
                }).show()
        }
    }

    private fun userTypeViewClick() {

        view2.setOnClickListener {
            SearchableDialog(this@AddQuotationActivity,
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
                    usertypeId = userTypeListArray!!.get(position).usertypeID.toString()
                    edHSN.setText(userTypeListArray!!.get(position).hSNNo)
                    edQty.setText("1")
                    edRate.setText(userTypeListArray!!.get(position).rate)
                    setUpdatedTotal()

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

    fun stateViewClick() {
        view4.setOnClickListener {

            SearchableDialog(this,
                stateIteams!!,
                getString(R.string.select_state),
                { item, _ ->
                    spState.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun onAddField() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.row_dynamic_user, null, false)
        var btnRemove: TextView = rowView.findViewById(R.id.btnRemoveUser)
        var spUserTypeChild: SearchableSpinner = rowView.findViewById(R.id.spUserTypeChild)
        var viewChild: View = rowView.findViewById(R.id.viewUserTypeChild)
        var edHSNChild: EditText = rowView.findViewById(R.id.edHSNChild)
        var edQtyChild: EditText = rowView.findViewById(R.id.edQtyChild)
        var edRateChild: EditText = rowView.findViewById(R.id.edRateChild)
        // userTypeChildId.add("")
        // Log.e("TAG", "onAddField:      "+userTypeChildId.size )


        btnRemove.setOnClickListener {
            lin_add_user.removeView(rowView)
            //     userTypeChildId.removeAt((lin_add_user as ViewGroup).indexOfChild(btnRemove))
            setUpdatedTotal()
        }
        adapterUserType = ArrayAdapter(
            this@AddQuotationActivity,
            R.layout.custom_spinner_item,
            userTypeNameList!!
        )
        spUserTypeChild.setAdapter(adapterUserType)


        spUserTypeChild.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && userTypeListArray!!.size > position) {


                    Log.e(
                        "TAG",
                        "onAddField:      " + (lin_add_user as ViewGroup).indexOfChild(
                            spUserTypeChild
                        )
                    )
                    //               userTypeChildId.set((lin_add_user as ViewGroup).indexOfChild(spUserTypeChild), userTypeListArray!!.get(position).usertypeID.toString())
                    // usertypeChildId =
                    edHSNChild.setText(userTypeListArray!!.get(position).hSNNo)
                    edQtyChild.setText("1")
                    edRateChild.setText(userTypeListArray!!.get(position).rate)
                    setUpdatedTotal()
                }

            }
        }


        edRateChild.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpdatedTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        edQtyChild.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpdatedTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        viewChild.setOnClickListener {
            SearchableDialog(
                this@AddQuotationActivity,
                itemUserType!!,
                getString(R.string.select_usertype),
                { item, _ -> spUserTypeChild.setSelection(item.id.toInt()) }).show()
        }

        lin_add_user!!.addView(rowView)
    }

    fun compnyViewClick() {
        view1.setOnClickListener {

            SearchableDialog(this,
                companyIteams!!,
                getString(R.string.select_company),
                { item, _ ->
                    spCompany.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun serviceViewClick() {
        view5.setOnClickListener {

            SearchableDialog(this,
                itemService!!,
                getString(R.string.select_service),
                { item, _ ->
                    spService.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun companySpinnerListner() {
        spCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && companyListArray.size > position) {
                    compnyID = companyListArray.get(position).companyID.toString()
                    Logger.d("companyID", compnyID)
                }

            }
        }
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
                if (position != -1 && serviceListArray.size > position) {
                    serviceId = serviceListArray.get(position).serviceID.toString()


                    Logger.d("serviceID : ", serviceId)
                }

            }
        }
    }

    fun getCompanyList() {
        showProgressbar()
        companyListArray.clear()
        var result = ""
        try {
            val jsonBody = JSONObject()
            // jsonBody.put("StateID", stateid)

            result = Networking.setParentJsonData(Constant.METHOD_COMPANY_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .getCompanyList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CompanyListModal>() {
                override fun onSuccess(response: CompanyListModal) {
                    hideProgressbar()
                    companyListArray?.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    for (items in response.data.indices) {
                        companyNameList.add(response.data.get(items).companyName.toString())
                        myList.add(SearchableItem(items.toLong(), companyNameList.get(items)))
                    }
                    companyIteams = myList

                    adaptercompany = ArrayAdapter(
                        this@AddQuotationActivity,
                        R.layout.custom_spinner_item,
                        companyNameList
                    )
                    spCompany.setAdapter(adaptercompany)

                    for (i in response.data.indices) {
                        if (response.data.get(i).companyID.equals(compnyID)) {
                            spCompany.setSelection(i)
                        }
                    }

                    if (compnyID.equals("")) {
                        spCompany.setSelection(-1, true)
                        spCompany.nothingSelectedText = getString(R.string.select_company)
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                }

            }).addTo(autoDisposable)
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
            .with(this@AddQuotationActivity)
            .getServices()
            .getUserTypeList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<UserTypeListModel>() {
                override fun onSuccess(response: UserTypeListModel) {
                    userTypeListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    for (items in response.data.indices) {
                        userTypeNameList!!.add(response.data.get(items).usertype.toString())
                        myList.add(SearchableItem(items.toLong(), userTypeNameList!!.get(items)))

                    }
                    itemUserType = myList

                    adapterUserType = ArrayAdapter(
                        this@AddQuotationActivity,
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

        if (stateID.equals("")) {
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
                    cityListArray?.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    for (items in response.data.indices) {
                        cityNameList.add(response.data.get(items).cityName.toString())
                        myList.add(SearchableItem(items.toLong(), cityNameList.get(items)))
                    }
                    cityIteams = myList

                    adapterCity = ArrayAdapter(
                        this@AddQuotationActivity,
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


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }


    fun setUpdatedTotal() {

        var TotalAmount: Float = 0f
        var CGST: Float = 0f
        var SGST: Float = 0f
        var IGST: Float = 0f


        if (!edQty.isEmpty() && !edRate.isEmpty()) {

            TotalAmount = TotalAmount + (edQty.getValue().toFloat() * edRate.getValue().toFloat())

        }

        if (lin_add_user.childCount > 0) {
            for (item in 0 until lin_add_user.childCount) {
                if (!lin_add_user.getChildAt(item).edQtyChild.isEmpty() && !lin_add_user.getChildAt(
                        item
                    ).edRateChild.isEmpty()
                ) {
                    TotalAmount = TotalAmount + (lin_add_user.getChildAt(item).edQtyChild.getValue()
                        .toFloat() * lin_add_user.getChildAt(item).edRateChild.getValue().toFloat())

                }

            }
        }


        if (TotalAmount == 0f) {
            edTotalAmount.setText("")
            edCGST.setText("")
            edSGST.setText("")
            edIGST.setText("")
        } else {

            CGST = CGST + ((TotalAmount * session.configData.data?.cGST!!.toFloat()) / 100)
            SGST = SGST + ((TotalAmount * session.configData.data?.sGST!!.toFloat()) / 100)
            IGST = IGST + ((TotalAmount * session.configData.data?.iGST!!.toFloat()) / 100)

            edTotalAmount.setText(TotalAmount.toString())

            var df: DecimalFormat = DecimalFormat("##.##")

            if (siteListItem?.stateID?.toInt() == 12) {
                edCGST.setText(df.format(CGST))
                edSGST.setText(df.format(SGST))
                edIGST.setText("")
            } else {
                edCGST.setText("")
                edSGST.setText("")
                edIGST.setText(df.format(IGST))
            }
        }
    }


    fun AddQuotation() {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            val jsonArray = JSONArray()

            jsonBody.put("SitesID", siteListItem?.sitesID)
            jsonBody.put("CompanyID", compnyID)
            jsonBody.put("EstimateDate", formatDateFromString(edEstimationDate.getValue()))
            jsonBody.put("Address", edAddress1.getValue())
            jsonBody.put("Address2", edAddress2.getValue())
            jsonBody.put("CityID", cityID)
            jsonBody.put("StateID", stateID)
            jsonBody.put("PinCode", edPincode.getValue())
            jsonBody.put("SubTotal", edTotalAmount.getValue())
            jsonBody.put("CGST", edCGST.getValue())
            jsonBody.put("SGST", edSGST.getValue())
            jsonBody.put("IGST", edIGST.getValue())
            jsonBody.put("ServiceID", serviceId)
            if (leadItem != null) {
                jsonBody.put("VisitorID", leadItem?.visitorID)
                jsonBody.put("CustomerID", leadItem?.customerID)
            } else if (siteListItem != null) {
                jsonBody.put("VisitorID", siteListItem?.visitorID)
                jsonBody.put("CustomerID", siteListItem?.customerID)
            } else {
                jsonBody.put("VisitorID", siteListItem?.visitorID)
                jsonBody.put("CustomerID", siteListItem?.customerID)
            }
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Item", jsonArray)

            if (!edQty.isEmpty() && !edRate.isEmpty()) {
                val jsonObj = JSONObject()
                jsonObj.put("UsertypeID", usertypeId)
                jsonObj.put("Qty", edQty.getValue())
                jsonObj.put("Rate", edRate.getValue())

                jsonArray.put(jsonObj)
            }

            if (lin_add_user.childCount > 0) {
                for (item in 0 until lin_add_user.childCount) {
                    if (!lin_add_user.getChildAt(item).edQtyChild.isEmpty() && !lin_add_user.getChildAt(
                            item
                        ).edRateChild.isEmpty()
                    ) {
                        val jsonObj = JSONObject()
                        jsonObj.put(
                            "UsertypeID",
                            userTypeListArray?.get(lin_add_user.getChildAt(item).spUserTypeChild.selectedItemPosition)?.usertypeID
                        )
                        jsonObj.put("Qty", lin_add_user.getChildAt(item).edQtyChild.getValue())
                        jsonObj.put("Rate", lin_add_user.getChildAt(item).edRateChild.getValue())
                        jsonArray.put(jsonObj)

                    }

                }
            }




            result = Networking.setParentJsonData(Constant.METHOD_ADD_QUOTATIOON, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddQuotationActivity)
            .getServices()
            .AddQuotationData(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        finish()
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    showAlert(message.toString())
                }

            }).addTo(autoDisposable)
    }

}