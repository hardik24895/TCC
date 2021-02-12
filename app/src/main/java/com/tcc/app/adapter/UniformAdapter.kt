package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.UniformDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_uniform.*

class UniformAdapter(
        private val mContext: Context,
        var list: MutableList<UniformDataItem> = mutableListOf(),
        private val listener: OnItemSelected
) : RecyclerView.Adapter<UniformAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_uniform,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: UniformDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
                context: Context,
                data: UniformDataItem,
                listener: UniformAdapter.OnItemSelected
        ) {

            txtName.text = data.uniformtype
            txtDate.text = data.uniformDate

        }

    }


}