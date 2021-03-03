package com.tcc.app.activity

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.AvailableEmployeeDataItem
import com.tcc.app.modal.AvailableEmployeeListModel
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
import kotlinx.android.synthetic.main.row_dynamic_user_team_definition.view.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import tech.hibk.searchablespinnerlibrary.SearchableSpinner
import java.util.*
import kotlin.collections.ArrayList


class AddTeamDefinitionActivity : BaseActivity() {
    var userTypeNameList: ArrayList<String> = ArrayList()
    var userTypeListArray: ArrayList<AvailableEmployeeDataItem> = ArrayList()
    var adapterUserType: ArrayAdapter<String>? = null
    var itemUserType: List<SearchableItem>? = null
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
        btnAddUser.setOnClickListener { onAddField() }
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
        var selectuserCount = 0
        showProgressbar()
        var result = ""

        try {
            val jsonObj = JSONObject()
            val jsonArray = JSONArray()
            jsonObj.put("EmployeeID", userId)
            jsonArray.put(jsonObj)

            if (linAddTeamDefinition.childCount > 0) {
                for (item in 0 until linAddTeamDefinition.childCount) {
                    selectuserCount = 0
                    Log.d(
                        "TAG",
                        "AddTeamDefinitionList:  =   ${userTypeListArray.get(
                            linAddTeamDefinition.getChildAt(item).spUserTypeChild.selectedItemPosition
                        ).userID}"
                    )
                    for (i in userTypeListArray.indices) {
                        if (userTypeListArray.get(linAddTeamDefinition.getChildAt(item).spUserTypeChild.selectedItemPosition).userID == userTypeListArray.get(
                                i
                            ).userID
                        ) {
                            selectuserCount += 1
                            if (selectuserCount == 2) {
                                hideProgressbar()
                                root.showSnackBar("PLease Remove Duplicate Employee")
                                Log.d(
                                    "TAG",
                                    "AddTeamDefinitionList:  =   ${userTypeListArray.get(
                                        linAddTeamDefinition.getChildAt(item).spUserTypeChild.selectedItemPosition
                                    ).userID}  =   ${userTypeListArray.get(i).userID}"
                                )
                                return
                            }

                        } else {
                            Log.d(
                                "TAG",
                                "AddTeamDefinitionList:  =   ${userTypeListArray.get(
                                    linAddTeamDefinition.getChildAt(item).spUserTypeChild.selectedItemPosition
                                ).userID}  =!  ${userTypeListArray.get(i).userID}"
                            )

                        }

                        /* if(userTypeListArray.get(i).userID==userId){
                             hideProgressbar()
                             root.showSnackBar("PLease Remove Duplicate Employee")
                             return
                         }*/

                    }
                    val jsonObj1 = JSONObject()
                    jsonObj1.put(
                        "EmployeeID",
                        userTypeListArray.get(linAddTeamDefinition.getChildAt(item).spUserTypeChild.selectedItemPosition).userID.toString()
                    )
                    jsonArray.put(jsonObj1)
                }
            }

            /* if (linAddTeamDefinition.childCount > 0) {

                 for (i in 0 until  jsonArray.length()){
                     for (item in 0 until linAddTeamDefinition.childCount) {

                         if (userId==jsonArray.getJSONObject(i).getString("EmployeeID")){
                             root.showSnackBar("PLease Remove Duplicate Employee")
                             hideProgressbar()
                             return
                         }
                         if(userTypeListArray.get(linAddTeamDefinition.getChildAt(item).spUserTypeChild.selectedItemPosition).userID== jsonArray.getJSONObject(i).getString("EmployeeID")){
                            root.showSnackBar("PLease Remove Duplicate Employee")
                             hideProgressbar()
                            return
                        }

                     }
                 }


             }*/


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
//        Networking
//            .with(this@AddTeamDefinitionActivity)
//            .getServices()
//            .AddTeamDefinition(Networking.wrapParams(result))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
//                override fun onSuccess(response: CommonAddModal) {
//                           hideProgressbar()
//                    if (response.error == 200) {
//                        root.showSnackBar(response.message.toString())
//                        finish()
//                    } else {
//                        showAlert(response.message.toString())
//                    }
//
//                }
//
//                override fun onFailed(code: Int, message: String) {
//                    hideProgressbar()
//                    showAlert(message)
//                }
//
//            }).addTo(autoDisposable)
//

    }


    fun onAddField() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.row_dynamic_user_team_definition, null, false)
        var btnRemove: TextView = rowView.findViewById(R.id.btnRemove)
        var spUserTypeChild: SearchableSpinner = rowView.findViewById(R.id.spUserTypeChild)
        var viewChild: View = rowView.findViewById(R.id.viewChild)

        btnRemove.setOnClickListener {
            linAddTeamDefinition.removeView(rowView)
            setUpdatedTotal()
        }
        adapterUserType = ArrayAdapter(
            this@AddTeamDefinitionActivity,
            R.layout.custom_spinner_item,
            userTypeNameList
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
                if (position != -1 && userTypeListArray.size > position) {

                    setUpdatedTotal()
                }

            }
        }

        viewChild.setOnClickListener {
            SearchableDialog(
                this@AddTeamDefinitionActivity,
                itemUserType!!,
                getString(R.string.select_usertype),
                { item, _ -> spUserTypeChild.setSelection(item.id.toInt()) }).show()
        }

        linAddTeamDefinition!!.addView(rowView)
    }

    private fun setUpdatedTotal() {
        //TODO("Not yet implemented")
    }

    fun getUserTypeList() {

        if (!edStartDate.isEmpty() && !edEndDate.isEmpty()) {
            userTypeListArray.clear()
            userTypeNameList.clear()
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
                        myList.add(
                            SearchableItem(
                                0,
                                "Select Staff"
                            )
                        )

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
            SearchableDialog(this@AddTeamDefinitionActivity,
                itemUserType!!,
                getString(R.string.select_employee), { item, _ ->
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