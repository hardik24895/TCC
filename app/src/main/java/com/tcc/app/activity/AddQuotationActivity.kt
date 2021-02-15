package com.tcc.app.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tcc.app.R
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.showDateTimePicker
import com.tcc.app.extention.visible
import com.tcc.app.modal.CompanyDataItem
import com.tcc.app.modal.CompanyListModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_quotation.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class AddQuotationActivity : BaseActivity() {

    var compnyID: String = "-1"
    var companyNameList: ArrayList<String> = ArrayList()
    var companyListArray: ArrayList<CompanyDataItem> = ArrayList()
    var adaptercompany: ArrayAdapter<String>? = null
    var companyIteams: List<SearchableItem>? = null

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


        edEstimationDate.setOnClickListener {
            showDateTimePicker(
                this@AddQuotationActivity,
                edEstimationDate
            )
        }
        getCompanyList()
        companySpinnerListner()
        compnyViewClick()

    }

    fun onAddField() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.row_dynamic_user, null, false)

        var btnRemove: TextView = rowView.findViewById(R.id.btnRemoveUser)
        btnRemove.setOnClickListener {
            lin_add_user.removeView(rowView)
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

                    if (compnyID.equals("-1")) {
                        spCompany.setSelection(-1, true)
                        spCompany.nothingSelectedText = getString(R.string.select_company)
                    }


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }
}