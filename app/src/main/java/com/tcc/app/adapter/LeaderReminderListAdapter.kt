package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.LeadReminderDataItem
import com.tcc.app.modal.SiteListItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_lead_reminder_list.*


class LeaderReminderListAdapter(
    private val mContext: Context,
    var list: MutableList<LeadReminderDataItem> = mutableListOf()
) : RecyclerView.Adapter<LeaderReminderListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_lead_reminder_list,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: SiteListItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: LeadReminderDataItem
        ) {

            txtname.text = data.employeeName
            txtReminderDateTime1.text = data.reminderDate
            txtProcessDatetime2.text = data.pastDate
            txtmsg.text = data.message
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.employeeName.toString().substring(0, 1)
            txtIcon.visible()

        }


    }
}