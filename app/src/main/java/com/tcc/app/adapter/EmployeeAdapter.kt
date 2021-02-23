package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.extention.invisible
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.utils.Constant
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

             Glide.with(context).load(Constant.EMP_PROFILE + data.profilePic).placeholder(R.drawable.ic_profile).into(imgProfile)

          //  imgProfile.setImageResource(R.drawable.bg_circle)
        //    imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.firstName.toString().substring(0, 1)
            txtIcon.invisible()
            itemView.setOnClickListener {
                listener.onItemSelect(adapterPosition, data)
            }
        }




    }
}