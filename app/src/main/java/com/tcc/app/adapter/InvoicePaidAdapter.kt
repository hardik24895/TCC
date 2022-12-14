package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import com.tcc.app.modal.InvoiceDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_invoice.*


class InvoicePaidAdapter(
    private val mContext: Context,
    var list: MutableList<InvoiceDataItem> = mutableListOf(),
    var isPaid: Boolean = false,
    var flag: String,
    private val listener: InvoicePaidAdapter.OnItemSelected
) : RecyclerView.Adapter<InvoicePaidAdapter.ItemHolder>() {
    val MAX_LINES = 1
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_invoice,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener, isPaid, flag)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: InvoiceDataItem)
        fun openSelectedPDF(position: Int, data: InvoiceDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: InvoiceDataItem,
            listener: InvoicePaidAdapter.OnItemSelected,
            isPaid: Boolean,
            flag: String,
        ) {

            txtName.text = data.siteUserFrindlyName
            txtInvoiceno.text = data.invoiceNo
            txtEstimateno.text = data.estimateNo
            txtInvoiceDate.text = data.invoiceDate
            txtNotes.text = data.notes
            txtTerms.text = data.terms
            txtSubtotal.text = data.subTotal
            txtTotal.text = data.totalAmount


            if (data.cGST.equals(""))
                txtCgst.text = "N/A"
            else
                txtCgst.text = data.cGST

            if (data.sGST.equals(""))
                txtSgst.text = "N/A"
            else
                txtSgst.text = data.sGST


            if (data.iGST.equals(""))
                txtIgst.text = "N/A"
            else
                txtIgst.text = data.iGST



            crd_remaining.visible()
            texRemainingPayment.text = data.remainingPayment
            texRemainingGST.text = data.remainingGSTPayment


            if (isPaid) {

                btnPay.invisible()
            } else {
                txtNotes.visible()
            }
            txtNotes.setOnClickListener {
                if (txtNotes.maxLines == 1) txtNotes.setMaxLines(100) else txtNotes.setMaxLines(
                    1
                )
            }

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.siteUserFrindlyName.toString().substring(0, 1)
            txtIcon.visible()
            btnPay.setOnClickListener { listener.onItemSelect(adapterPosition, data) }

            imgPrint.setOnClickListener {

                listener.openSelectedPDF(adapterPosition, data)

            }
        }


    }


}