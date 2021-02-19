package com.tcc.app.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.AttendanceListDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_attendance.*

class AttendanceAdapter(
    private val mContext: Context,
    var list: MutableList<AttendanceListDataItem> = mutableListOf(),
    private val listener: AttendanceAdapter.OnItemSelected
) : RecyclerView.Adapter<AttendanceAdapter.ItemHolder>() {

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

        holder.txtNone.isSelected = true
        holder.txtOvertimeHalfDay.isEnabled = false
        holder.txtOvertimeFullDay.isEnabled = false
        setselection(holder, position, data)
        holder.bindData(mContext, data, listener)
    }

    private fun setselection(
        holder: AttendanceAdapter.ItemHolder,
        position: Int,
        data: AttendanceListDataItem
    ) {

        holder.txtPresent.setOnClickListener {

            data.SubmitAttendance.toString() == "0"
            holder.txtPresent.isSelected = true
            holder.txtHalfDay.isSelected = false
            holder.txtAbsent.isSelected = false
            holder.txtNone.isSelected = false
            holder.txtOvertimeHalfDay.isEnabled = true
            holder.txtOvertimeFullDay.isEnabled = true
            listener.onItemSelect(
                position,
                data,
                "PRESENT"
            )
        }
        holder.txtAbsent.setOnClickListener {
            data.SubmitAttendance.toString() == "0"
            holder.txtPresent.isSelected = false
            holder.txtAbsent.isSelected = true
            holder.txtHalfDay.isSelected = false
            holder.txtOvertimeHalfDay.isSelected = false
            holder.txtOvertimeFullDay.isSelected = false
            holder.txtNone.isSelected = false
            holder.txtOvertimeHalfDay.isEnabled = false
            holder.txtOvertimeFullDay.isEnabled = false
            listener.onItemSelect(position, data, "ABSENT")
        }
        holder.txtHalfDay.setOnClickListener {
            data.SubmitAttendance.toString() == "0.5"
            holder.txtPresent.isSelected = false
            holder.txtHalfDay.isSelected = true
            holder.txtAbsent.isSelected = false
            holder.txtOvertimeHalfDay.isSelected = false
            holder.txtOvertimeFullDay.isSelected = false
            holder.txtNone.isSelected = false

            holder.txtOvertimeHalfDay.isEnabled = false
            holder.txtOvertimeFullDay.isEnabled = false
            listener.onItemSelect(
                position,
                data,
                "HALFDAY"
            )
        }


        holder.txtOvertimeHalfDay.setOnClickListener {
            data.SubmitOvertime.toString() == "0.5"
            holder.txtHalfDay.isSelected = false
            holder.txtAbsent.isSelected = false
            holder.txtOvertimeHalfDay.isSelected = true
            holder.txtOvertimeFullDay.isSelected = false
            holder.txtNone.isSelected = false
            listener.onItemSelect(
                position,
                data,
                "OVERTIMEHALT"
            )
        }
        holder.txtOvertimeFullDay.setOnClickListener {
            data.SubmitOvertime.toString() == "1"
            holder.txtHalfDay.isSelected = false
            holder.txtAbsent.isSelected = false
            holder.txtOvertimeHalfDay.isSelected = false
            holder.txtOvertimeFullDay.isSelected = true
            holder.txtNone.isSelected = false
            listener.onItemSelect(
                position,
                data,
                "OVERTIMEFULL"
            )
        }
        holder.txtNone.setOnClickListener {
            data.SubmitOvertime.toString() == "0"
            holder.txtPresent.isSelected = false
            holder.txtHalfDay.isSelected = false
            holder.txtAbsent.isSelected = false
            holder.txtOvertimeHalfDay.isSelected = false
            holder.txtOvertimeFullDay.isSelected = false

            holder.txtOvertimeHalfDay.isEnabled = false
            holder.txtOvertimeFullDay.isEnabled = false
            holder.txtNone.isSelected = true
            listener.onItemSelect(position, data, "NONE")
        }

    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: AttendanceListDataItem, action: String)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: AttendanceListDataItem, listener: AttendanceAdapter.OnItemSelected
        ) {


            txtName.text = data.employeeName
            txtEmail.text = data.mobileNo
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.employeeName.toString().substring(0, 1)
            txtIcon.visible()


        }


    }
}