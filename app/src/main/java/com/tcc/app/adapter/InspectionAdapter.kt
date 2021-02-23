package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.extention.invisible
import com.tcc.app.utils.Constant
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_inspection.*


class InspectionAdapter(
    private val mContext: Context,
    var list: MutableList<InspectionDataItem> = mutableListOf(),
    private val listener: InspectionAdapter.OnItemSelected
) : RecyclerView.Adapter<InspectionAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_inspection,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: InspectionDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: InspectionDataItem,
            listener: InspectionAdapter.OnItemSelected
        ) {
            txtName.text = data.employeeName
            txtCompanyName.text = data.companyName
            txtUserType.text = data.userType
            txtSiteName.text = data.sitesName
            txtDate.text = data.inspectionDate
            Glide.with(context).load(Constant.PDF_INSPECTION_URL + data.image)
                .placeholder(R.drawable.ic_profile).into(imgProfile)
            txtIcon.text = data.employeeName.toString().substring(0, 1)
            txtIcon.invisible()

            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }


    }
}