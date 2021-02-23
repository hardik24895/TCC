package com.tcc.app.activity

import android.app.DatePickerDialog
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
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_invoice.*
import kotlinx.android.synthetic.main.row_dynamic_user.view.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import tech.hibk.searchablespinnerlibrary.SearchableSpinner
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class AddInvoiceActivity : BaseActivity() {

    // var compnyID: String = ""
    var quotationIteam: QuotationItem? = null
    var leadItem: LeadItem? = null


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

    var invoiceAttedanceList: ArrayList<InvoiceAttendanceDataItem> = ArrayList()

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
        setContentView(R.layout.activity_add_invoice)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.invoice)
        btnAddUser.setOnClickListener { onAddField() }

        if (intent.hasExtra(Constant.DATA)) {
            quotationIteam = intent.getSerializableExtra(Constant.DATA) as QuotationItem
        }
        if (intent.hasExtra(Constant.DATA1)) {
            leadItem = intent.getSerializableExtra(Constant.DATA1) as LeadItem
        }
        userTypeNameList = ArrayList()
        userTypeListArray = ArrayList()

        serviceNameList = ArrayList()
        serviceListArray = ArrayList()

        edtInvoiceDate.setText(getCurrentDate())
        edStartDate.setText(getCurrentDate())
        edEndDate.setText(getCurrentDate())
        edtInvoiceDate.setOnClickListener {
            showDateTimePicker(
                this@AddInvoiceActivity,
                edtInvoiceDate
            )
        }

        edStartDate.setOnClickListener {
            /*showDateTimePicker(
                this@AddInvoiceActivity,
                edStartDate
            )*/

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
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
                    getInvoiceAttedanceList()

                },
                year,
                month,
                day
            )
            dpd.show()
        }

        edEndDate.setOnClickListener {
            /*  showDateTimePicker(
                  this@AddInvoiceActivity,
                  edEndDate
              )*/
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
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
                    getInvoiceAttedanceList()

                },
                year,
                month,
                day
            )
            dpd.show()

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


        getUserTypeList()
        userTypeSpinnerListner()
        userTypeViewClick()

    }


    fun validation() {
        when {
            edtNote.isEmpty() -> {
                root.showSnackBar("Enter Notes")
                edtNote.requestFocus()
            }
            edtTerms.isEmpty() -> {
                root.showSnackBar("Enter Terms")
                edtTerms.requestFocus()
            }

            else -> {
                AddInvoice()
            }

        }
    }


    private fun userTypeViewClick() {

        view2.setOnClickListener {
            SearchableDialog(this@AddInvoiceActivity,
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
                    if (position != -1 && invoiceAttedanceList!!.size > position) {

                        for (iteam in invoiceAttedanceList.indices) {
                            if (userTypeListArray!!.get(position).usertypeID.toString()
                                    .equals(invoiceAttedanceList.get(iteam).usertypeID)
                            ) {
                                usertypeId = invoiceAttedanceList!!.get(iteam).usertypeID.toString()
                                edHSN.setText(invoiceAttedanceList!!.get(iteam).hSNNo)
                                edQty.setText(invoiceAttedanceList!!.get(iteam).qty)
                                edRate.setText(invoiceAttedanceList!!.get(iteam).rate)
                                setUpdatedTotal()
                            }
                        }
                    } else {
                        usertypeId = userTypeListArray!!.get(position).usertypeID.toString()
                        edHSN.setText(userTypeListArray!!.get(position).hSNNo)
                        edQty.setText("1")
                        edRate.setText(userTypeListArray!!.get(position).rate)
                        setUpdatedTotal()
                    }


                }


            }
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
            this@AddInvoiceActivity,
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

                    if (position != -1 && invoiceAttedanceList!!.size > position) {

                        for (iteam in invoiceAttedanceList.indices) {
                            if (userTypeListArray!!.get(position).usertypeID.toString()
                                    .equals(invoiceAttedanceList.get(iteam).usertypeID)
                            ) {
                                edHSNChild.setText(invoiceAttedanceList!!.get(position).hSNNo)
                                edQtyChild.setText(invoiceAttedanceList!!.get(position).qty)
                                edRateChild.setText(invoiceAttedanceList!!.get(position).rate)
                                setUpdatedTotal()
                            }
                        }
                    } else {
                        edHSNChild.setText(userTypeListArray!!.get(position).hSNNo)
                        edQtyChild.setText("1")
                        edRateChild.setText(userTypeListArray!!.get(position).rate)
                        setUpdatedTotal()
                    }

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
                this@AddInvoiceActivity,
                itemUserType!!,
                getString(R.string.select_usertype),
                { item, _ -> spUserTypeChild.setSelection(item.id.toInt()) }).show()
        }

        lin_add_user!!.addView(rowView)
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
            .with(this@AddInvoiceActivity)
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
                        this@AddInvoiceActivity,
                        R.layout.custom_spinner_item,
                        userTypeNameList!!
                    )
                    spUserType.setAdapter(adapterUserType)


                    if (quotationIteam?.isFixCost?.equals("No")!!) {
                        getInvoiceAttedanceList()
                    } else {
                        for (iteam in quotationIteam?.item!!.indices) {
                            if (iteam == 0) {
                                for (i in userTypeListArray!!.indices) {
                                    if (quotationIteam!!.item.get(iteam).usertypeID.equals(
                                            userTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {
                                        spUserType.setSelection(i)
                                    }
                                }
                            } else {
                                onAddField()
                                for (i in userTypeListArray!!.indices) {
                                    if (quotationIteam!!.item.get(iteam).usertypeID.equals(
                                            userTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {
                                        lin_add_user.getChildAt(iteam - 1).spUserTypeChild.setSelection(
                                            i
                                        )
                                    }
                                }
                            }

                        }
                    }


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }


    fun getInvoiceAttedanceList() {

        showProgressbar()
        invoiceAttedanceList.clear()
        lin_add_user.removeAllViews()
        //   setUpdatedTotal()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("QuotationID", quotationIteam?.quotationID)
            jsonBody.put("StartDate", formatDateFromString(edStartDate.getValue()))
            jsonBody.put("EndDate", formatDateFromString(edEndDate.getValue()))

            result =
                Networking.setParentJsonData(Constant.METHOD_GET_INVOICE_ATTEDANCE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddInvoiceActivity)
            .getServices()
            .getInvoiceAttedanceList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<InvoiceAttendanceListModal>() {
                override fun onSuccess(response: InvoiceAttendanceListModal) {
                    hideProgressbar()
                    val data = response.data
                    invoiceAttedanceList.addAll(data)
                    if (invoiceAttedanceList.size > 0) {
                        for (iteam in invoiceAttedanceList!!.indices) {
                            if (iteam == 0) {
                                for (i in userTypeListArray!!.indices) {
                                    if (invoiceAttedanceList.get(iteam).usertypeID.equals(
                                            userTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {

                                        usertypeId =
                                            invoiceAttedanceList!!.get(iteam).usertypeID.toString()
                                        edHSN.setText(invoiceAttedanceList!!.get(iteam).hSNNo)
                                        edQty.setText(invoiceAttedanceList!!.get(iteam).qty)
                                        edRate.setText(invoiceAttedanceList!!.get(iteam).rate)
                                        setUpdatedTotal()

                                        spUserType.setSelection(i)
                                    }


                                }
                            } else {
                                onAddField()
                                for (i in userTypeListArray!!.indices) {
                                    if (invoiceAttedanceList.get(iteam).usertypeID.equals(
                                            userTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {
                                        lin_add_user.getChildAt(iteam - 1).spUserTypeChild.setSelection(
                                            i
                                        )
                                        lin_add_user.getChildAt(iteam - 1).edHSNChild.setText(
                                            invoiceAttedanceList!!.get(iteam).hSNNo
                                        )
                                        lin_add_user.getChildAt(iteam - 1).edQtyChild.setText(
                                            invoiceAttedanceList!!.get(iteam).qty
                                        )
                                        lin_add_user.getChildAt(iteam - 1).edRateChild.setText(
                                            invoiceAttedanceList!!.get(iteam).rate
                                        )
                                        setUpdatedTotal()

                                    }
                                }
                            }

                        }
                    } else {
                        lin_add_user.removeAllViews()
                    }


                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
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

            if (quotationIteam?.stateID?.toInt() == 12) {
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


    fun AddInvoice() {
        var result = ""
        showProgressbar()
        var total = 0.0;
        if (quotationIteam?.stateID?.toInt() == 12) {
            total = edTotalAmount.getValue().toDouble() + edCGST.getValue()
                .toDouble() + edSGST.getValue().toDouble() + 0
        } else {
            total = edTotalAmount.getValue().toDouble() + 0 + 0 + edIGST.getValue().toDouble()
        }

        try {
            val jsonBody = JSONObject()
            val jsonArray = JSONArray()

            jsonBody.put("SitesID", quotationIteam?.sitesID)
            jsonBody.put("QuotationID", quotationIteam?.quotationID)
            //  jsonBody.put("CompanyID", compnyID)
            jsonBody.put("InvoiceDate", formatDateFromString(edtInvoiceDate.getValue()))
            jsonBody.put("StartDate", formatDateFromString(edStartDate.getValue()))
            jsonBody.put("EndDate", formatDateFromString(edEndDate.getValue()))
            jsonBody.put("SubTotal", edTotalAmount.getValue())
            jsonBody.put("TotalAmount", total)
            jsonBody.put("CGST", edCGST.getValue())
            jsonBody.put("SGST", edSGST.getValue())
            jsonBody.put("IGST", edIGST.getValue())
            jsonBody.put("Notes", edtNote.getValue())
            jsonBody.put("Terms", edtTerms.getValue())
            if (leadItem != null) {
                jsonBody.put("VisitorID", leadItem?.visitorID)
                jsonBody.put("CustomerID", leadItem?.customerID)
            } else {
                jsonBody.put("VisitorID", quotationIteam?.visitorID)
                jsonBody.put("CustomerID", quotationIteam?.customerID)
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




            result = Networking.setParentJsonData(Constant.METHOD_ADD_INVOICE, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddInvoiceActivity)
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