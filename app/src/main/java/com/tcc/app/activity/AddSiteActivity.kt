package com.tcc.app.activity

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
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.GSTINValidator.validGSTIN
import com.tcc.app.utils.Logger
import com.tcc.app.utils.TimeStamp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_site.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.util.*

class AddSiteActivity : BaseActivity() {

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
        btnSubmit.setOnClickListener { validation() }

        btnAddQuatation.setOnClickListener { goToActivity<AddQuotationActivity>() }
        getStateSppinerData()
        getCityList(stateID)
        stateSpinnerListner()
        citySpinnerListner()
        stateViewClick()
        cityViewClick()

        edtSdate.setText(getCurrentDate())
        edtEdate.setText(getCurrentDate())
        edtPdate.setText(getCurrentDate())

        edtSdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtSdate) }
        edtEdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtEdate) }
        edtPdate.setOnClickListener { showDateTimePicker(this@AddSiteActivity, edtPdate) }
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

                    if (stateid.equals("-1")) {
                        spCity.setSelection(-1)
                    }


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }

    fun validation() {
        val selectedId: Int = rg.getCheckedRadioButtonId()
        val rbLead = findViewById<View>(selectedId) as? RadioButton
        when {
            edtSiteName.isEmpty() -> {
                root.showSnackBar("Enter Site Name")
                edtSiteName.requestFocus()
            }
            edtCompanyName.isEmpty() -> {
                root.showSnackBar("Enter Company Name or Individual Name")
                edtCompanyName.requestFocus()
            }

            edtWorkingDays.isEmpty() -> {
                root.showSnackBar("Enter  Working Days")
                edtWorkingDays.requestFocus()
            }
            edtWorkingHour.isEmpty() -> {
                root.showSnackBar("Enter WorkingHours")
                edtWorkingHour.requestFocus()
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
                root.showSnackBar("Enter Valid Pincode")
                edtPincode.requestFocus()
            }
            edtGST.isEmpty() -> {
                root.showSnackBar("Enter GST No.")
                edtGST.requestFocus()
            }
            !validGSTIN(edtGST.getValue()) -> {
                root.showSnackBar("Enter Valid GST No.")
                edtGST.requestFocus()
            }

            else -> {
                addSite(rbLead?.text.toString())
            }

        }
    }

    fun addSite(siteType: String?) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Name", edtCompanyName.getValue())
            jsonBody.put("VisitorID", intent.getStringExtra(Constant.ID))
            jsonBody.put("CustomerID", "0")
            jsonBody.put("Address", edtAddress.getValue())
            jsonBody.put("SiteName", edtSiteName.getValue())
            jsonBody.put("StateID", stateID)
            jsonBody.put("CityID", cityID)
            jsonBody.put("WorkingHours", edtWorkingHour.getValue())
            jsonBody.put("WorkingDays", edtWorkingDays.getValue())
            jsonBody.put("PinCode", edtPincode.getValue())
            jsonBody.put("GSTNo", edtGST.getValue())
            jsonBody.put("Address2", edtAddress2.getValue())
            jsonBody.put("ProposedDate", TimeStamp.formatDateFromString(edtPdate.getValue()))
            jsonBody.put("StartDate", TimeStamp.formatDateFromString(edtSdate.getValue()))
            jsonBody.put("EndDate", TimeStamp.formatDateFromString(edtEdate.getValue()))
            jsonBody.put("SiteType", siteType)


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
            .addLead(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
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