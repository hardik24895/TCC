package com.tcc.app.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.tcc.app.R
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.visible
import com.tcc.app.modal.LeadItem
import com.tcc.app.modal.StateItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_add_visitor.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class AddLeadActivity : BaseActivity() {
    var leadItem: LeadItem? = null
    var stateIteam: StateItem? = null
    var stateNameList: ArrayList<String> = ArrayList()
    var adapterState: ArrayAdapter<String>? = null
    var itens: List<SearchableItem>? = null
    var stateID: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_visitor)
        getBundleData()
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = resources.getText(R.string.visitor)
        btnSubmit.setOnClickListener { finish() }
        btnAddSite.setOnClickListener { goToActivity<AddSiteActivity>() }

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
                }

            }
        }

        view.setOnClickListener {

            SearchableDialog(this,
                itens!!,
                getString(R.string.select_state),
                { item, _ ->
                    spState.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun getBundleData() {
        if (intent.hasExtra(Constant.DATA)) {
            leadItem = intent.getSerializableExtra(Constant.DATA) as LeadItem
            setData()
        }
    }

    fun setData() {
        edtName.setText(leadItem?.name.toString())
        edtEmail.setText(leadItem?.emailID.toString())
        edtMobile.setText(leadItem?.mobileNo.toString())
        edtAddress.setText(leadItem?.address.toString())
        edtName.setText(leadItem?.name.toString())
        stateID = leadItem?.stateID.toString()
        var myList: MutableList<SearchableItem> = mutableListOf()
        for (i in session.stetList.indices) {
            stateNameList.add(session.stetList.get(i).stateName.toString())
            myList.add(SearchableItem(i.toLong(), stateNameList.get(i)))
        }
        itens = myList

        adapterState = ArrayAdapter(this, R.layout.custom_spinner_item, stateNameList)
        spState.setAdapter(adapterState)

        for (i in session.stetList.indices) {
            if (session.stetList.get(i).stateID.toString().equals(stateID)) {
                spState.setSelection(i)
            }

        }

    }
}