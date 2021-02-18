package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.CustomerAttendanceDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_customer_attendance.*


class CustomerAttendanceListAdapter(
    private val mContext: Context,
    var list: MutableList<CustomerAttendanceDataItem> = mutableListOf(),
    private val listener: OnItemSelected
) : RecyclerView.Adapter<CustomerAttendanceListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_customer_attendance,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: CustomerAttendanceDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: CustomerAttendanceDataItem,
            listener: OnItemSelected
        ) {

            txtName.text = data.siteUserFrindlyName
            txtDate.text = data.attendanceDate
            txtEstimateNo.text = data.estimateNo
            txtPresentCount.text = data.presentCount
            txtAbsentCount.text = data.absentount
            txtHalfDayCount.text = data.halfDayount
            txtOverTime.text = data.overTime
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.siteUserFrindlyName.toString().substring(0, 1)
            txtIcon.visible()
            //  imgDown.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }


    }
}