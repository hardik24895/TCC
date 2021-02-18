package com.tcc.app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter
import com.tcc.app.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_home_counter.*


class AutoImageSliderAdapter(
        private val mContext: Context,
        var list: MutableList<String> = mutableListOf(),
        private val listener: OnItemSelected
) : SliderViewAdapter<AutoImageSliderAdapter.AutoImageSliderItemHolder>() {

    override fun getCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup): AutoImageSliderItemHolder {
        return AutoImageSliderItemHolder(
                LayoutInflater.from(mContext).inflate(
                        R.layout.row_home_counter,
                        parent, false
                )
        )
    }


    override fun onBindViewHolder(holder: AutoImageSliderItemHolder, position: Int) {
        val data = list[position]
        holder.txtCounterTitle.setText(data)

        if (position == 0) {
            holder.main_view.setBackgroundColor(Color.parseColor("#d32f2f"))
        } else if (position == 1) {
            holder.main_view.setBackgroundColor(Color.parseColor("#7b1fa2"))
        } else if (position == 2) {
            holder.main_view.setBackgroundColor(Color.parseColor("#303f9f"))
        } else if (position == 3) {
            holder.main_view.setBackgroundColor(Color.parseColor("#0288d1"))
        } else if (position == 4) {
            holder.main_view.setBackgroundColor(Color.parseColor("#00796b"))
        }


        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: String)
    }

    class AutoImageSliderItemHolder(override val containerView: View) :
            ViewHolder(containerView),
            LayoutContainer {


        fun bindData(
                context: Context,
                data: String,
                listener: OnItemSelected
        ) {

        }

    }

}