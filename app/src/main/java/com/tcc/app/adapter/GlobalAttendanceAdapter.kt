package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.GlobalEmployeeAttedanceDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_attendance.*

class GlobalAttendanceAdapter(
    private val mContext: Context,
    var list: MutableList<GlobalEmployeeAttedanceDataItem> = mutableListOf(),
    private val listener: OnItemSelected
) : RecyclerView.Adapter<GlobalAttendanceAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_attendance,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        if (data.attendance.equals("1")) {
            holder.txtPresent.isSelected = true
            holder.txtOvertimeHalfDay.isEnabled = true
            holder.txtOvertimeFullDay.isEnabled = true
            holder.txtAbsent.isSelected = false
            holder.txtHalfDay.isSelected = false
            holder.txtNone.isSelected = false
            if (data.overtime.equals("1")) {
                holder.txtOvertimeHalfDay.isSelected = false
                holder.txtOvertimeFullDay.isSelected = true
            } else if (data.overtime.equals("0.5")) {
                holder.txtOvertimeHalfDay.isSelected = true
                holder.txtOvertimeFullDay.isSelected = false
            } else {
                holder.txtOvertimeHalfDay.isSelected = false
                holder.txtOvertimeFullDay.isSelected = false
            }

        } else if (data.attendance.equals("0.5")) {
            holder.txtPresent.isSelected = false
            holder.txtHalfDay.isSelected = true
            holder.txtAbsent.isSelected = false
            holder.txtOvertimeHalfDay.isSelected = false
            holder.txtOvertimeFullDay.isSelected = false
            holder.txtNone.isSelected = false

        } else if (data.attendance.equals("0")) {
            holder.txtPresent.isSelected = false
            holder.txtAbsent.isSelected = true
            holder.txtHalfDay.isSelected = false
            holder.txtOvertimeHalfDay.isSelected = false
            holder.txtOvertimeFullDay.isSelected = false
            holder.txtNone.isSelected = false

        }
        if (data.attendance.equals("-1") || data.attendance.equals("")) {
            holder.txtPresent.isSelected = false
            holder.txtHalfDay.isSelected = false
            holder.txtAbsent.isSelected = false
            holder.txtOvertimeHalfDay.isSelected = false
            holder.txtOvertimeFullDay.isSelected = false
            holder.txtNone.isSelected = true
        }

        setselection(holder, position, list[position])
        holder.bindData(mContext, data, listener)
    }

    private fun setselection(
        holder: GlobalAttendanceAdapter.ItemHolder,
        position: Int,
        data: GlobalEmployeeAttedanceDataItem
    ) {

        holder.txtPresent.setOnClickListener {
            listener.onItemSelect(position, data, "PRESENT", "1", "0")
            notifyItemChanged(position)
        }
        holder.txtAbsent.setOnClickListener {

            listener.onItemSelect(position, data, "ABSENT", "0", "0")
            notifyItemChanged(position)
        }
        holder.txtHalfDay.setOnClickListener {

            listener.onItemSelect(position, data, "HALFDAY", "0.5", "0")
            notifyItemChanged(position)
        }


        holder.txtOvertimeHalfDay.setOnClickListener {

            listener.onItemSelect(position, data, "OVERTIMEHALT", "1", "0.5")
            notifyItemChanged(position)
        }
        holder.txtOvertimeFullDay.setOnClickListener {

            listener.onItemSelect(position, data, "OVERTIMEFULL", "1", "1")
            notifyItemChanged(position)
        }
        holder.txtNone.setOnClickListener {

            listener.onItemSelect(position, data, "NONE", "-1", "0")
            notifyItemChanged(position)
        }

    }

    interface OnItemSelected {
        fun onItemSelect(
            position: Int,
            data: GlobalEmployeeAttedanceDataItem,
            action: String,
            attendance: String,
            overtime: String
        )
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: GlobalEmployeeAttedanceDataItem, listener: GlobalAttendanceAdapter.OnItemSelected
        ) {

            imgProfile.setImageResource(R.drawable.bg_circle)
            txtName.text = data.firstName + " " + data.lastName
            txtEmail.text = data.mobileNo
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.firstName.toString().substring(0, 1)
            txtIcon.visible()


        }


    }
}