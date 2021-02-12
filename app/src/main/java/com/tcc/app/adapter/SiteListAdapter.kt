package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.SiteListItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_site_list.*


class SiteListAdapter(
    private val mContext: Context,
    var list: MutableList<SiteListItem> = mutableListOf(),
    private val listener: SiteListAdapter.OnItemSelected
) : RecyclerView.Adapter<SiteListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_site_list,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: SiteListItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: SiteListItem,
            listener: SiteListAdapter.OnItemSelected
        ) {

            txtSiteName.text = data.siteName
            textName.text = data.name
            txtSityType.text = data.siteType
            txtHRS.text = data.workingHours
            txtDay.text = data.workingDays
            txtProposedDate.text = data.proposedDate
            txtStartDate.text = data.startDate
            txtEndDate.text = data.endDate
            txtAddress.text = data.address

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.siteName.toString().substring(0, 1)
            txtIcon.visible()
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }


    }
}