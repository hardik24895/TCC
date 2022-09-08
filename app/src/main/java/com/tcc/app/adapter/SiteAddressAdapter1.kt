package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import com.tcc.app.modal.SiteListItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_site_address.*


class SiteAddressAdapter1(
    private val mContext: Context,
    var list: MutableList<SiteListItem> = mutableListOf(),
    private val listener: SiteAddressAdapter1.OnItemSelected

) : RecyclerView.Adapter<SiteAddressAdapter1.ItemHolder>() {
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
            holder.txtSiteName.invisible()
            holder.txtSiteAddress.invisible()
            holder.txtAddNew.visible()
        } else {

            holder.txtSiteName.visible()
            holder.txtSiteAddress.visible()
            holder.txtAddNew.invisible()
            holder.txtSiteName.text = data.siteName
            if (data.pinCode.equals(""))
                holder.txtSiteAddress.text =
                    data.address + ", " + data.address2 + ", " + data.cityName + ", " + data.stateName
            else
                holder.txtSiteAddress.text =
                    data.address + ", " + data.address2 + ", " + data.pinCode + ", " + data.cityName + ", " + data.stateName
        }


        if (lastPos != -1 && position == lastPos)
            holder.view_select.setBackgroundResource(R.drawable.bg_none_selected)
        else {
            holder.view_select.setBackgroundResource(R.drawable.bg_address_select)
        }



        holder.itemView.setOnClickListener {
            lastPos = position
            if (lastPos == -1) {
                holder.view_select.setBackgroundResource(R.drawable.bg_none_selected)
            } else {
                notifyDataSetChanged()
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
        fun onItemSelect(position: Int, data: SiteListItem)
        fun onItemSelect(position: Int)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: SiteListItem,
            listener: SiteAddressAdapter1.OnItemSelected
        ) {


        }


    }
}