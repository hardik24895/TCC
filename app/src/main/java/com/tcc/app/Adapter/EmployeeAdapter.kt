package com.tcc.app.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.visible
import com.tcc.app.modal.EmployeeDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_employee.*
import kotlinx.android.synthetic.main.row_invoice.imgProfile
import kotlinx.android.synthetic.main.row_invoice.txtIcon
import kotlinx.android.synthetic.main.row_invoice.txtName

class EmployeeAdapter(
        private val mContext: Context,
        var list: MutableList<EmployeeDataItem> = mutableListOf(),
        private val listener: EmployeeAdapter.OnItemSelected
) : RecyclerView.Adapter<EmployeeAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_employee,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: EmployeeDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
                context: Context,
                data: EmployeeDataItem,
                listener: EmployeeAdapter.OnItemSelected
        ) {

            txtName.text = data.firstName + " " + data.lastName
            txtEmail.text = data.emailID
            txtContact.text = data.mobileNo
            txtUserType.text = data.usertype
            txtJoinDate.text = data.joiningDate
            txtCity.text = data.cityName



            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400"))
            txtIcon.text = data.firstName.toString().substring(0, 1)
            txtIcon.visible()
            itemView.setOnClickListener {
                listener.onItemSelect(adapterPosition, data)
            }
        }


        private fun getRandomMaterialColor(typeColor: String): Int {
            var returnColor = Color.GRAY
            val arrayId = itemView.context.resources.getIdentifier(
                "mdcolor_$typeColor",
                "array",
                itemView.context!!.packageName
            )
            if (arrayId != 0) {
                val colors = itemView.context.resources.obtainTypedArray(arrayId)
                val index = (Math.random() * colors.length()).toInt()
                returnColor = colors.getColor(index, Color.GRAY)
                colors.recycle()
            }
            return returnColor
        }

    }
}