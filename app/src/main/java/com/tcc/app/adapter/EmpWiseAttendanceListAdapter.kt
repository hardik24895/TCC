package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getColorCompat
import com.tcc.app.modal.EmployeeWiseDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_emp_wise_attendance_list.*


class EmpWiseAttendanceListAdapter(
    private val mContext: Context,
    var list: MutableList<EmployeeWiseDataItem> = mutableListOf(),
    private val listener: EmpWiseAttendanceListAdapter.OnItemSelected
) : RecyclerView.Adapter<EmpWiseAttendanceListAdapter.ItemHolder>() {

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
        fun onItemSelect(position: Int, data: EmployeeWiseDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: EmployeeWiseDataItem,
            listener: EmpWiseAttendanceListAdapter.OnItemSelected
        ) {

            txtDate.text = data.attendanceDate
            if (data.attendance.equals("1")) {
                txtStatus.text = context.getText(R.string.present)
            } else if (data.attendance.equals("0.5")) {
                txtStatus.text = context.getText(R.string.half_day)
            } else if (data.attendance.equals("0")) {
                txtStatus.text = context.getText(R.string.absent)
            }

            if (data.overTime.equals("1")) {
                txtOverTime.text = context.getText(R.string.fullday)
            } else if (data.overTime.equals("0.5")) {
                txtOverTime.text = context.getText(R.string.half_day)
            } else if (data.overTime.equals("0")) {
                txtOverTime.text = ""
            }


        }


    }
}