package com.tcc.app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.DashBoardLeadDataItem
import com.tcc.app.modal.LeadFollowUpDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_lead_follow_up.*


class LeadFollowUpAdapter(
    private val mContext: Context,
    var list: MutableList<LeadFollowUpDataItem> = mutableListOf(),
    private val listener: OnItemSelected
) : RecyclerView.Adapter<LeadFollowUpAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_lead_follow_up,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]


        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: LeadFollowUpDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: LeadFollowUpDataItem,
            listener: LeadFollowUpAdapter.OnItemSelected
        ) {

            txtEmpName.text = data.employeeFirstName + " " + data.employeeLastName
            txtConatct.text = data.mobileNo
            txtMessage.text = data.message
            txtName.text = data.name

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400",context))
            txtIcon.text = data.name.toString().substring(0, 1)
            txtIcon.visible()
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }



    }

}