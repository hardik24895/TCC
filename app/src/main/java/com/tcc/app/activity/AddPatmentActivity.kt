package com.tcc.app.activity

import android.os.Bundle
import android.widget.RadioButton
import androidx.core.view.isVisible
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.modal.InvoiceDataItem
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
    var invoiceDataItem: InvoiceDataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_payment)

        if (intent.hasExtra(Constant.DATA)) {
            invoiceDataItem = intent.getSerializableExtra(Constant.DATA) as InvoiceDataItem

        }


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
            edtPaymentAmount.isEmpty() -> {
                root.showSnackBar("Enter Payment Amount")
                edtPaymentAmount.requestFocus()
            }
            edtGSTAmount.isEmpty() && edtGSTAmount.isVisible -> {
                root.showSnackBar("Enter GST Amount")
                edtGSTAmount.requestFocus()
            }
            edtAccountNo.isEmpty() && edtAccountNo.isVisible -> {
                root.showSnackBar("Enter Account No.")
                edtAccountNo.requestFocus()
            }
            edtChequeNo.isEmpty() && edtChequeNo.isVisible -> {
                root.showSnackBar("Enter Cheque No.")
                edtChequeNo.requestFocus()
            }
            edtIFSC.isEmpty() && edtIFSC.isVisible -> {
                root.showSnackBar("Enter IFSC Code")
                edtIFSC.requestFocus()
            }
            edtBankName.isEmpty() && edtBankName.isVisible -> {
                root.showSnackBar("Enter Bank Name")
                edtBankName.requestFocus()
            }

            edtBranchName.isEmpty() && edtBranchName.isVisible -> {
                root.showSnackBar("Enter Branch Name")
                edtBranchName.requestFocus()
            }
            else -> {
                AddPaymentApi()
            }

        }
    }

    fun AddPaymentApi() {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("InvoiceID", invoiceDataItem?.invoiceID)
            jsonBody.put("PaymentAmount", edtPaymentAmount.getValue())
            jsonBody.put("GSTAmount", edtGSTAmount.getValue())
            jsonBody.put("AmountType", rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId())))
            jsonBody.put("PaymentDate", edtPaymentDate.getValue())
            jsonBody.put(
                "PaymentMode",
                rgPaymentMode.indexOfChild(findViewById(rgPaymentMode.getCheckedRadioButtonId()))
            )
            jsonBody.put("ChequeNo", edtChequeNo.getValue())
            jsonBody.put("IFCCode", edtIFSC.getValue())
            jsonBody.put("AccountNo", edtAccountNo.getValue())
            jsonBody.put("BankName", edtBankName.getValue())
            jsonBody.put("BranchName", edtBranchName.getValue())


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