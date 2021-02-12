package com.tcc.app.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.visible
import com.tcc.app.modal.LeadItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_invoice.imgProfile
import kotlinx.android.synthetic.main.row_invoice.txtIcon
import kotlinx.android.synthetic.main.row_visitor.*

class LeadAdapter(
        private val mContext: Context,
        var list: MutableList<LeadItem> = mutableListOf(),
        private val listener: LeadAdapter.OnItemSelected
) : RecyclerView.Adapter<LeadAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
                LayoutInflater.from(mContext).inflate(
                        R.layout.row_visitor,
                        parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]


        holder.btnCall.setOnClickListener {
            holder.btnCall.isSelected = true
            holder.btnSMS.isSelected = false
            holder.btnEmail.isSelected = false

        }
        holder.btnSMS.setOnClickListener {
            holder.btnCall.isSelected = false
            holder.btnSMS.isSelected = true
            holder.btnEmail.isSelected = false

        }
        holder.btnEmail.setOnClickListener {
            holder.btnCall.isSelected = false
            holder.btnSMS.isSelected = false
            holder.btnEmail.isSelected = true

        }

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: LeadItem)
    }

    class ItemHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView),
            LayoutContainer {


        fun bindData(
                context: Context,
                data: LeadItem,
                listener: LeadAdapter.OnItemSelected
        ) {
            /* var txtName = containerView.findViewById<TextView>(R.id.txtName)
             txtName.text= data*/

            //chips.text= data

            txtName.text = data.name
            txtEmail.text = data.emailID
            txtContact.text = data.mobileNo
            txtLeadType.text = data.leadType
            if (data.customerID?.equals("0")!!) {
                txtStatus.text = "Pending"
            } else {
                txtStatus.text = "Customer"
            }
            txtCity.text = data.address
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400"))
            txtIcon.text = data.name.toString().substring(0, 1)
            txtIcon.visible()

            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }


        private fun getRandomMaterialColor(typeColor: String): Int {
            var returnColor = Color.GRAY
            val arrayId = itemView.context.resources.getIdentifier(
                    "mdcolor_$typeColor",
                    "array",
                    itemView.context!!.packageName
            )
            if (arrayId != 0) {
                val colors = itemView.context.resources.obtainTypedArray(arrayId)
                val index = (Math.random() * colors.length()).toInt()
                returnColor = colors.getColor(index, Color.GRAY)
                colors.recycle()
            }
            return returnColor
        }

    }
}