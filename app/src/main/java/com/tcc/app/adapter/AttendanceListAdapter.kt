package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.AllEmpAttendanceDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_attendance_list.*


class AttendanceListAdapter(
    private val mContext: Context,
    var list: MutableList<AllEmpAttendanceDataItem> = mutableListOf(),
    private val listener: AttendanceListAdapter.OnItemSelected
) : RecyclerView.Adapter<AttendanceListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_attendance_list,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.txtPresent.isSelected = true
        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: AllEmpAttendanceDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: AllEmpAttendanceDataItem,
            listener: AttendanceListAdapter.OnItemSelected
        ) {

            txtPresent.text = data.presentCount
            txtAbsent.text = data.absentCount
            txtHalfDay.text = data.halfDayCount
            txtHalfDayOt.text = data.halfOverTime
            txtFullDayOt.text = data.fullOverTime
            txtName.text = data.employeeName
            txtTotal.text =
                (data.presentCount?.toFloat()!! + data.absentCount?.toFloat()!! + data.halfDayCount?.toFloat()!! + data.halfOverTime?.toFloat()!! + data.fullOverTime?.toFloat()!!).toString()
            txtContact.text = data.mobileNo
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.employeeName.toString().substring(0, 1)
            txtIcon.visible()
        }


    }
}