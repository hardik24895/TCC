package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.callPhone
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.CustomerDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_customer.*


class CustomerListAdapter(
    private val mContext: Context,
    var list: MutableList<CustomerDataItem> = mutableListOf(),
    private val listener: CustomerListAdapter.OnItemSelected
) : RecyclerView.Adapter<CustomerListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_customer,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: CustomerDataItem, action: String)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: CustomerDataItem,
            listener: CustomerListAdapter.OnItemSelected
        ) {

            txtName.text = data.name
            txtEmail.text = data.emailID
            txtMobile.text = data.mobileNo
            txtAddress.text = data.address
            txtSiteCount.text = data.siteCount
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.name.toString().substring(0, 1)
            txtIcon.visible()
            //  itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }


            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }


            linbtnCall.setOnClickListener {
                linbtnCall.isSelected = true
                linbtnSMS.isSelected = false
                linbtnEmail.isSelected = false
                callPhone(context, data.mobileNo.toString())

            }
            linbtnSMS.setOnClickListener {
                linbtnCall.isSelected = false
                linbtnSMS.isSelected = true
                linbtnEmail.isSelected = false
                listener.onItemSelect(adapterPosition, data, "SMS")
            }
            linbtnEmail.setOnClickListener {
                linbtnCall.isSelected = false
                linbtnSMS.isSelected = false
                linbtnEmail.isSelected = true
                listener.onItemSelect(adapterPosition, data, "Email")
            }
        }


    }
}