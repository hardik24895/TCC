package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import kotlinx.android.extensions.LayoutContainer

class SalaryAdapter(
    private val mContext: Context,
    var list: MutableList<String> = mutableListOf(),
    private val listener: OnItemSelected
) : RecyclerView.Adapter<SalaryAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_salary,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: String)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: String,
            listener: SalaryAdapter.OnItemSelected
        ) {
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }

        }

    }


}