package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.TeamDefinitionDataItem
import kotlinx.android.extensions.LayoutContainer


class TeamDefinitionListAdapter(
    private val mContext: Context,
    var list: MutableList<TeamDefinitionDataItem> = mutableListOf(),
    private val listener: TeamDefinitionListAdapter.OnItemSelected
) : RecyclerView.Adapter<TeamDefinitionListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_team_definition_list,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: TeamDefinitionDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: TeamDefinitionDataItem,
            listener: TeamDefinitionListAdapter.OnItemSelected
        ) {

//            txtSiteName.text = data.siteName
//            textName.text = data.name
//            txtSityType.text = data.siteType
//            txtHRS.text = data.workingHours
//            txtDay.text = data.workingDays
//            txtProposedDate.text = data.proposedDate
//            txtStartDate.text = data.startDate
//            txtEndDate.text = data.endDate
//            if (!data.address2?.isEmpty()!!)
//                txtAddress.text = data.address + "," + data.address2
//            else
//                txtAddress.text = data.address
//            imgProfile.setImageResource(R.drawable.bg_circle)
//            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
//            txtIcon.text = data.siteName.toString().substring(0, 1)
//            txtIcon.visible()
//            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }


    }
}