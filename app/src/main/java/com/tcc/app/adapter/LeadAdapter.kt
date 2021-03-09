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
import com.tcc.app.modal.LeadItem
import com.tcc.app.utils.SessionManager
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_lead.*


class LeadAdapter(
    private val mContext: Context,
    var list: MutableList<LeadItem> = mutableListOf(),
    var session: SessionManager,
    private val listener: LeadAdapter.OnItemSelected,
) : RecyclerView.Adapter<LeadAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_lead,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener, session)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: LeadItem, action: String)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: LeadItem,
            listener: LeadAdapter.OnItemSelected, session: SessionManager
        ) {
            /* var txtName = containerView.findViewById<TextView>(R.id.txtName)
             txtName.text= data*/

            //chips.text= data

            txtName.text = data.name
            txtEmail.text = data.emailID
            txtContact.text = data.mobileNo
            txtLeadType.text = data.leadType
            txtNoOfSite.text = data.sitesCount
            if (data.customerID?.equals("0")!!) {
                txtStatus.text = "Pending"

                txtStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.bg_lead_status_pending_circle,
                    0,
                    0,
                    0
                )
            } else {
                txtStatus.text = "Customer"
                txtStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.bg_lead_status_convert_circle,
                    0,
                    0,
                    0
                )
            }
            txtCity.text = data.address
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.name.toString().substring(0, 1)
            txtIcon.visible()

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

            imgEdit.setOnClickListener {

                listener.onItemSelect(adapterPosition, data, "Edit")
            }


        }


    }
}