package com.tcc.app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_home_counter.*

class HomeCounterAdapter(
    private val mContext: Context,
    var list: MutableList<String> = mutableListOf(),
    private val listener: HomeCounterAdapter.OnItemSelected
) : RecyclerView.Adapter<HomeCounterAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_home_counter,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.txtCounterTitle.setText(data)

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
            listener: HomeCounterAdapter.OnItemSelected
        ) {

        }

    }
}