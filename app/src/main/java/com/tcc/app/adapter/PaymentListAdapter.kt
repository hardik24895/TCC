package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rm.enterprise.modal.PaymentListDataItem
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.utils.Constant
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_payment_list.*


class PaymentListAdapter(
    private val mContext: Context,
    var list: MutableList<PaymentListDataItem> = mutableListOf(),
    private val listener: PaymentListAdapter.OnItemSelected
) : RecyclerView.Adapter<PaymentListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_payment_list,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: PaymentListDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: PaymentListDataItem,
            listener: PaymentListAdapter.OnItemSelected
        ) {

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.siteUserFrindlyName.toString().substring(0, 1)
            txtIcon.visible()



            if (data.siteUserFrindlyName.toString().isEmpty()) {
                txtCompanyName.setText(Constant.NA)
            } else {
                txtCompanyName.setText(data.siteUserFrindlyName)
            }

            if (data.invoiceNo.toString().equals("")) {
                txtInvoiceNo.setText(Constant.NA)
            } else {
                txtInvoiceNo.setText(data.invoiceNo)
            }

            if (data.paymentDate.toString().isEmpty()) {
                txtPaymentDate.setText(Constant.NA)
            } else {
                txtPaymentDate.setText(data.paymentDate)
            }

            if (data.paymentAmount.toString().isEmpty()) {
                txtPaymentAmount.setText(Constant.NA)
            } else {
                txtPaymentAmount.setText(data.paymentAmount)
            }

            if (data.paymentMode.toString().isEmpty()) {
                txtPaymentMode.setText(Constant.NA)
            } else {
                txtPaymentMode.setText(data.paymentMode)
            }

            if (data.amountType.toString().isEmpty()) {
                txtAmountType.setText(Constant.NA)
            } else {
                txtAmountType.setText(data.amountType)
            }

            if (data.gSTAmount.toString().isEmpty()) {
                txtGSTAmount.setText(Constant.NA)
            } else {
                txtGSTAmount.setText(data.gSTAmount)
            }

            if (data.remainingPayment.toString().isEmpty()) {
                txtRemainingPayment.setText("0")
            } else {
                txtRemainingPayment.setText(data.remainingPayment)
            }

            if (data.remainingGSTPayment.toString().isEmpty()) {
                txtRemainingGst.setText("0")
            } else {
                txtRemainingGst.setText(data.remainingGSTPayment)
            }

            if (data.chequeNo.toString().isEmpty()) {
                txtChequeNo.setText(Constant.NA)
            } else {
                txtChequeNo.setText(data.chequeNo)
            }

            if (data.iFCCode.toString().isEmpty()) {
                txtIFSCCode.setText(Constant.NA)
            } else {
                txtIFSCCode.setText(data.iFCCode)
            }

            if (data.accountNo.toString().isEmpty()) {
                txtAccountNo.setText(Constant.NA)
            } else {
                txtAccountNo.setText(data.accountNo)
            }

            if (data.bankName.toString().isEmpty()) {
                txtBankName.setText(Constant.NA)
            } else {
                txtBankName.setText(data.bankName)
            }

            if (data.branchName.toString().isEmpty()) {
                txtBranchName.setText(Constant.NA)
            } else {
                txtBranchName.setText(data.branchName)
            }
            txtTotal.text = data.totalAmount


        }


    }
}