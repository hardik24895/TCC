package com.tcc.app.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tcc.app.R
import com.tcc.app.adapter.SiteAddressAdapter
import com.tcc.app.extention.*
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.GSTINValidator
import com.tcc.app.utils.Logger
import com.tcc.app.utils.TimeStamp.formatDateFromString
import com.tcc.app.utils.TimeStamp.formatServerDateToLocal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_lead.*
import kotlinx.android.synthetic.main.bottom_dailog_attendance.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList


class AddLeadActivity : BaseActivity(), SiteAddressAdapter.OnItemSelected {
    var leadItem: LeadItem? = null
    var stateNameList: ArrayList<String> = ArrayList()
    var adapterState: ArrayAdapter<String>? = null
    var stateIteams: List<SearchableItem>? = null
    var stateID: String = "-1"
    var siteID: String = "-1"
    var cityID: String = "-1"
    var cityNameList: ArrayList<String> = ArrayList()
    var cityListArray: ArrayList<CityDataItem> = ArrayList()
    var adapterCity: ArrayAdapter<String>? = null
    var cityIteams: List<SearchableItem>? = null
    var leadType: String = ""
    var adapter: SiteAddressAdapter? = null
    var site: SitesItem? = null
    var isEdit: Boolean = false

    var list: ArrayList<SitesItem> = ArrayList()
    var bottomSheetBehavior: BottomSheetBehavior<CardView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_lead)
        rbHot.isChecked = true
        getBundleData()
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = resources.getText(R.string.visitor)

        btnSubmit.setOnClickListener {
            if (isEdit) {
                validationEdit(false)
            } else
                validation(false)
        }

        if (session.roleData.data?.sites?.isInsert.equals("0")) {
            btnAddSite.invisible()
        }

        if (intent.hasExtra("Edit")) {
            isEdit = true

        }

        if (intent.getStringExtra(Constant.SERVICE_ID).equals("1")) {
            inWorkingd.visible()
            inWorkingh.visible()

        }

        if (isEdit) {
            crd_4.invisible()
            txtExistSite.invisible()
            inSiteName.invisible()
            ingst.invisible()
            inendDate.invisible()
            inStartdaate.invisible()
            inpDate.invisible()


        } else {


            if (intent.hasExtra(Constant.DATA_SITE)) {

                val gson = Gson()
                val type: Type = object : TypeToken<List<SitesItem?>?>() {}.type
                val siteList: List<SitesItem> =
                    gson.fromJson(intent.getStringExtra(Constant.DATA_SITE), type)
                list.addAll(siteList)

                if (list.size > 0) {
                    list.add(siteList.get(0))
                    bottomSheetBehavior = BottomSheetBehavior.from(bottom)

                    txtExistSite.setOnClickListener {
                        if (bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED)
                            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                        else
                            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                    }

                    setupRecyclerView()
                    txtExistSite.visible()
                } else
                    txtExistSite.invisible()
            } else {
                txtExistSite.invisible()

            }
        }

        edtSdate.setText(getCurrentDate())
        edtEdate.setText(getCurrentDate())
        edtPdate.setText(getCurrentDate())


        edtSdate.setOnClickListener {


            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this, R.style.DialogTheme,
                { view, year, monthOfYear, dayOfMonth ->

                    var selectedMonth: String = ""
                    var selectedDay: String = ""
                    if (dayOfMonth < 10) {
                        selectedDay = "0" + dayOfMonth
                    } else
                        selectedDay = dayOfMonth.toString()


                    if (monthOfYear < 10) {
                        selectedMonth = "0" + (monthOfYear + 1)
                    } else
                        selectedMonth = monthOfYear.toString()

                    edtSdate.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
                    edtEdate.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
                },
                year,
                month,
                day
            )
            dpd.show()
            dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(this.getColorCompat(R.color.colorPrimary))
            dpd.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(this.getColorCompat(R.color.colorPrimary))
        }
        edtEdate.setOnClickListener {
            showNextFromStartDateTimePicker(this@AddLeadActivity, edtEdate, edtSdate.getValue())
        }
        edtPdate.setOnClickListener { showDateTimePicker(this@AddLeadActivity, edtPdate) }

        btnAddSite.setOnClickListener {

            if (isEdit) {
                validationEdit(true)
            } else
                validation(true)

        }
        stateSpinnerListner()
        citySpinnerListner()
        stateViewClick()
        cityViewClick()


    }


    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recSiteAddress.layoutManager = layoutManager
        adapter = SiteAddressAdapter(this, list, this)
        recSiteAddress.adapter = adapter

    }

    fun getBundleData() {
        edtMobile.setText(intent.getStringExtra(Constant.MOBILE))
        if (intent.hasExtra(Constant.DATA)) {
            leadItem = intent.getSerializableExtra(Constant.DATA) as LeadItem
            setData()
        }
        getStateSppinerData()
        getCityList(stateID)
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
                    //   Logger.d("state", stateID)

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
                    stateID = cityListArray.get(position).stateID.toString()
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

    fun setData() {
        edtName.setText(leadItem?.name.toString())
        edtEmail.setText(leadItem?.emailID.toString())
        edtMobile.setText(leadItem?.mobileNo.toString())
        edtAddress.setText(leadItem?.address.toString())
        edtAddress1.setText(leadItem?.address2.toString())
        edtPincode.setText(leadItem?.pinCode.toString())
        stateID = leadItem?.stateID.toString()
        cityID = leadItem?.cityID.toString()
        if (leadItem?.leadType.toString().equals(Constant.HOT)) {
            rbHot.isChecked = true
        } else if (leadItem?.leadType.toString().equals(Constant.WARM)) {
            rbWarm.isChecked = true
        } else if (leadItem?.leadType.toString().equals(Constant.COLD)) {
            rbCold.isChecked = true
        } else if (leadItem?.leadType.toString().equals(Constant.SILENT)) {
            rbSilent.isChecked = true
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
                    cityListArray.clear()
                    cityNameList.clear()
                    if (response.error == 200) {
                        view2.isEnabled = true
                        cityListArray.addAll(response.data)
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        for (items in response.data.indices) {
                            cityNameList.add(response.data.get(items).cityName.toString())
                            myList.add(SearchableItem(items.toLong(), cityNameList.get(items)))
                        }
                        cityIteams = myList

                        adapterCity = ArrayAdapter(
                            this@AddLeadActivity,
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
            edtName.isEmpty() -> {
                root.showSnackBar("Enter Name")
                edtName.requestFocus()
            }
            edtEmail.isEmpty() -> {
                root.showSnackBar("Enter Email")
                edtEmail.requestFocus()
            }
            !isValidEmail(edtEmail.getValue()) -> {
                root.showSnackBar("Enter valid email")
                edtEmail.requestFocus()
            }
            edtMobile.isEmpty() -> {
                root.showSnackBar("Enter  Mobile No.")
                edtMobile.requestFocus()
            }
            edtMobile.getValue().length < 10 -> {
                root.showSnackBar("Enter Valid Mobile No.")
                edtMobile.requestFocus()
            }

            edtSiteName.isEmpty() -> {
                root.showSnackBar("Enter Site Name")
                edtSiteName.requestFocus()
            }

            !edtGST.isEmpty() -> {
                if (!GSTINValidator.validGSTIN(edtGST.getValue())) {
                    root.showSnackBar("Enter Valid GST No.")
                    edtGST.requestFocus()
                }
            }

            edtAddress.isEmpty() -> {
                root.showSnackBar("Enter Address")
                edtAddress.requestFocus()
            }


            edtAddress1.isEmpty() -> {
                root.showSnackBar("Enter Address1")
                edtAddress1.requestFocus()
            }
            !edtPincode.isEmpty() && edtPincode.getValue().length < 6 -> {
                root.showSnackBar("Enter Valid Pincode")
                edtPincode.requestFocus()
            }
            stateID.equals("-1") -> {
                root.showSnackBar("Select State")
            }
            cityID.equals("-1") -> {
                root.showSnackBar("Select City")
            }
            selectedId == -1 -> {
                root.showSnackBar("Select Lead Type")
            }
            inWorkingh.isVisible && edtWorkingDays.isEmpty() -> {
                root.showSnackBar("Enter Working Days")
                edtWorkingDays.requestFocus()
            }
            inWorkingd.isVisible && edtWorkingHour.isEmpty() -> {
                root.showSnackBar("Enter Working Hours")
                edtWorkingHour.requestFocus()
            }

            else -> {

                addLead(rbLead?.text.toString(), flag)

            }
        }

    }

    fun validationEdit(flag: Boolean) {
        val selectedId: Int = rg.getCheckedRadioButtonId()
        val rbLead = findViewById<View>(selectedId) as? RadioButton
        when {
            edtName.isEmpty() -> {
                root.showSnackBar("Enter Name")
                edtName.requestFocus()
            }
            edtEmail.isEmpty() -> {
                root.showSnackBar("Enter Email")
                edtEmail.requestFocus()
            }
            !isValidEmail(edtEmail.getValue()) -> {
                root.showSnackBar("Enter valid email")
                edtEmail.requestFocus()
            }
            edtMobile.isEmpty() -> {
                root.showSnackBar("Enter  Mobile No.")
                edtMobile.requestFocus()
            }
            edtMobile.getValue().length < 10 -> {
                root.showSnackBar("Enter Valid Mobile No.")
                edtMobile.requestFocus()
            }

            edtAddress.isEmpty() -> {
                root.showSnackBar("Enter Address")
                edtAddress.requestFocus()
            }


            edtAddress1.isEmpty() -> {
                root.showSnackBar("Enter Address1")
                edtAddress1.requestFocus()
            }
            !edtPincode.isEmpty() && edtPincode.getValue().length < 6 -> {
                root.showSnackBar("Enter Valid Pincode")
                edtPincode.requestFocus()
            }
            stateID.equals("-1") -> {
                root.showSnackBar("Select State")
            }
            cityID.equals("-1") -> {
                root.showSnackBar("Select City")
            }
            selectedId == -1 -> {
                root.showSnackBar("Select Lead Type")
            }
            cityID == "-1" -> {
                root.showSnackBar("City Not Found")
            }
            else -> {

                editLead(rbLead?.text.toString(), flag)
            }

        }


    }

    fun addLead(leadType: String?, flag: Boolean) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Name", edtName.getValue())
            jsonBody.put("MobileNo", edtMobile.getValue())
            jsonBody.put("EmailID", edtEmail.getValue())
            jsonBody.put("Address", edtAddress.getValue())
            jsonBody.put("Address2", edtAddress1.getValue())
            jsonBody.put("CityID", cityID)
            jsonBody.put("StateID", stateID)
            jsonBody.put("SiteID", siteID)
            jsonBody.put("PinCode", edtPincode.getValue())
            jsonBody.put("ServiceID", intent.getStringExtra(Constant.SERVICE_ID))
            jsonBody.put("GSTNo", edtGST.getValue())
            jsonBody.put("LeadType", leadType)
            jsonBody.put("SiteName", edtSiteName.getValue())

//            if (intent.getStringExtra(Constant.SERVICE_ID).equals("1")) {
//                jsonBody.put("WorkingHours", edtWorkingHour.getValue())
//                jsonBody.put("WorkingDays", edtWorkingDays.getValue())
//            } else {
//                jsonBody.put("WorkingHours", "1")
//                jsonBody.put("WorkingDays", "1")
//            }
            jsonBody.put("StartDate", formatDateFromString(edtSdate.getValue()))
            jsonBody.put("EndDate", formatDateFromString(edtEdate.getValue()))
            jsonBody.put("ProposedDate", formatDateFromString(edtPdate.getValue()))
            val selectedId: Int = rg1.getCheckedRadioButtonId()
            val rbSite = findViewById<View>(selectedId) as? RadioButton
            jsonBody.put("SiteType", rbSite?.text.toString())
            jsonBody.put("GSTNo", edtGST.getValue())

            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_LEAD,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .addLead(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<SiteListModal>() {
                override fun onSuccess(response: SiteListModal) {

                    val data = response.data
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        if (flag) {
                            val i = Intent(this@AddLeadActivity, AddQuotationActivity::class.java)
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

    fun editLead(leadType: String?, flag: Boolean) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("VisitorID", leadItem?.visitorID)
            jsonBody.put("Name", edtName.getValue())
            jsonBody.put("MobileNo", edtMobile.getValue())
            jsonBody.put("EmailID", edtEmail.getValue())
            jsonBody.put("Address", edtAddress.getValue() + ", " + edtAddress1.getValue())
            jsonBody.put("CityID", cityID)
            jsonBody.put("StateID", stateID)
            jsonBody.put("PinCode", edtPincode.getValue())
            jsonBody.put("LeadType", leadType)


            result = Networking.setParentJsonData(
                Constant.METHOD_EDIT_LEAD,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .editLead(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        if (flag) {
                            val intent = Intent(this@AddLeadActivity, AddSiteActivity::class.java)
                            intent.putExtra(Constant.VISITOR_ID, data.get(0).iD.toString())
                            intent.putExtra(Constant.CUSTOMER_ID, "0")
                            intent.putExtra(Constant.CUSTOMER_NAME, edtName.getValue())
                            startActivity(intent)
                        } else {
                            showAlert(response.message.toString())
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

    override fun onItemSelect(position: Int, data: SitesItem) {
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        edtSiteName.setText(data.siteName)
        edtGST.setText(data.gSTNo)
        edtAddress.setText(data.address)
        edtAddress1.setText(data.address2)
        edtPincode.setText(data.pinCode)
        edtSdate.setText(formatServerDateToLocal(data.startDate.toString()))
        edtEdate.setText(formatServerDateToLocal(data.endDate.toString()))
        edtPdate.setText(formatServerDateToLocal(data.proposedDate.toString()))
        edtWorkingDays.setText(data.workingDays)
        edtWorkingHour.setText(data.workingHours)

        edtSiteName.isEnabled = false
        edtGST.isEnabled = false
        edtAddress.isEnabled = false
        edtAddress1.isEnabled = false
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
        edtAddress1.setText("")
        edtPincode.setText("")
        edtWorkingDays.setText("")
        edtWorkingHour.setText("")
        edtSiteName.isEnabled = true
        edtGST.isEnabled = true
        edtAddress.isEnabled = true
        edtAddress1.isEnabled = true
        edtPincode.isEnabled = true

        edtSdate.setText(getCurrentDate())
        edtEdate.setText(getCurrentDate())
        edtPdate.setText(getCurrentDate())


        edtSdate.setOnClickListener { showDateTimePicker(this@AddLeadActivity, edtSdate) }
        edtEdate.setOnClickListener {
            showNextFromStartDateTimePicker(
                this@AddLeadActivity,
                edtEdate,
                edtSdate.getValue()
            )
        }
        edtPdate.setOnClickListener { showDateTimePicker(this@AddLeadActivity, edtPdate) }

        view.isClickable = true
        view2.isClickable = true
        linlayCity.isClickable = true
        linlayState.isClickable = true
    }
}