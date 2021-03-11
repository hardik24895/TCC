package com.tcc.app.activity

import android.os.Bundle
import android.view.View
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
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_payment.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat

class AddPaymentActivity : BaseActivity() {
    var invoiceDataItem: InvoiceDataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_payment)

        tilAccountNo.invisible()
        tilChequeNo.invisible()
        tilIFSC.invisible()
        tilBankName.invisible()
        tilBranchName.invisible()
        tilAccountHolderName.invisible()
        cardView3.invisible()
        if (intent.hasExtra(Constant.DATA)) {
            invoiceDataItem = intent.getSerializableExtra(Constant.DATA) as InvoiceDataItem

            edtCompanyName.setText(invoiceDataItem?.siteUserFrindlyName)
            edtEstimateno.setText(invoiceDataItem?.invoiceNo)
            var df = DecimalFormat("##.##")

            total_basic_amount.text = getString(R.string.RS) + " " + invoiceDataItem?.totalAmount
            remaining_basic_payment.text =
                getString(R.string.RS) + " " + invoiceDataItem?.remainingPayment

            total_gst_amount.text =
                getString(R.string.RS) + " " + df.format(invoiceDataItem?.cGST?.toFloat()!! * invoiceDataItem?.sGST?.toFloat()!! * invoiceDataItem?.iGST?.toFloat()!!)
                    .toString()
            remaining_gst_payment.text =
                getString(R.string.RS) + " " + df.format(invoiceDataItem?.remainingGSTPayment!!.toBigDecimal())
                    .toString()

        }


        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.payment)

        edtPaymentDate.setOnClickListener {
            showDateTimePicker(this@AddPaymentActivity, edtPaymentDate)
        }

        edtPaymentDate.setText(getCurrentDate())
        edtPaymentAmount.setText(invoiceDataItem?.remainingPayment)
        edtGSTAmount.setText(invoiceDataItem?.remainingGSTPayment)


        btnSubmit.setOnClickListener {
            validation()

        }



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
                tilAccountNo.invisible()
                tilAccountHolderName.visible()
                tilChequeNo.visible()
                tilIFSC.invisible()
                tilBankName.visible()
                tilBranchName.visible()
                cardView3.visible()

            } else if (radio.text.equals(getString(R.string.online))) {
                tilAccountNo.visible()
                tilAccountHolderName.invisible()
                tilChequeNo.invisible()
                tilIFSC.visible()
                tilBankName.visible()
                tilBranchName.visible()
                cardView3.visible()
            } else {
                tilAccountNo.invisible()
                tilAccountHolderName.invisible()
                tilChequeNo.invisible()
                tilIFSC.invisible()
                tilBankName.invisible()
                tilBranchName.invisible()
                cardView3.invisible()
            }
        })
    }


    fun validation() {
        when {


            edtPaymentAmount.isEmpty() && tilPaymentAmount.isVisible -> {
                root.showSnackBar("Enter Payment Amount")
                edtPaymentAmount.requestFocus()
            }

            tilPaymentAmount.isVisible && edtPaymentAmount.getValue()
                .toBigDecimal() > invoiceDataItem?.remainingPayment!!.toBigDecimal() -> {
                root.showSnackBar("Enter less than or equal to ${invoiceDataItem?.remainingPayment!!.toBigDecimal()}")
                edtPaymentAmount.requestFocus()
            }
            edtGSTAmount.isEmpty() && tilGSTAmount.isVisible -> {
                root.showSnackBar("Enter GST Amount")
                edtGSTAmount.requestFocus()
            }
            edtAccountNo.isEmpty() && tilAccountNo.isVisible -> {
                root.showSnackBar("Enter Account No.")
                edtAccountNo.requestFocus()
            }
            edtHolderName.isEmpty() && tilAccountHolderName.isVisible -> {
                root.showSnackBar("Enter Account Holder Name")
                edtHolderName.requestFocus()
            }
            edtChequeNo.isEmpty() && tilChequeNo.isVisible -> {
                root.showSnackBar("Enter Cheque No.")
                edtChequeNo.requestFocus()
            }
            edtIFSC.isEmpty() && tilIFSC.isVisible -> {
                root.showSnackBar("Enter IFSC Code")
                edtIFSC.requestFocus()
            }
            edtBankName.isEmpty() && tilBankName.isVisible -> {
                root.showSnackBar("Enter Bank Name")
                edtBankName.requestFocus()
            }

            edtBranchName.isEmpty() && tilBranchName.isVisible -> {
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
        val selectedId: Int = rgPaymentMode.getCheckedRadioButtonId()
        val rb = findViewById<View>(selectedId) as? RadioButton
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("InvoiceID", invoiceDataItem?.invoiceID)
            if (rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId())) == 0) {
                jsonBody.put("PaymentAmount", edtPaymentAmount.getValue())
                jsonBody.put("GSTAmount", edtGSTAmount.getValue())
            } else if (rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId())) == 1) {
                jsonBody.put("PaymentAmount", edtPaymentAmount.getValue())
                jsonBody.put("GSTAmount", "0")
            } else {
                jsonBody.put("PaymentAmount", "0")
                jsonBody.put("GSTAmount", edtGSTAmount.getValue())
            }
            jsonBody.put("AmountType", rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId())))
            jsonBody.put("PaymentDate", formatDateFromString(edtPaymentDate.getValue()))
            jsonBody.put("PaymentMode", rb?.text.toString())
            jsonBody.put("HolderName", edtHolderName.getValue())
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
            .with(this@AddPaymentActivity)
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