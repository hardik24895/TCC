package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.TicketDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_ticket.*

class TicketAdapter(
    private val mContext: Context,
    var list: MutableList<TicketDataItem> = mutableListOf(),
    private val listener: TicketAdapter.OnItemSelected
) : RecyclerView.Adapter<TicketAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_ticket,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]


        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: TicketDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: TicketDataItem,
            listener: TicketAdapter.OnItemSelected
        ) {

            txtTitle.text = data.title
            txtDescription.text = data.description
            txtCGST.text = data.firstName + " " + data.lastName
            txtSGST.text = data.priority
        }

    }
}