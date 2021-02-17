package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.PaymentListDataItem
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
            txtCompanyName.setText(data.siteUserFrindlyName)
            txtInvoiceNo.setText(data.invoiceNo)
            txtPaymentDate.setText(data.paymentDate)
            txtPaymentAmount.setText(data.paymentAmount)
            txtPaymentMode.setText(data.paymentMode)
            txtAmountType.setText(data.amountType)
            txtGSTAmount.setText(data.gSTAmount)
            txtChequeNo.setText(data.chequeNo)
            txtIFSCCode.setText(data.iFCCode)
            txtAccountNo.setText(data.accountNo)
            txtBankName.setText(data.bankName)
            txtBranchName.setText(data.branchName)


        }


    }
}