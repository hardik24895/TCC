package com.tcc.app.activity

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.AvailableEmployeeDataItem
import com.tcc.app.modal.AvailableEmployeeListModel
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.modal.QuotationItem
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_team_definition.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.util.*
import kotlin.collections.ArrayList


class AddTeamDefinitionActivity : BaseActivity() {
    var userTypeNameList: ArrayList<String> = ArrayList()

    var userTypeListArray: ArrayList<AvailableEmployeeDataItem> = ArrayList()
    var selectedArrayList: ArrayList<AvailableEmployeeDataItem> = ArrayList()
    var adapterUserType: ArrayAdapter<String>? = null
    var itemUserType: List<SearchableItem> = ArrayList()
    var userId: String = ""
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    var quotationItem: QuotationItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_team_definition)

        if (intent.hasExtra(Constant.DATA)) {
            quotationItem = intent.getSerializableExtra(Constant.DATA) as QuotationItem

        }
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.team_definition)


        edStartDate.setText(getCurrentDate())
        edEndDate.setText(getCurrentDate())


        getUserTypeList()
        userTypeSpinnerListner()
        userTypeViewClick()

        edStartDate.setOnClickListener {

            val dpd = DatePickerDialog(
                this@AddTeamDefinitionActivity,
                { view, year, monthOfYear, dayOfMonth ->

                    var selectedMonth: String = ""
                    var selectedDay: String = ""
                    if (dayOfMonth < 10) {
                        selectedDay = "0" + dayOfMonth
                    } else
                        selectedDay = dayOfMonth.toString()


                    if (month < 10) {
                        selectedMonth = "0" + (month + 1)
                    } else
                        selectedMonth = month.toString()

                    edStartDate.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
                    getUserTypeList()
                    userTypeSpinnerListner()
                    userTypeViewClick()
                },
                year,
                month,
                day
            )
            dpd.show()

        }

        edEndDate.setOnClickListener {

            val dpd = DatePickerDialog(
                this@AddTeamDefinitionActivity,
                { view, year, monthOfYear, dayOfMonth ->

                    var selectedMonth: String = ""
                    var selectedDay: String = ""
                    if (dayOfMonth < 10) {
                        selectedDay = "0" + dayOfMonth
                    } else
                        selectedDay = dayOfMonth.toString()


                    if (month < 10) {
                        selectedMonth = "0" + (month + 1)
                    } else
                        selectedMonth = month.toString()

                    edEndDate.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
                    getUserTypeList()
                    userTypeSpinnerListner()
                    userTypeViewClick()
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        rgType.setOnCheckedChangeListener({ group, checkedId ->
            getUserTypeList()
        })
        btnAddUser.setOnClickListener {

            var selectedPos = spUserType.selectedItemPosition - 1
            if (selectedPos > -1) {
                var userName = "${userTypeListArray.get(selectedPos).firstName} ${
                    userTypeListArray.get(selectedPos).lastName
                } - ${userTypeListArray.get(selectedPos).userType}"
                selectedArrayList.add(userTypeListArray.get(selectedPos))
                onAddField(userName)
                spUserType.setSelection(0)
            } else {
                root.showSnackBar("Please select Staff")
            }
        }
        btnSubmit.setOnClickListener { AddTeamDefinitionList() }

        edStartTime.setOnClickListener {
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.start_time)
            }.build().apply {
                setListener { hour, minute ->
                    Logger.d("time", hour.toString() + ":" + minute.toString())
                    var edStartTime1: EditText = findViewById(R.id.edStartTime)
                    edStartTime1.setText(
                        convertIntoTowDigit(hour) + ":" + convertIntoTowDigit(
                            minute
                        )
                    )

                }
            }.show(supportFragmentManager, "")

        }
        edEndTime.setOnClickListener {
            SnapTimePickerDialog.Builder().apply {
                setTitle(R.string.end_time)
            }.build().apply {
                setListener { hour, minute ->
                    Logger.d("time", hour.toString() + ":" + minute.toString())


                    var edEndTime1: EditText = findViewById(R.id.edEndTime)
                    edEndTime1.setText(convertIntoTowDigit(hour) + ":" + convertIntoTowDigit(minute))
                }
            }.show(supportFragmentManager, "")

        }
    }

    private fun AddTeamDefinitionList() {
        showProgressbar()
        var result = ""

        try {
            val jsonObj = JSONObject()
            val jsonArray = JSONArray()
            jsonObj.put("EmployeeID", userId)
            jsonArray.put(jsonObj)


            for (item in selectedArrayList.indices) {


                val jsonObj1 = JSONObject()
                jsonObj1.put("EmployeeID", selectedArrayList.get(item).userID)
                jsonArray.put(jsonObj1)
            }


            val jsonBody = JSONObject()

            var id: Int = rgType.checkedRadioButtonId
            val radio: RadioButton = findViewById(id)


            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("SitesID", quotationItem?.sitesID)
            jsonBody.put("QuotationID", quotationItem?.quotationID)
            jsonBody.put("Type", radio.text)
            jsonBody.put("StartDate", formatDateFromString(edStartDate.getValue()))
            jsonBody.put("EndDate", formatDateFromString(edEndDate.getValue()))
            jsonBody.put("StartTime", formatDateFromString(edStartTime.getValue()))
            jsonBody.put("EndTime", formatDateFromString(edEndTime.getValue()))
            jsonBody.put("UserList", jsonArray)

            result = Networking.setParentJsonData(Constant.METHOD_ADD_TEAM_DEFINITION, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddTeamDefinitionActivity)
            .getServices()
            .AddTeamDefinition(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        finish()
                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    showAlert(message)
                }

            }).addTo(autoDisposable)


    }

    fun onAddField(username: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.row_dynamic_user_team_definition, null, false)
        var btnClose: ImageView = rowView.findViewById(R.id.btnClose)
        var txtSelectUser: TextView = rowView.findViewById(R.id.txtSelectedUser)
        txtSelectUser.isSelected = true
        txtSelectUser.text = username

        btnClose.setOnClickListener {
            selectedArrayList.removeAt(linAddTeamDefinition.indexOfChild(rowView))
            linAddTeamDefinition.removeView(rowView)
        }

        linAddTeamDefinition!!.addView(rowView)
    }


    fun getUserTypeList() {

        if (!edStartDate.isEmpty() && !edEndDate.isEmpty()) {
            userTypeListArray.clear()
            userTypeNameList.clear()
            selectedArrayList.clear()
            var result = ""


            val rbType = findViewById<View>(rgType.getCheckedRadioButtonId()) as? RadioButton
            try {
                val jsonBody = JSONObject()
                jsonBody.put("StartDate", formatDateFromString(edStartDate.getValue()))
                jsonBody.put("EndDate", formatDateFromString(edEndDate.getValue()))
                jsonBody.put("Type", rbType?.text.toString())
                jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
                result =
                    Networking.setParentJsonData(Constant.METHOD_AVAILABLE_EMPLOYEE_LIST, jsonBody)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Networking
                .with(this@AddTeamDefinitionActivity)
                .getServices()
                .getAvailableEmployeeList(Networking.wrapParams(result))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackObserver<AvailableEmployeeListModel>() {
                    override fun onSuccess(response: AvailableEmployeeListModel) {

                        if (linAddTeamDefinition.childCount > 0) {
                            linAddTeamDefinition.removeAllViews()
                        }
                        userTypeListArray.addAll(response.data)
                        var myList: MutableList<SearchableItem> = mutableListOf()

                        userTypeNameList!!.add("Select Staff")
                        myList.add(SearchableItem(0, "Select Staff"))

                        for (items in response.data.indices) {
                            userTypeNameList.add(
                                response.data.get(items).firstName.toString() + " " + response.data.get(
                                    items
                                ).lastName.toString() + " - " + response.data.get(items).userType.toString()
                            )
                            myList.add(
                                SearchableItem(
                                    items.toLong() + 1,
                                    userTypeNameList.get(items + 1)
                                )
                            )

                        }
                        itemUserType = myList

                        adapterUserType = ArrayAdapter(
                            this@AddTeamDefinitionActivity,
                            R.layout.custom_spinner_item,
                            userTypeNameList
                        )
                        spUserType.setAdapter(adapterUserType)

                    }

                    override fun onFailed(code: Int, message: String) {

                        showAlert(message)

                    }

                }).addTo(autoDisposable)
        }

    }

    private fun userTypeViewClick() {

        view2.setOnClickListener {

            if (selectedArrayList.size == 0) {
                SearchableDialog(this@AddTeamDefinitionActivity, itemUserType!!,
                    getString(R.string.select_employee), { item, _ ->
                        spUserType.setSelection(item.id.toInt())
                    }).show()
            } else {
                var SelectedItemUserType: List<SearchableItem>? = null
                var tempuserTypeNameList: ArrayList<String> = ArrayList()
                var myList: MutableList<SearchableItem> = mutableListOf()

                myList.add(SearchableItem(0, "Select Staff"))

                for (items in userTypeListArray.indices) {
                    var isAdd: Boolean? = null

                    for (selectedpos in selectedArrayList) {

                        if (userTypeListArray.get(items).userID.equals(selectedpos.userID)) {
                            isAdd = false
                            break
                        } else {
                            isAdd = true
                        }


                    }
                    if (isAdd!!) {
                        tempuserTypeNameList.add(
                            userTypeListArray.get(items).firstName.toString() + " " + userTypeListArray.get(
                                items
                            ).lastName.toString() + " - " + userTypeListArray.get(items).userType.toString()
                        )
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                userTypeNameList.get(items + 1)
                            )
                        )
                    }

                }
                SelectedItemUserType = myList
                SearchableDialog(this@AddTeamDefinitionActivity, SelectedItemUserType!!,
                    getString(R.string.select_employee), { item, _ ->
                        spUserType.setSelection(item.id.toInt())
                    }).show()

            }


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
                if (position != -1 && userTypeListArray.size > position - 1) {
                    if (position == 0) {
                        userId = "-1"
                    } else {
                        userId = userTypeListArray.get(position).userID.toString()
                    }


                }

            }
        }
    }

}