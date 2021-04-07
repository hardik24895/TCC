package com.tcc.app.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.fragment.QuotationFragment
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.TimeStamp.formatDateFromString
import com.tcc.app.widgets.DecimalDigitsInputFilter
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
    var usertypeId: String = "-1"


    var materialTypeNameList: ArrayList<String>? = null
    var adapterMaterialType: ArrayAdapter<String>? = null
    var materialTypeListArray: ArrayList<UserTypeDataItem>? = null
    var itemMaterialType: List<SearchableItem>? = null
    var materialtypeId: String = "-1"

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

    var serviceName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_quotation)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Quotation"

        edtDays.setText("1")
        til22.invisible()
        edtDays.invisible()

        btnAddUser.setOnClickListener { onAddField() }

        btnAddMaterial.setOnClickListener { onAddMaterial() }

        if (intent.hasExtra(Constant.DATA)) {
            siteListItem = intent.getSerializableExtra(Constant.DATA) as SiteListItem


            edAddress1.setText(siteListItem?.address.toString())
            edAddress2.setText(siteListItem?.address2.toString())
            edPincode.setText(siteListItem?.pinCode.toString())

            serviceName = siteListItem!!.serviceName.toString()

//            if (serviceName.equals("Deep cleaning")) {
//                txtUserTitle.setText("Service Item")
//                btnAddUser.setText("Add Service Item")
//
//            } else {
            txtUserTitle.setText("Item")
            btnAddUser.setText("ADD Item")
            //         }


            txtService.text = serviceName
            serviceId = siteListItem!!.serviceID.toString()

            tilIGST.invisible()
            tilCGST.invisible()
            tilSGST.invisible()
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


        edEstimationDate.setText(getCurrentDate())
        edEstimationDate.setOnClickListener {
            showDateTimePicker(
                this@AddQuotationActivity,
                edEstimationDate
            )
        }

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

        edtDays.addTextChangedListener(object : TextWatcher {
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

        edtMaterialDays.addTextChangedListener(object : TextWatcher {
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

        getNotes()
        getCompanyList()
        //   getServiceList()
        getUserTypeList(siteListItem?.serviceID.toString())
        getMaterialTypeList()
        getStateSppinerData()
        getCityList(siteListItem!!.stateID.toString())

        companySpinnerListner()
        userTypeSpinnerListner()
        stateSpinnerListner()
        citySpinnerListner()
        materialTypeSpinnerListner()

        compnyViewClick()
        userTypeViewClick()
        materialTypeViewClick()
        stateViewClick()
        cityViewClick()
    }


    fun validation() {
        when {
            /*  edtDays.isEmpty() -> {
                  root.showSnackBar("Enter Days")
                  edtDays.requestFocus()
              }*/

            compnyID == "-1" -> {
                root.showSnackBar("Select Company")
                spCompany.requestFocus()
            }

            edAddress1.isEmpty() -> {
                root.showSnackBar("Enter Address 1")
                edAddress1.requestFocus()
            }
            edAddress2.isEmpty() -> {
                root.showSnackBar("Enter Address 2")
                edAddress2.requestFocus()
            }
            !edPincode.isEmpty() && edPincode.getValue().length < 6 -> {
                root.showSnackBar("Enter Valid Pincode")
                edPincode.requestFocus()
            }
            serviceId == "-1" -> {
                root.showSnackBar("Select Service")
                spService.requestFocus()
            }

            cityID == "-1" -> {
                root.showSnackBar("City Not Found")
            }
            else -> {
                AddQuotation()
            }

        }
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


//            if (serviceName.equals("Deep cleaning"))
//                SearchableDialog(this@AddQuotationActivity, itemUserType!!, "Select Service Item",
//                    { item, _ ->
//                        spUserType.setSelection(item.id.toInt())
//
//                    }).show()
//            else
            SearchableDialog(this@AddQuotationActivity,
                itemUserType!!,
                getString(R.string.select_item),
                { item, _ ->
                    spUserType.setSelection(item.id.toInt())

                }).show()

        }

    }

    private fun materialTypeViewClick() {

        view7.setOnClickListener {
            SearchableDialog(this@AddQuotationActivity,
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
                        // edtDays.setText("0")

                    } else {
                        usertypeId = userTypeListArray!!.get(position - 1).usertypeID.toString()
                        edHSN.setText(userTypeListArray!!.get(position - 1).hSNNo)
                        edQty.setText("1")
                        edRate.setText(userTypeListArray!!.get(position - 1).rate)

                    }

                }
                setUpdatedTotal()
            }
        }
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
                        // edtDays.setText("0")

                    } else {
                        materialtypeId =
                            materialTypeListArray!!.get(position - 1).usertypeID.toString()
                        edMaterialHSN.setText(materialTypeListArray!!.get(position - 1).hSNNo)
                        edMaterialQty.setText("1")
                        edMaterialRate.setText(materialTypeListArray!!.get(position - 1).rate)

                    }

                }

                setUpdatedTotal()
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
        var txtItemTotalAmount: TextView = rowView.findViewById(R.id.txtItemTotalAmount)
        edRateChild.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        var edDaysChild: EditText = rowView.findViewById(R.id.edtChildDays)
        var txtUserTitleChild: TextView = rowView.findViewById(R.id.txtUserTitle)

