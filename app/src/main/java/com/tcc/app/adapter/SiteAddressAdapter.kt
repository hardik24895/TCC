package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.hide
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import com.tcc.app.modal.SitesItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_site_address.*


class SiteAddressAdapter(
    private val mContext: Context,
    var list: MutableList<SitesItem> = mutableListOf(),
    private val listener: SiteAddressAdapter.OnItemSelected,

    ) : RecyclerView.Adapter<SiteAddressAdapter.ItemHolder>() {
    var lastPos = -1
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_site_address,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]


        if (position.equals(list.size - 1)) {
            holder.txtSiteName.hide()
            holder.txtSiteAddress.hide()
            holder.txtAddNew.visible()
        } else {

            holder.txtSiteName.visible()
            holder.txtSiteAddress.visible()
            holder.txtAddNew.invisible()
            holder.txtSiteName.text = data.siteName
            holder.txtSiteAddress.text =
                data.address + ", " + data.address2 + ", " + data.pinCode + ", " + data.cityName + ", " + data.stateName
        }
        holder.itemView.setOnClickListener {

            if (lastPos == -1) {
                holder.root.setBackgroundResource(R.drawable.bg_none_selected)
                lastPos = position

            } else {
                holder.root.get(lastPos).setBackgroundResource(R.drawable.bg_service_unselected)
                notifyItemChanged(lastPos)
                holder.root.setBackgroundResource(R.drawable.bg_none_selected)
                notifyItemChanged(position)
                lastPos = position
            }

            if (position == list.size - 1) {
                listener.onItemSelect(-1)
            } else {
                listener.onItemSelect(position, data)
            }
        }

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: SitesItem)
        fun onItemSelect(position: Int)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: SitesItem,
            listener: SiteAddressAdapter.OnItemSelected
        ) {


        }


    }
}