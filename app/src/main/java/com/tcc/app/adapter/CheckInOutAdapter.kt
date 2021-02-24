package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.CheckInOutDataItem
import com.tcc.app.utils.TimeStamp.getDateFromCheckInTime
import com.tcc.app.utils.TimeStamp.getTimeFromCheckInOUtTime
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_checkinout.*

class CheckInOutAdapter(
    private val mContext: Context,
    var list: MutableList<CheckInOutDataItem> = mutableListOf(),
    private val listener: OnItemSelected
) : RecyclerView.Adapter<CheckInOutAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_checkinout,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: CheckInOutDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: CheckInOutDataItem,
            listener: OnItemSelected
        ) {
            txtDate.text = getDateFromCheckInTime(data.checkintime.toString())
            txtCheckIn.text = getTimeFromCheckInOUtTime(data.checkintime.toString())
            txtCheckOut.text = getTimeFromCheckInOUtTime(data.checkouttime.toString())
            txtLocation.text = data.inAddress

        }

    }


}