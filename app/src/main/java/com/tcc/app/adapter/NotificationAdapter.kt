package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.NotificationDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_notification.*

class NotificationAdapter(
    private val mContext: Context,
    var list: MutableList<NotificationDataItem> = mutableListOf(),
    private val listener: OnItemSelected
) : RecyclerView.Adapter<NotificationAdapter.ItemHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_notification,
                parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: NotificationDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(context: Context, data: NotificationDataItem) {
            txtTitle.text = data.discription
            txtDate.text = data.createdDate
        }
    }
}