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
import com.tcc.app.modal.ProcessListDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_process.*

class ProcessAdapter(
    private val mContext: Context,
    var list: MutableList<ProcessListDataItem> = mutableListOf(),
    private val listener: ProcessAdapter.OnItemSelected
) : RecyclerView.Adapter<ProcessAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_process,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: ProcessListDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: ProcessListDataItem,
            listener: ProcessAdapter.OnItemSelected
        ) {
            txtName.text = data.visitorName
            txtDate.text = data.createdDate
            txtDesc.text = data.discription

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.visitorName.toString().substring(0, 1)
            txtIcon.visible()
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }


    }
}