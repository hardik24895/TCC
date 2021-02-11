package com.tcc.app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.visible
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_quoatation.*

class QuotationAdapter(
    private val mContext: Context,
    var list: MutableList<String> = mutableListOf(),
    val isAccept: Boolean,
    private val listener: OnItemSelected
) : RecyclerView.Adapter<QuotationAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_quoatation,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener, isAccept)
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
            listener: QuotationAdapter.OnItemSelected,
            isAccept: Boolean
        ) {

            if (isAccept) {
                constrain.visible()
            }

        }

    }


}