package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.CityDataItem
import com.tcc.app.modal.CityListModel
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.modal.LeadItem
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_lead.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem


class AddLeadActivity : BaseActivity() {
    var leadItem: LeadItem? = null
    var stateNameList: ArrayList<String> = ArrayList()
    var adapterState: ArrayAdapter<String>? = null
    var stateIteams: List<SearchableItem>? = null
    var stateID: String = "-1"
    var cityID: String = "-1"
    var cityNameList: ArrayList<String> = ArrayList()
    var cityListArray: ArrayList<CityDataItem> = ArrayList()
    var adapterCity: ArrayAdapter<String>? = null
    var cityIteams: List<SearchableItem>? = null
    var leadType: String = ""

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

        btnSubmit.setOnClickListener { validation(false) }

        if (session.roleData.data?.sites?.isInsert.equals("0")) {
            btnAddSite.invisible()
        }
        btnAddSite.setOnClickListener { validation(true) }
        stateSpinnerListner()
        citySpinnerListner()
        stateViewClick()
        cityViewClick()

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


    fun setData() {
        edtName.setText(leadItem?.name.toString())
        edtEmail.setText(leadItem?.emailID.toString())
        edtMobile.setText(leadItem?.mobileNo.toString())
        edtAddress.setText(leadItem?.address.toString())
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

                    showAlert(message)

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
            edtAddress.isEmpty() -> {
                root.showSnackBar("Enter Address")
                edtAddress.requestFocus()
            }
            edtPincode.isEmpty() -> {
                root.showSnackBar("Enter Pincode")
                edtPincode.requestFocus()
            }
            edtPincode.getValue().length < 6 -> {
                root.showSnackBar("Enter  Valid Pincode")
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
                if (leadItem == null)
                    addLead(rbLead?.text.toString(), flag)
                else
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
            jsonBody.put("CityID", cityID)
            jsonBody.put("StateID", stateID)
            jsonBody.put("PinCode", edtPincode.getValue())
            jsonBody.put("LeadType", leadType)


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
                        }

                        finish()

                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
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
            jsonBody.put("Address", edtAddress.getValue())
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
            .addLead(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
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
                        }


                        finish()
                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }
}