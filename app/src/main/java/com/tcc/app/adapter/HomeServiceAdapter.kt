package com.tcc.app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.DashBoardLeadDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_home_service.*


class HomeServiceAdapter(
    private val mContext: Context,
    var list: MutableList<DashBoardLeadDataItem> = mutableListOf(),
    private val listener: HomeServiceAdapter.OnItemSelected
) : RecyclerView.Adapter<HomeServiceAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_home_service,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]


        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: DashBoardLeadDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: DashBoardLeadDataItem,
            listener: HomeServiceAdapter.OnItemSelected
        ) {

            txtName.text = data.name
            txtConatct.text = data.mobileNo
            txtAddress.text = data.address
            txtLeadType.text = data.leadType

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400",context))
            txtIcon.text = data.name.toString().substring(0, 1)
            txtIcon.visible()
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }



    }

}