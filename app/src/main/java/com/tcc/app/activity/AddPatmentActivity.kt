package com.tcc.app.activity

import android.os.Bundle
import android.widget.RadioButton
import com.tcc.app.R
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.showSnackBar
import com.tcc.app.extention.visible
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_payment.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject

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
//                AddPaymentApi()
//            }

        }
    }

    fun AddPaymentApi() {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("InvoiceID", "")
            jsonBody.put("PaymentAmount", "")
            jsonBody.put("GSTAmount", "")
            jsonBody.put("AmountType", "")
            jsonBody.put("PaymentDate", "")
            jsonBody.put("PaymentMode", "")
            jsonBody.put("ChequeNo", "")
            jsonBody.put("IFCCode", "")
            jsonBody.put("AccountNo", "")
            jsonBody.put("BankName", "")
            jsonBody.put("BranchName", "")


            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_PAYMENT,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(this@AddPatmentActivity)
            .getServices()
            .AddPayment(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
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


}