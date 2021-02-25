package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getColorCompat
import com.tcc.app.modal.AdvanceListDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_emp_wise_attendance_list.*


class AdvanceListAdapter(
    private val mContext: Context,
    var list: MutableList<AdvanceListDataItem> = mutableListOf(),
    private val listener: AdvanceListAdapter.OnItemSelected
) : RecyclerView.Adapter<AdvanceListAdapter.ItemHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_emp_wise_attendance_list,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        if (position % 2 == 0) {
            holder.crd_main.setBackgroundColor(mContext.getColorCompat(R.color.colorWhite))
        } else
            holder.crd_main.setBackgroundColor(mContext.getColorCompat(R.color.swipe_view_bg))

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: AdvanceListDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: AdvanceListDataItem,
            listener: AdvanceListAdapter.OnItemSelected
        ) {

            txtDate.text = data.advanceDate
            txtStatus.text = data.amount
            txtOverTime.text = data.type


        }


    }
}