//        if (serviceName.equals("Deep cleaning")) {
//            txtUserTitleChild.setText("Service Item")
//        } else {
        txtUserTitleChild.setText("Item")
//        }


        edDaysChild.setText("1")

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
                if (position != -1 && userTypeListArray!!.size > position - 1) {

                    if (position == 0) {
                        edHSNChild.setText("0")
                        edQtyChild.setText("0")
                        edRateChild.setText("0")
                        edDaysChild.setText("1")

                    } else {
                        edHSNChild.setText(userTypeListArray!!.get(position - 1).hSNNo)
                        edQtyChild.setText("1")
                        edRateChild.setText(userTypeListArray!!.get(position - 1).rate)

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

        /* edDaysChild.addTextChangedListener(object : TextWatcher {
             override fun afterTextChanged(s: Editable?) {
                 setUpdatedTotal()
             }

             override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
             }

             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             }
         })*/


        viewChild.setOnClickListener {

//            if (serviceName.equals("Deep cleaning"))
//
//                SearchableDialog(
//                    this@AddQuotationActivity,
//                    itemUserType!!,
//                    "Select Service Item",
//                    { item, _ -> spUserTypeChild.setSelection(item.id.toInt()) }).show()
//            else
            SearchableDialog(
                this@AddQuotationActivity,
                itemUserType!!,
                getString(R.string.select_item),
                { item, _ -> spUserTypeChild.setSelection(item.id.toInt()) }).show()
        }
        edDaysChild.setText("1")
        lin_add_user!!.addView(rowView)
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
        var txtMaterialAmount: TextView = rowView.findViewById(R.id.txtItemTotalAmount)
        edRateChild.setFilters(arrayOf<InputFilter>(DecimalDigitsInputFilter(8, 2)))
        var edDaysChild: EditText = rowView.findViewById(R.id.edtChildDays)
        var til22: TextInputLayout = rowView.findViewById(R.id.til22)
        edDaysChild.setText("1")
        til22.invisible()
        txtUserTitle.setText("Material")
        // userTypeChildId.add("")
        // Log.e("TAG", "onAddField:      "+userTypeChildId.size )


        btnRemove.setOnClickListener {
            lin_add_material.removeView(rowView)
            //     userTypeChildId.removeAt((lin_add_user as ViewGroup).indexOfChild(btnRemove))
            setUpdatedTotal()
        }
        adapterMaterialType = ArrayAdapter(
            this@AddQuotationActivity,
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
                        edDaysChild.setText("0")

                    } else {
                        edHSNChild.setText(materialTypeListArray!!.get(position - 1).hSNNo)
                        edQtyChild.setText("1")
                        edDaysChild.setText("1")
                        edRateChild.setText(materialTypeListArray!!.get(position - 1).rate)

                    }

                    setUpdatedTotal()

                }

            }
        }

        edRateChild.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpdatedTotal()

                if (!edQtyChild.isEmpty() && !edRateChild.isEmpty()) {
                    txtMaterialAmount.text = getString(R.string.RS) + " " + (edQtyChild.getValue()
                        .toFloat() * edRateChild.getValue().toFloat()).toString()
                } else {
                    txtItemTotalAmount.text = getString(R.string.RS) + " 0"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        edQtyChild.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpdatedTotal()
                if (!edQtyChild.isEmpty() && !edRateChild.isEmpty()) {
                    txtMaterialAmount.text = getString(R.string.RS) + " " + (edQtyChild.getValue()
                        .toFloat() * edRateChild.getValue().toFloat()).toString()
                } else {
                    txtItemTotalAmount.text = getString(R.string.RS) + " 0"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        /*   edDaysChild.addTextChangedListener(object : TextWatcher {
               override fun afterTextChanged(s: Editable?) {
                   setMaterialTotal()
               }

               override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
               }

               override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               }
           })*/


        viewChild.setOnClickListener {
            SearchableDialog(
                this@AddQuotationActivity,
                itemMaterialType!!,
                getString(R.string.select_material),
                { item, _ -> spUserTypeChild.setSelection(item.id.toInt()) }).show()
        }
        edDaysChild.setText("1")
        lin_add_material!!.addView(rowView)
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
                if (position != -1 && companyListArray.size > position - 1) {
                    if (position == 0) {
                        compnyID = "-1"
                    } else {
                        compnyID = companyListArray.get(position - 1).companyID.toString()
                        Logger.d("companyID", compnyID)


                        if (compnyID.equals("1")) {
                            if (siteListItem!!.stateID.equals("12")) {
                                tilCGST.visible()
                                tilSGST.visible()
                                tilIGST.invisible()
                            } else {
                                tilCGST.invisible()
                                tilSGST.invisible()
                                tilIGST.visible()
                            }

                        } else {
                            tilCGST.invisible()
                            tilSGST.invisible()
                            tilIGST.invisible()
                        }

                        setUpdatedTotal()
                    }

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

                    companyNameList!!.add(getString(R.string.select_company))
                    myList.add(
                        SearchableItem(
                            0,
                            getString(R.string.select_company)
                        )
                    )

                    for (items in response.data.indices) {
                        companyNameList.add(response.data.get(items).companyName.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                companyNameList.get(items + 1)
                            )
                        )
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
                            spCompany.setSelection(i + 1)
                        }
                    }

                    if (compnyID.equals("")) {
                        spCompany.setSelection(-1, true)
                        spCompany.nothingSelectedText = getString(R.string.select_company)
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                }

            }).addTo(autoDisposable)
    }

    fun getUserTypeList(serviceID: String) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", "")
            jsonBody.put("ServiceID", serviceID)

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

