package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.RoomDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_room_allocation.*

class RoomAllocationAdapter(
        private val mContext: Context,
        var list: MutableList<RoomDataItem> = mutableListOf(),
        private val listener: OnItemSelected
) : RecyclerView.Adapter<RoomAllocationAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_room_allocation,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: RoomDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
                context: Context,
                data: RoomDataItem,
                listener: RoomAllocationAdapter.OnItemSelected
        ) {

            txtRoomNo.text = data.roomNo
            txtAddress.text = data.roomAddress
            txtStartDate.text = data.startDate
            txtEndDate.text = data.endDate


        }

    }


}