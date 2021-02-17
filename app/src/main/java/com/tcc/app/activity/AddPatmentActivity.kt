package com.tcc.app.activity

import android.os.Bundle
import android.widget.RadioButton
import com.tcc.app.R
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_add_payment.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class AddPatmentActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_payment)


        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.payment)

//        edtStartDate.setOnClickListener {
//            showDateTimePicker(this@AddPatmentActivity, edtStartDate)
//        }
//
//        edtStartDate.setText(getCurrentDate())
//
//
//        btnSubmit.setOnClickListener {
//            validation()
//
//        }

        rg.setOnCheckedChangeListener({ group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals(getString(R.string.including_gst_amount))) {
                tilPaymentAmount.visible()
                tilGSTAmount.visible()
            } else if (radio.text.equals(getString(R.string.excluding_gst_amount))) {
                tilPaymentAmount.visible()
                tilGSTAmount.invisible()
            } else {
                tilPaymentAmount.invisible()
                tilGSTAmount.visible()
            }
        })


        rgPaymentMode.setOnCheckedChangeListener({ group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text.equals(getString(R.string.cheque))) {
                tilChequeNo.visible()
                tilIFSC.invisible()
                tilBankName.visible()
                tilBranchName.visible()

            } else if (radio.text.equals(getString(R.string.online))) {
                tilChequeNo.invisible()
                tilIFSC.visible()
                tilBankName.visible()
                tilBranchName.visible()
            } else {
                tilChequeNo.invisible()
                tilIFSC.invisible()
                tilBankName.invisible()
                tilBranchName.invisible()
            }
        })
    }


    fun validation() {
        when {
//            edtRoomNo.isEmpty() -> {
//                root.showSnackBar("Enter Room Number")
//                edtRoomNo.requestFocus()
//            }
//            edtRoomAddress.isEmpty() -> {
//                root.showSnackBar("Enter Room Address")
//                edtRoomAddress.requestFocus()
//            }
//            edtStartDate.isEmpty() -> {
//                root.showSnackBar("Select Start Date")
//                edtStartDate.requestFocus()
//            }
//            edtEndDate.isEmpty() -> {
//                root.showSnackBar("Select End Date")
//                edtEndDate.requestFocus()
//            }
//            else -> {
//                AddEmployee()
//            }

        }
    }


}