//                    if (serviceName.equals("Deep cleaning")) {
//
//                        userTypeNameList!!.add("Select Service Item")
//                        myList.add(SearchableItem(0, "Select Service Item"))
//                    } else {
                    userTypeNameList!!.add(getString(R.string.select_item))
                    myList.add(SearchableItem(0, getString(R.string.select_item)))
//                    }


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
                        this@AddQuotationActivity,
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
            .with(this@AddQuotationActivity)
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
                        this@AddQuotationActivity,
                        R.layout.custom_spinner_item,
                        materialTypeNameList!!
                    )
                    spMaterialType.setAdapter(adapterMaterialType)


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

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

        if (siteListItem != null) {

            for (i in session.stetList.indices) {

                siteListItem
                if (session.stetList.get(i).stateID.toString() == siteListItem!!.stateID.toString()) {
                    spState.setSelection(i)
                    break
                }

            }

        } else {

            for (i in session.stetList.indices) {
                if (session.stetList.get(i).stateID.toString().equals(stateID)) {
                    spState.setSelection(i)
                    break
                }

            }

            if (stateID.equals("")) {
                spState.setSelection(11)
            }
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
                        view3.isEnabled = true
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

                    } else {
                        cityListArray.clear()
                        cityNameList.clear()
                        cityID = "-1"
                        adapterCity?.clear()
                        // spCity.removeAllViews()
                        view3.isEnabled = false

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

    fun setUpdatedTotal() {
        UserAmount = 0f
        var df = DecimalFormat("##.##")
        var TotalAmount: Float = 0f
        var CGST: Float = 0f
        var SGST: Float = 0f
        var IGST: Float = 0f
        var DAYS: Float = 0f

        if (!edQty.isEmpty() && !edRate.isEmpty() && !edtDays.isEmpty()) {

            TotalAmount = TotalAmount + (edQty.getValue().toFloat() * edRate.getValue()
                .toFloat()) * edtDays.getValue().toFloat()
            txtItemTotalAmount.text =
                getString(R.string.RS) + " " + (edQty.getValue().toFloat() * edRate.getValue()
                    .toFloat()).toString()
        } else
            txtItemTotalAmount.text = getString(R.string.RS) + " 0"



        if (lin_add_user.childCount > 0) {
            for (item in 0 until lin_add_user.childCount) {
                if (!lin_add_user.getChildAt(item).edQtyChild.isEmpty() && !lin_add_user.getChildAt(
                        item
                    ).edRateChild.isEmpty() && !lin_add_user.getChildAt(item).edtChildDays.isEmpty()
                ) {

                    TotalAmount = TotalAmount + (lin_add_user.getChildAt(item).edQtyChild.getValue()
                        .toFloat() * lin_add_user.getChildAt(item).edRateChild.getValue()
                        .toFloat() * lin_add_user.getChildAt(item).edtChildDays.getValue()
                        .toFloat())

                    lin_add_user.getChildAt(item).txtItemTotalAmount.text =
                        getString(R.string.RS) + " " + (lin_add_user.getChildAt(item).edQtyChild.getValue()
                            .toFloat() * lin_add_user.getChildAt(item).edRateChild.getValue()
                            .toFloat()).toString()

                }

            }
        }



        if (!edMaterialQty.isEmpty() && !edMaterialRate.isEmpty() && !edtMaterialDays.isEmpty()) {

            TotalAmount =
                TotalAmount + (edMaterialQty.getValue().toFloat() * edMaterialRate.getValue()
                    .toFloat()) * edtMaterialDays.getValue().toFloat()
            txtMaterialTotalAmount.text = getString(R.string.RS) + " " + (edMaterialQty.getValue()
                .toFloat() * edMaterialRate.getValue().toFloat()).toString()
        } else
            txtMaterialTotalAmount.text = getString(R.string.RS) + " 0"




        if (lin_add_material.childCount > 0) {
            for (item in 0 until lin_add_material.childCount) {
                if (!lin_add_material.getChildAt(item).edQtyChild.isEmpty() && !lin_add_material.getChildAt(
                        item
                    ).edRateChild.isEmpty() && !lin_add_material.getChildAt(item).edtChildDays.isEmpty()
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

            var df = DecimalFormat("##.##")


            if (!compnyID.equals("1")) {
                CGST = 0f
                SGST = 0f
                IGST = 0f

            }

            if (siteListItem?.stateID?.toInt() == 12) {
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

    fun AddQuotation() {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            val materialJsonArray = JSONArray()
            val itemJsonArray = JSONArray()

            jsonBody.put("SitesID", siteListItem?.sitesID)

            jsonBody.put("CompanyID", compnyID)
            jsonBody.put("EstimateDate", formatDateFromString(edEstimationDate.getValue()))
            jsonBody.put("Address", edAddress1.getValue())
            jsonBody.put("Address2", edAddress2.getValue())
            jsonBody.put("CityID", cityID)
            jsonBody.put("StateID", stateID)
            jsonBody.put("PinCode", edPincode.getValue())
            jsonBody.put("SubTotal", edSubTotalAmount.getValue())
            jsonBody.put("CGST", edCGST.getValue())
            jsonBody.put("SGST", edSGST.getValue())
            jsonBody.put("IGST", edIGST.getValue())
            jsonBody.put("Notes", edtNote.getValue())
            jsonBody.put("Terms", edtTerms.getValue())
            jsonBody.put("StartDate", siteListItem?.startDate.toString())
            jsonBody.put("EndDate", siteListItem?.endDate.toString())
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
            jsonBody.put("Item", materialJsonArray)

            if (!edQty.isEmpty() && !edQty.getValue()
                    .equals("0") && !edRate.isEmpty() && !edRate.getValue()
                    .equals("0") && usertypeId != "-1"
            ) {
                val jsonObj = JSONObject()
                jsonObj.put("UsertypeID", usertypeId)
                jsonObj.put("Qty", edQty.getValue())
                jsonObj.put("Rate", edRate.getValue())
                jsonObj.put("Days", edtDays.getValue())
                itemJsonArray.put(jsonObj)
                materialJsonArray.put(jsonObj)
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

                materialJsonArray.put(jsonObj)
            }

            if (lin_add_user.childCount > 0) {
                for (item in 0 until lin_add_user.childCount) {
                    if (!lin_add_user.getChildAt(item).edQtyChild.isEmpty() && !lin_add_user.getChildAt(
                            item
                        ).edQtyChild.getValue().equals("0") && !lin_add_user.getChildAt(
                            item
                        ).edRateChild.isEmpty() && !lin_add_user.getChildAt(item).edRateChild.getValue()
                            .equals("0") && lin_add_user.getChildAt(item).spUserTypeChild.selectedItemPosition != 0
                    ) {
                        val jsonObj = JSONObject()
                        jsonObj.put(
                            "UsertypeID",
                            userTypeListArray?.get(lin_add_user.getChildAt(item).spUserTypeChild.selectedItemPosition - 1)?.usertypeID
                        )
                        jsonObj.put("Qty", lin_add_user.getChildAt(item).edQtyChild.getValue())
                        jsonObj.put("Rate", lin_add_user.getChildAt(item).edRateChild.getValue())
                        jsonObj.put("Days", lin_add_user.getChildAt(item).edtChildDays.getValue())
                        itemJsonArray.put(jsonObj)
                        materialJsonArray.put(jsonObj)

                    }

                }
            }

            if (lin_add_material.childCount > 0) {
                for (item in 0 until lin_add_material.childCount) {
                    if (!lin_add_material.getChildAt(item).edQtyChild.isEmpty() && !lin_add_material.getChildAt(
                            item
                        ).edQtyChild.getValue()
                            .equals("0") && !lin_add_material.getChildAt(item).edRateChild.isEmpty() && !lin_add_material.getChildAt(
                            item
                        ).edRateChild.getValue()
                            .equals("0") && lin_add_material.getChildAt(item).spUserTypeChild.selectedItemPosition != 0
                    ) {
                        val jsonObj = JSONObject()
                        jsonObj.put(
                            "UsertypeID",
                            materialTypeListArray?.get(lin_add_material.getChildAt(item).spUserTypeChild.selectedItemPosition - 1)?.usertypeID
                        )
                        jsonObj.put("Qty", lin_add_material.getChildAt(item).edQtyChild.getValue())
                        jsonObj.put(
                            "Rate",
                            lin_add_material.getChildAt(item).edRateChild.getValue()
                        )
                        jsonObj.put(
                            "Days",
                            lin_add_material.getChildAt(item).edtChildDays.getValue()
                        )
                        materialJsonArray.put(jsonObj)

                    }

                }
            }


            if (itemJsonArray.length() == 0) {
//                if (serviceName.equals("Deep cleaning"))
//                    root.showSnackBar("Please Select Service Item")
//                else
                root.showSnackBar("Please Select item")

                hideProgressbar()
                return
            }

            for (i in 0 until itemJsonArray.length()) {
                for (j in i + 1 until itemJsonArray.length()) {
                    if (itemJsonArray.getJSONObject(i).getString("UsertypeID")
                            .equals(itemJsonArray.getJSONObject(j).getString("UsertypeID"))
                    ) {
                        for (k in userTypeListArray!!.indices) {
                            if (itemJsonArray.getJSONObject(i).getString("UsertypeID")
                                    .equals(userTypeListArray!!.get(k).usertypeID)
                            ) {
                                root.showSnackBar(
                                    "Romove Duplicated Item (${
                                        userTypeListArray!!.get(
                                            k
                                        ).usertype
                                    })"
                                )
                                hideProgressbar()
                                return
                            }
                        }

                    }
                }
            }



            for (i in 0 until materialJsonArray.length()) {
                for (j in i + 1 until materialJsonArray.length()) {
                    if (materialJsonArray.getJSONObject(i).getString("UsertypeID")
                            .equals(materialJsonArray.getJSONObject(j).getString("UsertypeID"))
                    ) {
                        for (k in materialTypeListArray!!.indices) {
                            if (materialJsonArray.getJSONObject(i).getString("UsertypeID")
                                    .equals(materialTypeListArray!!.get(k).usertypeID)
                            ) {
                                root.showSnackBar(
                                    "Romove Duplicated Item (${
                                        materialTypeListArray!!.get(
                                            k
                                        ).usertype
                                    })"
                                )
                                hideProgressbar()
                                return
                            }
                        }

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
                        QuotationFragment.isFromQuotation = true
                        finish()
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    showAlert(message.toString())
                }

            }).addTo(autoDisposable)
    }

    fun getNotes() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("Type", "Quotation")
            result = Networking.setParentJsonData(Constant.METHOD_GET_NOTES, jsonBody)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .getNotes(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<GetNotesModal>() {
                override fun onSuccess(response: GetNotesModal) {
                    hideProgressbar()
                    if (response.error == 200) {


                        edtNote.setVerticalScrollBarEnabled(true)
                        edtNote.setOverScrollMode(View.OVER_SCROLL_ALWAYS)
                        edtNote.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET)
                        edtNote.setMovementMethod(ScrollingMovementMethod.getInstance())

                        edtNote.setOnTouchListener(OnTouchListener { view, motionEvent ->
                            view.parent.requestDisallowInterceptTouchEvent(true)
                            if (motionEvent.action and MotionEvent.ACTION_UP != 0 && motionEvent.actionMasked and MotionEvent.ACTION_UP != 0) {
                                view.parent.requestDisallowInterceptTouchEvent(false)
                            }
                            false
                        })


                        edtTerms.setVerticalScrollBarEnabled(true)
                        edtTerms.setOverScrollMode(View.OVER_SCROLL_ALWAYS)
                        edtTerms.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET)
                        edtTerms.setMovementMethod(ScrollingMovementMethod.getInstance())

                        edtTerms.setOnTouchListener(OnTouchListener { view, motionEvent ->
                            view.parent.requestDisallowInterceptTouchEvent(true)
                            if (motionEvent.action and MotionEvent.ACTION_UP != 0 && motionEvent.actionMasked and MotionEvent.ACTION_UP != 0) {
                                view.parent.requestDisallowInterceptTouchEvent(false)
                            }
                            false
                        })

                        edtNote.setText(response.data?.note)
                        edtTerms.setText(response.data?.term)
                    } else {

                    }


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

}