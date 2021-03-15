package com.tcc.app.activity


import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp.formatDateFromString
import com.tcc.app.utils.TimeStamp.formatServerDateToLocal
import com.tcc.app.widgets.DecimalDigitsInputFilter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_invoice.*
import kotlinx.android.synthetic.main.activity_add_invoice.btnAddMaterial
import kotlinx.android.synthetic.main.activity_add_invoice.btnAddUser
import kotlinx.android.synthetic.main.activity_add_invoice.btnSubmit
import kotlinx.android.synthetic.main.activity_add_invoice.edCGST
import kotlinx.android.synthetic.main.activity_add_invoice.edHSN
import kotlinx.android.synthetic.main.activity_add_invoice.edIGST
import kotlinx.android.synthetic.main.activity_add_invoice.edMaterialHSN
import kotlinx.android.synthetic.main.activity_add_invoice.edMaterialQty
import kotlinx.android.synthetic.main.activity_add_invoice.edMaterialRate
import kotlinx.android.synthetic.main.activity_add_invoice.edQty
import kotlinx.android.synthetic.main.activity_add_invoice.edRate
import kotlinx.android.synthetic.main.activity_add_invoice.edSGST
import kotlinx.android.synthetic.main.activity_add_invoice.edSubTotalAmount
import kotlinx.android.synthetic.main.activity_add_invoice.edTotalAmount
import kotlinx.android.synthetic.main.activity_add_invoice.edtMaterialDays
import kotlinx.android.synthetic.main.activity_add_invoice.edtNote
import kotlinx.android.synthetic.main.activity_add_invoice.edtTerms
import kotlinx.android.synthetic.main.activity_add_invoice.lin_add_material
import kotlinx.android.synthetic.main.activity_add_invoice.lin_add_user
import kotlinx.android.synthetic.main.activity_add_invoice.root
import kotlinx.android.synthetic.main.activity_add_invoice.spMaterialType
import kotlinx.android.synthetic.main.activity_add_invoice.spUserType
import kotlinx.android.synthetic.main.activity_add_invoice.view2
import kotlinx.android.synthetic.main.activity_add_invoice.view7
import kotlinx.android.synthetic.main.activity_add_quotation.*
import kotlinx.android.synthetic.main.activity_add_quotation.tilCGST
import kotlinx.android.synthetic.main.activity_add_quotation.tilIGST
import kotlinx.android.synthetic.main.activity_add_quotation.tilSGST
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
    var usertypeId: String = "-1"
    var usertypeChildId: String = "-1"

    var materialTypeNameList: ArrayList<String>? = null
    var adapterMaterialType: ArrayAdapter<String>? = null
    var materialTypeListArray: ArrayList<UserTypeDataItem>? = null
    var itemMaterialType: List<SearchableItem>? = null
    var materialtypeId: String = "-1"

    var invoiceMaterialList: ArrayList<MaterialItemData> = ArrayList()
    var invoiceUserList: ArrayList<UserItemData> = ArrayList()

    var stateNameList: ArrayList<String> = ArrayList()
    var adapterState: ArrayAdapter<String>? = null
    var stateIteams: List<SearchableItem>? = null
    var stateID: String = ""
    var cityID: String = ""
    var cityNameList: ArrayList<String> = ArrayList()
    var cityListArray: ArrayList<CityDataItem> = ArrayList()
    var adapterCity: ArrayAdapter<String>? = null
    var cityIteams: List<SearchableItem>? = null

    var UserAmount: Float = 0f
    var MaterialAmount: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_invoice)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.invoice)



        edStartDate.setText(getCurrentDate())
        edEndDate.setText(getCurrentDate())
        btnAddUser.setOnClickListener { onAddField() }
        btnAddMaterial.setOnClickListener { onAddMaterial() }

        if (intent.hasExtra(Constant.DATA)) {
            quotationIteam = intent.getSerializableExtra(Constant.DATA) as QuotationItem

            edtNote.setText(quotationIteam?.note)
            edtTerms.setText(quotationIteam?.term)



            edStartDate.setText(formatServerDateToLocal(quotationIteam!!.startDate.toString()))
            edEndDate.setText(formatServerDateToLocal(quotationIteam!!.endDate.toString()))

        }
        if (intent.hasExtra(Constant.DATA1)) {
            leadItem = intent.getSerializableExtra(Constant.DATA1) as LeadItem
        }
        userTypeNameList = ArrayList()
        userTypeListArray = ArrayList()
        materialTypeListArray = ArrayList()
        materialTypeNameList = ArrayList()
        serviceNameList = ArrayList()
        serviceListArray = ArrayList()

        edtCompanyName.setText(quotationIteam?.companyName)
        edRate.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        edMaterialRate.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        edSubTotalAmount.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        edTotalAmount.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        edCGST.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        edSGST.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        edIGST.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        tilCGST.hint = "CGST ( ${session.configData.data?.cGST!!}% )"
        tilSGST.hint = "SGST ( ${session.configData.data?.sGST!!}% )"
        tilIGST.hint = "IGST ( ${session.configData.data?.iGST!!}% )"


        edtInvoiceDate.setText(getCurrentDate())

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


        edMaterialRate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpdatedTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        edMaterialQty.addTextChangedListener(object : TextWatcher {
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
        getMaterialTypeList()

        getUserTypeList()
        userTypeSpinnerListner()
        userTypeViewClick()

        materialTypeSpinnerListner()
        materialTypeViewClick()


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
            edTotalAmount.isEmpty() -> {
                root.showSnackBar("Select Staff")
                //  edTotalAmount.requestFocus()
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
                getString(R.string.staff_selection), { item, _ ->
                    spUserType.setSelection(item.id.toInt())
                }).show()
        }

    }

    private fun materialTypeViewClick() {

        view7.setOnClickListener {
            SearchableDialog(this@AddInvoiceActivity,
                itemMaterialType!!,
                getString(R.string.select_material), { item, _ ->
                    spMaterialType.setSelection(item.id.toInt())
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
                if (position != -1 && userTypeListArray!!.size > position - 1) {

                    if (position == 0) {
                        usertypeId = "-1"
                        edHSN.setText("0")
                        edQty.setText("0")
                        edRate.setText("0")

                    } else {
                        if (invoiceUserList!!.size > 0) {

                            for (iteam in invoiceUserList.indices) {
                                if (userTypeListArray!!.get(position - 1).usertypeID.toString()
                                        .equals(invoiceUserList.get(iteam).usertypeID)
                                ) {
                                    usertypeId = invoiceUserList!!.get(iteam).usertypeID.toString()
                                    edHSN.setText(invoiceUserList!!.get(iteam).hSNNo)
                                    edQty.setText(invoiceUserList!!.get(iteam).qty)
                                    edRate.setText(invoiceUserList!!.get(iteam).rate)

                                } else {
                                    usertypeId =
                                        userTypeListArray!!.get(position - 1).usertypeID.toString()
                                    edHSN.setText(userTypeListArray!!.get(position - 1).hSNNo)
                                    edQty.setText("1")
                                    edRate.setText(userTypeListArray!!.get(position - 1).rate)

                                    for (i in quotationIteam?.item?.user!!.indices) {
                                        if (userTypeListArray!!.get(position - 1).usertypeID.toString()
                                                .equals(quotationIteam?.item?.user!!.get(i)!!.usertypeID)


                                        ) {
                                            usertypeId =
                                                userTypeListArray!!.get(position - 1).usertypeID.toString()
                                            edHSN.setText(quotationIteam?.item?.user!!.get(i)!!.hSNNo)
                                            val qua =
                                                quotationIteam?.item?.user!!.get(i)!!.qty!!.toInt() * quotationIteam?.item?.user!!.get(
                                                    i
                                                )!!.days!!.toInt()
                                            edQty.setText(qua.toString())
                                            edRate.setText(quotationIteam?.item?.user!!.get(i)!!.rate)

                                        }
                                    }
                                }
                            }
                        } else {
                            usertypeId = userTypeListArray!!.get(position - 1).usertypeID.toString()
                            edHSN.setText(userTypeListArray!!.get(position - 1).hSNNo)
                            edQty.setText("1")
                            edRate.setText(userTypeListArray!!.get(position - 1).rate)

                            for (i in quotationIteam?.item?.user!!.indices) {
                                if (userTypeListArray!!.get(position - 1).usertypeID.toString()
                                        .equals(quotationIteam?.item?.user!!.get(i)!!.usertypeID)


                                ) {
                                    usertypeId =
                                        userTypeListArray!!.get(position - 1).usertypeID.toString()
                                    edHSN.setText(quotationIteam?.item?.user!!.get(i)!!.hSNNo)
                                    val qua =
                                        quotationIteam?.item?.user!!.get(i)!!.qty!!.toInt() * quotationIteam?.item?.user!!.get(
                                            i
                                        )!!.days!!.toInt()
                                    edQty.setText(qua.toString())
                                    edRate.setText(quotationIteam?.item?.user!!.get(i)!!.rate)

                                }
                            }

                        }
                    }
                    setUpdatedTotal()

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
        edRateChild.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        var edtChildDays: EditText = rowView.findViewById(R.id.edtChildDays)
        var txtUserTitle: TextView = rowView.findViewById(R.id.txtUserTitle)
        var til22: TextInputLayout = rowView.findViewById(R.id.til22)
        edtChildDays.setText("1")
        til22.invisible()
        txtUserTitle.setText(getString(R.string.staff))
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


                if (position != -1 && userTypeListArray!!.size > position - 1) {

                    if (position == 0) {
                        edHSNChild.setText("0")
                        edQtyChild.setText("0")
                        edRateChild.setText("0")
                        usertypeChildId = "-1"

                    } else {
                        if (invoiceUserList.size > 0) {
                            for (iteam in invoiceUserList.indices) {
                                if (userTypeListArray!!.get(position - 1).usertypeID.toString()
                                        .equals(invoiceUserList.get(iteam).usertypeID)
                                ) {
                                    //  usertypeChildId = invoiceUserList.get(iteam).usertypeID.toString()
                                    edHSNChild.setText(invoiceUserList.get(iteam).hSNNo)
                                    edQtyChild.setText(invoiceUserList.get(iteam).qty)
                                    edRateChild.setText(invoiceUserList.get(iteam).rate)

                                } else {
                                    // usertypeChildId = userTypeListArray!!.get(position - 1).usertypeID.toString()
                                    edHSNChild.setText(userTypeListArray!!.get(position - 1).hSNNo)
                                    edQtyChild.setText("1")
                                    edRateChild.setText(userTypeListArray!!.get(position - 1).rate)

                                    for (i in quotationIteam?.item?.user!!.indices) {
                                        if (userTypeListArray!!.get(position - 1).usertypeID.toString()
                                                .equals(quotationIteam?.item?.user!!.get(i)!!.usertypeID)


                                        ) {
                                            //  usertypeChildId =quotationIteam?.item?.user!!.get(i)!!.usertypeID.toString()
                                            edHSNChild.setText(quotationIteam?.item?.user!!.get(i)!!.hSNNo)
                                            val qua =
                                                quotationIteam?.item?.user!!.get(i)!!.qty!!.toInt() * quotationIteam?.item?.user!!.get(
                                                    i
                                                )!!.days!!.toInt()
                                            edQtyChild.setText(qua.toString())
                                            edRateChild.setText(quotationIteam?.item?.user!!.get(i)!!.rate)

                                        }


                                    }
                                }
                            }
                        } else {
                            //  usertypeChildId = userTypeListArray!!.get(position - 1).usertypeID.toString()
                            edHSNChild.setText(userTypeListArray!!.get(position - 1).hSNNo)
                            edQtyChild.setText("1")
                            edRateChild.setText(userTypeListArray!!.get(position - 1).rate)

                            for (i in quotationIteam?.item?.user!!.indices) {
                                if (userTypeListArray!!.get(position - 1).usertypeID.toString()
                                        .equals(quotationIteam?.item?.user!!.get(i)!!.usertypeID)


                                ) {
                                    //  usertypeChildId =quotationIteam?.item?.user!!.get(i)!!.usertypeID.toString()
                                    edHSNChild.setText(quotationIteam?.item?.user!!.get(i)!!.hSNNo)
                                    val qua =
                                        quotationIteam?.item?.user!!.get(i)!!.qty!!.toInt() * quotationIteam?.item?.user!!.get(
                                            i
                                        )!!.days!!.toInt()
                                    edQtyChild.setText(qua.toString())
                                    edRateChild.setText(quotationIteam?.item?.user!!.get(i)!!.rate)

                                }


                            }
                        }
                    }
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
                this@AddInvoiceActivity,
                itemUserType!!,
                getString(R.string.staff_selection),
                { item, _ -> spUserTypeChild.setSelection(item.id.toInt()) }).show()
        }

        lin_add_user!!.addView(rowView)
    }

    fun getUserTypeList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            if (quotationIteam != null)
                jsonBody.put("ServiceID", quotationIteam?.serviceID)

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
                    userTypeNameList!!.add(getString(R.string.staff_selection))
                    myList.add(
                        SearchableItem(
                            0,
                            getString(R.string.staff_selection)
                        )
                    )


                    for (items in response.data.indices) {
                        userTypeNameList!!.add(response.data.get(items).usertype.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                userTypeNameList!!.get(items + 1)
                            )
                        )

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
                        for (iteam in quotationIteam?.item!!.user!!.indices) {
                            if (iteam == 0) {
                                for (i in userTypeListArray!!.indices) {
                                    if (quotationIteam?.item!!.user!!.get(iteam)!!.usertypeID.equals(
                                            userTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {

                                        spUserType.setSelection(i + 1)
                                    }
                                }
                            } else {
                                onAddField()
                                for (i in userTypeListArray!!.indices) {
                                    if (quotationIteam?.item!!.user!!.get(iteam)!!.usertypeID.equals(
                                            userTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {
                                        lin_add_user.getChildAt(iteam - 1).spUserTypeChild.setSelection(
                                            i + 1
                                        )
                                    }
                                }
                            }

                        }
                    }


                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun getMaterialTypeList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("IsMaterial", "1")

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
                    materialTypeListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()

                    materialTypeNameList!!.add(getString(R.string.select_material))
                    myList.add(
                        SearchableItem(
                            0,
                            getString(R.string.select_material)
                        )
                    )

                    for (items in response.data.indices) {
                        materialTypeNameList!!.add(response.data.get(items).usertype.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                materialTypeNameList!!.get(items + 1)
                            )
                        )
                    }

                    itemMaterialType = myList
                    adapterMaterialType = ArrayAdapter(
                        this@AddInvoiceActivity,
                        R.layout.custom_spinner_item,
                        materialTypeNameList!!
                    )
                    spMaterialType.setAdapter(adapterMaterialType)

                    for (iteam in quotationIteam?.item!!.material!!.indices) {
                        if (iteam == 0) {
                            for (i in materialTypeListArray!!.indices) {
                                if (quotationIteam?.item!!.material!!.get(iteam)!!.usertypeID.equals(
                                        materialTypeListArray!!.get(i).usertypeID
                                    )
                                ) {
                                    spMaterialType.setSelection(i + 1)
                                }
                            }
                        } else {
                            onAddMaterial()
                            for (i in materialTypeListArray!!.indices) {
                                if (quotationIteam?.item!!.material!!.get(iteam)!!.usertypeID.equals(
                                        materialTypeListArray!!.get(i).usertypeID
                                    )
                                ) {
                                    lin_add_material.getChildAt(iteam - 1).spUserTypeChild.setSelection(
                                        i + 1
                                    )
                                }
                            }
                        }

                    }


                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    private fun materialTypeSpinnerListner() {
        spMaterialType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && materialTypeListArray!!.size > position - 1) {
                    if (position == 0) {
                        materialtypeId = "-1"
                        edMaterialHSN.setText("0")
                        edMaterialQty.setText("0")
                        edMaterialRate.setText("0")
                        //edtMaterialDays.setText("0")

                    } else {
                        if (invoiceMaterialList.size > 0) {
                            for (iteam in invoiceMaterialList.indices) {
                                if (materialTypeListArray!!.get(position - 1).usertypeID.toString()
                                        .equals(invoiceMaterialList.get(iteam).usertypeID)
                                ) {
                                    materialtypeId =
                                        invoiceMaterialList!!.get(iteam).usertypeID.toString()
                                    edMaterialHSN.setText(invoiceMaterialList!!.get(iteam).hSNNo)
                                    edMaterialQty.setText(invoiceMaterialList!!.get(iteam).qty)
                                    edMaterialRate.setText(invoiceMaterialList!!.get(iteam).rate)

                                } else {
                                    materialtypeId =
                                        materialTypeListArray!!.get(position - 1).usertypeID.toString()
                                    edMaterialHSN.setText(materialTypeListArray!!.get(position - 1).hSNNo)
                                    edMaterialQty.setText("1")
                                    edMaterialRate.setText(materialTypeListArray!!.get(position - 1).rate)

                                    for (i in quotationIteam?.item?.material!!.indices) {
                                        if (materialTypeListArray!!.get(position - 1).usertypeID.toString()
                                                .equals(quotationIteam?.item?.material!!.get(i)!!.usertypeID)


                                        ) {
                                            materialtypeId =
                                                quotationIteam?.item?.material!!.get(i)!!.usertypeID.toString()
                                            edMaterialHSN.setText(
                                                quotationIteam?.item?.material!!.get(
                                                    i
                                                )!!.hSNNo
                                            )

                                            val qua =
                                                quotationIteam?.item?.material!!.get(i)!!.qty!!.toInt() * quotationIteam?.item?.material!!.get(
                                                    i
                                                )!!.days!!.toInt()
                                            edMaterialQty.setText(qua.toString())
                                            edMaterialRate.setText(
                                                quotationIteam?.item?.material!!.get(
                                                    i
                                                )!!.rate
                                            )

                                        }
                                    }
                                }
                            }
                        } else {
                            materialtypeId =
                                materialTypeListArray!!.get(position - 1).usertypeID.toString()
                            edMaterialHSN.setText(materialTypeListArray!!.get(position - 1).hSNNo)
                            edMaterialQty.setText("1")
                            edMaterialRate.setText(materialTypeListArray!!.get(position - 1).rate)

                            for (i in quotationIteam?.item?.material!!.indices) {
                                if (materialTypeListArray!!.get(position - 1).usertypeID.toString()
                                        .equals(quotationIteam?.item?.material!!.get(i)!!.usertypeID)


                                ) {
                                    materialtypeId =
                                        quotationIteam?.item?.material!!.get(i)!!.usertypeID.toString()
                                    edMaterialHSN.setText(quotationIteam?.item?.material!!.get(i)!!.hSNNo)

                                    val qua =
                                        quotationIteam?.item?.material!!.get(i)!!.qty!!.toInt() * quotationIteam?.item?.material!!.get(
                                            i
                                        )!!.days!!.toInt()
                                    edMaterialQty.setText(qua.toString())
                                    edMaterialRate.setText(quotationIteam?.item?.material!!.get(i)!!.rate)
                                }
                            }
                        }

                    }
                    setUpdatedTotal()

                }

            }
        }
    }

    fun onAddMaterial() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.row_dynamic_user, null, false)
        var btnRemove: TextView = rowView.findViewById(R.id.btnRemoveUser)
        var txtUserTitle: TextView = rowView.findViewById(R.id.txtUserTitle)
        var spUserTypeChild: SearchableSpinner = rowView.findViewById(R.id.spUserTypeChild)
        var viewChild: View = rowView.findViewById(R.id.viewUserTypeChild)
        var edHSNChild: EditText = rowView.findViewById(R.id.edHSNChild)
        var edQtyChild: EditText = rowView.findViewById(R.id.edQtyChild)
        var edRateChild: EditText = rowView.findViewById(R.id.edRateChild)
        edRateChild.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        var edDaysChild: EditText = rowView.findViewById(R.id.edtChildDays)
        var til22: TextInputLayout = rowView.findViewById(R.id.til22)
        edDaysChild.setText("1")
        til22.invisible()
        edDaysChild.invisible()
        txtUserTitle.setText("Material")
        // userTypeChildId.add("")
        // Log.e("TAG", "onAddField:      "+userTypeChildId.size )


        btnRemove.setOnClickListener {
            lin_add_material.removeView(rowView)
            //     userTypeChildId.removeAt((lin_add_user as ViewGroup).indexOfChild(btnRemove))
            setUpdatedTotal()
        }
        adapterMaterialType = ArrayAdapter(
            this@AddInvoiceActivity,
            R.layout.custom_spinner_item,
            materialTypeNameList!!
        )
        spUserTypeChild.setAdapter(adapterMaterialType)


        spUserTypeChild.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && materialTypeListArray!!.size > position - 1) {
                    if (position == 0) {
                        edHSNChild.setText("0")
                        edQtyChild.setText("0")
                        edRateChild.setText("0")
                        //edDaysChild.setText("0")

                    } else {
                        if (invoiceMaterialList!!.size > 0) {

                            for (iteam in invoiceMaterialList.indices) {
                                if (materialTypeListArray!!.get(position - 1).usertypeID.toString()
                                        .equals(invoiceMaterialList.get(iteam).usertypeID)
                                ) {
                                    edHSNChild.setText(invoiceMaterialList!!.get(iteam).hSNNo)
                                    edQtyChild.setText(invoiceMaterialList!!.get(iteam).qty)
                                    edRateChild.setText(invoiceMaterialList!!.get(iteam).rate)

                                } else {
                                    edHSNChild.setText(materialTypeListArray!!.get(position - 1).hSNNo)
                                    edQtyChild.setText("1")
                                    edRateChild.setText(materialTypeListArray!!.get(position - 1).rate)

                                    for (i in quotationIteam?.item?.material!!.indices) {
                                        if (materialTypeListArray!!.get(position - 1).usertypeID.toString()
                                                .equals(quotationIteam?.item?.material!!.get(i)!!.usertypeID)


                                        ) {

                                            edHSNChild.setText(
                                                quotationIteam?.item?.material!!.get(
                                                    i
                                                )!!.hSNNo
                                            )

                                            val qua =
                                                quotationIteam?.item?.material!!.get(i)!!.qty!!.toInt() * quotationIteam?.item?.material!!.get(
                                                    i
                                                )!!.days!!.toInt()
                                            edQtyChild.setText(qua.toString())
                                            edRateChild.setText(
                                                quotationIteam?.item?.material!!.get(
                                                    i
                                                )!!.rate
                                            )

                                        }
                                    }
                                }
                            }
                        } else {

                            edHSNChild.setText(materialTypeListArray!!.get(position - 1).hSNNo)
                            edQtyChild.setText("1")
                            edRateChild.setText(materialTypeListArray!!.get(position - 1).rate)

                            for (i in quotationIteam?.item?.material!!.indices) {
                                if (materialTypeListArray!!.get(position - 1).usertypeID.toString()
                                        .equals(quotationIteam?.item?.material!!.get(i)!!.usertypeID)


                                ) {

                                    edHSNChild.setText(quotationIteam?.item?.material!!.get(i)!!.hSNNo)

                                    val qua =
                                        quotationIteam?.item?.material!!.get(i)!!.qty!!.toInt() * quotationIteam?.item?.material!!.get(
                                            i
                                        )!!.days!!.toInt()
                                    edQtyChild.setText(qua.toString())
                                    edRateChild.setText(quotationIteam?.item?.material!!.get(i)!!.rate)

                                }
                            }

                        }

                    }

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
                this@AddInvoiceActivity,
                itemMaterialType!!,
                getString(R.string.select_material),
                { item, _ -> spUserTypeChild.setSelection(item.id.toInt()) }).show()
        }

        lin_add_material!!.addView(rowView)
    }

    fun getInvoiceAttedanceList() {

        showProgressbar()
        invoiceMaterialList.clear()
        invoiceUserList.clear()
        lin_add_user.removeAllViews()
        //   setUpdatedTotal()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("QuotationID", quotationIteam?.quotationID)
            jsonBody.put("StartDate", formatDateFromString(edStartDate.getValue()))
            jsonBody.put("EndDate", formatDateFromString(edEndDate.getValue()))
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
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
            .subscribeWith(object : CallbackObserver<InVoiceAttendanceListModal>() {
                override fun onSuccess(response: InVoiceAttendanceListModal) {
                    hideProgressbar()
                    val data = response.data
                    if (data != null) {
                        invoiceMaterialList.addAll(data.material)
                        invoiceUserList.addAll(data.user)
                    }

                    if (invoiceUserList.size > 0) {
                        for (iteam in invoiceUserList.indices) {
                            if (iteam == 0) {
                                for (i in userTypeListArray!!.indices) {
                                    if (invoiceUserList.get(iteam).usertypeID.equals(
                                            userTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {
                                        edHSN.setText(invoiceUserList.get(iteam).hSNNo)
                                        edQty.setText(invoiceUserList.get(iteam).qty)
                                        edRate.setText(invoiceUserList.get(iteam).rate)
                                        setUpdatedTotal()
                                        spUserType.setSelection(i + 1)
                                    }


                                }
                            } else {
                                onAddField()
                                for (i in userTypeListArray!!.indices) {
                                    if (invoiceUserList.get(iteam).usertypeID.equals(
                                            userTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {
                                        lin_add_user.getChildAt(iteam - 1).spUserTypeChild.setSelection(
                                            i + 1
                                        )


                                    }
                                }
                            }

                        }
                    } else {
                        lin_add_user.removeAllViews()
                    }


                    if (invoiceMaterialList.size > 0) {
                        for (iteam in invoiceMaterialList!!.indices) {
                            if (iteam == 0) {
                                for (i in invoiceMaterialList!!.indices) {
                                    if (invoiceMaterialList.get(iteam).usertypeID.equals(
                                            materialTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {

                                        edMaterialHSN.setText(invoiceMaterialList!!.get(iteam).hSNNo)
                                        edMaterialQty.setText(invoiceMaterialList!!.get(iteam).qty)
                                        edMaterialRate.setText(invoiceMaterialList!!.get(iteam).rate)
                                        setUpdatedTotal()

                                        spMaterialType.setSelection(i + 1)
                                    }


                                }
                            } else {
                                onAddMaterial()
                                for (i in materialTypeListArray!!.indices) {
                                    if (invoiceMaterialList.get(iteam).usertypeID.equals(
                                            materialTypeListArray!!.get(i).usertypeID
                                        )
                                    ) {
                                        lin_add_material.getChildAt(iteam - 1).spUserTypeChild.setSelection(
                                            i + 1
                                        )


                                    }
                                }
                            }

                        }
                    } else {
                        lin_add_material.removeAllViews()
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun setUpdatedTotal() {
        UserAmount = 0f
        var df = DecimalFormat("##.##")
        var TotalAmount: Float = 0f
        var CGST: Float = 0f
        var SGST: Float = 0f
        var IGST: Float = 0f

        if (!edQty.isEmpty() && !edRate.isEmpty()) {
            TotalAmount = TotalAmount + (edQty.getValue().toFloat() * edRate.getValue().toFloat())
        }

        if (lin_add_user.childCount > 0) {
            for (item in 0 until lin_add_user.childCount) {
                if (!lin_add_user.getChildAt(item).edQtyChild.isEmpty()
                    && !lin_add_user.getChildAt(item).edRateChild.isEmpty()
                ) {
                    TotalAmount = TotalAmount + (lin_add_user.getChildAt(item).edQtyChild.getValue()
                        .toFloat() * lin_add_user.getChildAt(item).edRateChild.getValue().toFloat())

                }

            }
        }



        if (!edMaterialQty.isEmpty() && !edMaterialRate.isEmpty() && !edtMaterialDays.isEmpty()) {

            TotalAmount =
                TotalAmount + (edMaterialQty.getValue().toFloat() * edMaterialRate.getValue()
                    .toFloat()) * edtMaterialDays.getValue().toFloat()

        }

        if (lin_add_material.childCount > 0) {
            for (item in 0 until lin_add_material.childCount) {
                if (!lin_add_material.getChildAt(item).edQtyChild.isEmpty()
                    && !lin_add_material.getChildAt(item).edRateChild.isEmpty()
                ) {
                    TotalAmount =
                        TotalAmount + (lin_add_material.getChildAt(item).edQtyChild.getValue()
                            .toFloat() * lin_add_material.getChildAt(item).edRateChild.getValue()
                            .toFloat() * lin_add_material.getChildAt(item).edtChildDays.getValue()
                            .toFloat())

                }

            }
        }



        if (TotalAmount == 0f) {
            edSubTotalAmount.setText("0")
            edTotalAmount.setText("0")
            edCGST.setText("0")
            edSGST.setText("0")
            edIGST.setText("0")
        } else {

            edSubTotalAmount.setText(df.format(TotalAmount))

            CGST = CGST + ((TotalAmount * session.configData.data?.cGST!!.toFloat()) / 100)
            SGST = SGST + ((TotalAmount * session.configData.data?.sGST!!.toFloat()) / 100)
            IGST = IGST + ((TotalAmount * session.configData.data?.iGST!!.toFloat()) / 100)

            edTotalAmount.setText(df.format(TotalAmount + CGST + SGST + IGST))


            if (!quotationIteam?.companyID.equals("1")) {
                CGST = 0f
                SGST = 0f
                IGST = 0f
                tilIGST.invisible()
                tilCGST.invisible()
                tilSGST.invisible()
            }
            if (quotationIteam?.stateID?.toInt() == 12) {
                edCGST.setText(df.format(CGST))
                edSGST.setText(df.format(SGST))
                edIGST.setText("0")
                edTotalAmount.setText(df.format(TotalAmount + CGST + SGST))
            } else {
                edCGST.setText("0")
                edSGST.setText("0")
                edIGST.setText(df.format(IGST))
                edTotalAmount.setText(df.format(TotalAmount + IGST))
            }
        }
    }


    fun AddInvoice() {
        var result = ""
        showProgressbar()
        var total = 0.0;
        val jsonBody = JSONObject()
        val jsonArray = JSONArray()
        val jsonArray1 = JSONArray()


        try {

            jsonBody.put("SitesID", quotationIteam?.sitesID)
            jsonBody.put("QuotationID", quotationIteam?.quotationID)
            //  jsonBody.put("CompanyID", compnyID)
            jsonBody.put("InvoiceDate", formatDateFromString(edtInvoiceDate.getValue()))
            jsonBody.put("StartDate", formatDateFromString(edStartDate.getValue()))
            jsonBody.put("EndDate", formatDateFromString(edEndDate.getValue()))
            jsonBody.put("SubTotal", edSubTotalAmount.getValue())
            jsonBody.put("TotalAmount", edTotalAmount.getValue())
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

            if (!edQty.isEmpty() && !edQty.getValue()
                    .equals("0") && !edRate.isEmpty() && !edRate.getValue()
                    .equals("0") && usertypeId != "-1"
            ) {
                val jsonObj = JSONObject()
                jsonObj.put("UsertypeID", usertypeId)
                jsonObj.put("Qty", edQty.getValue())
                jsonObj.put("Rate", edRate.getValue())
                jsonArray1.put(jsonObj)
                jsonArray.put(jsonObj)
            }
            if (!edMaterialQty.isEmpty() && !edMaterialQty.getValue()
                    .equals("0") && !edMaterialRate.isEmpty() && !edMaterialRate.getValue()
                    .equals("0") && materialtypeId != "-1"
            ) {
                val jsonObj = JSONObject()
                jsonObj.put("UsertypeID", materialtypeId)
                jsonObj.put("Qty", edMaterialQty.getValue())
                jsonObj.put("Rate", edMaterialRate.getValue())
                jsonObj.put("Days", edtMaterialDays.getValue())
                jsonArray.put(jsonObj)
            }

            if (lin_add_user.childCount > 0) {
                for (item in 0 until lin_add_user.childCount) {


                    if (!lin_add_user.getChildAt(item).edQtyChild.isEmpty()
                        && !lin_add_user.getChildAt(item).edQtyChild.getValue().equals("0")
                        && !lin_add_user.getChildAt(item).edRateChild.isEmpty()
                        && !lin_add_user.getChildAt(item).edRateChild.getValue().equals("0")

                        && lin_add_user.getChildAt(item).spUserTypeChild.selectedItemPosition != 0
                    ) {

                        try {
                            val jsonObj = JSONObject()
                            jsonObj.put(
                                "UsertypeID",
                                userTypeListArray?.get(lin_add_user.getChildAt(item).spUserTypeChild.selectedItemPosition - 1)?.usertypeID
                            )
                            jsonObj.put("Qty", lin_add_user.getChildAt(item).edQtyChild.getValue())
                            jsonObj.put(
                                "Rate", lin_add_user.getChildAt(item).edRateChild.getValue()
                            )
                            jsonArray.put(jsonObj)
                            jsonArray1.put(jsonObj)
                        } catch (e: Exception) {

                        }


                    }

                }
            }

            if (lin_add_material.childCount > 0) {
                for (item in 0 until lin_add_material.childCount) {
                    if (!lin_add_material.getChildAt(item).edQtyChild.isEmpty()
                        && !lin_add_material.getChildAt(item).edQtyChild.getValue().equals("0")
                        && !lin_add_material.getChildAt(item).edRateChild.isEmpty()
                        && !lin_add_material.getChildAt(item).edRateChild.getValue().equals("0")

                    ) {
                        try {
                            val jsonObj = JSONObject()
                            jsonObj.put(
                                "UsertypeID",
                                materialTypeListArray?.get(lin_add_material.getChildAt(item).spUserTypeChild.selectedItemPosition - 1)?.usertypeID
                            )
                            jsonObj.put(
                                "Qty",
                                lin_add_material.getChildAt(item).edQtyChild.getValue()
                            )
                            jsonObj.put(
                                "Rate",
                                lin_add_material.getChildAt(item).edRateChild.getValue()
                            )
                            jsonObj.put(
                                "Days",
                                lin_add_material.getChildAt(item).edtChildDays.getValue()
                            )
                            jsonArray.put(jsonObj)
                        } catch (e: Exception) {

                        }


                    }

                }
            }

            Logger.d("Result", jsonArray.toString())

            result = Networking.setParentJsonData(Constant.METHOD_ADD_INVOICE, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        if (jsonArray1.length() == 0) {
            hideProgressbar()
            root.showSnackBar("staff not selected....")
            return
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