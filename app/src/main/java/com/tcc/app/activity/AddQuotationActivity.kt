package com.tcc.app.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.tcc.app.R
import com.tcc.app.extention.showDateTimePicker
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_add_quotation.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class AddQuotationActivity : BaseActivity() {


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


}