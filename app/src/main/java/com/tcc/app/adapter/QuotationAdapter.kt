package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.QuotationItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_quoatation.*

class QuotationAdapter(
    private val mContext: Context,
    var list: MutableList<QuotationItem> = mutableListOf(),
    val isAccept: String,
    private val listener: OnItemSelected
) : RecyclerView.Adapter<QuotationAdapter.ItemHolder>() {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_quoatation,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener, isAccept)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: String)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: QuotationItem,
            listener: QuotationAdapter.OnItemSelected,
            isAccept: String
        ) {
            txtCompanyName.text = data.companyName
            txtSiteName.text = data.siteName
            txtEstinationNo.text = data.estimateNo
            txtHRS.text = data.service
            txtName.text = data.name
            txtStatus.text = data.status
            txtCGST.text = data.cGST
            txtSGST.text = data.sGST
            txtIGST.text = data.iGST
            txtTotal.text = data.total
            txtAddress.text = data.address

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.name.toString().substring(0, 1)
            txtIcon.visible()

            if (isAccept.equals("Accept")) {
                constrain_accepted.visible()
            } else if (isAccept.equals("Pending")) {
                constrain_pending.visible()
            }

        }

    }


}