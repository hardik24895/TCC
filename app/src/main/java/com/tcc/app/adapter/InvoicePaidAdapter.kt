package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.InvoiceDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_invoice.*

class InvoicePaidAdapter(
    private val mContext: Context,
    var list: MutableList<InvoiceDataItem> = mutableListOf(),
    private val listener: InvoicePaidAdapter.OnItemSelected
) : RecyclerView.Adapter<InvoicePaidAdapter.ItemHolder>() {

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

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: InvoiceDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: InvoiceDataItem,
            listener: InvoicePaidAdapter.OnItemSelected
        ) {

            txtName.text = data.siteUserFrindlyName
            txtInvoiceno.text = data.invoiceNo
            txtCgst.text = data.cGST
            txtEstimateno.text = data.estimateNo
            txtInvoiceDate.text = data.invoiceDate
            txtSgst.text = data.sGST
            txtNotes.text = data.notes
            txtTerms.text = data.terms



            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = "H"
            txtIcon.visible()
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }


    }
}