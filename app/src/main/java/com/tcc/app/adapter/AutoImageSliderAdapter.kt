package com.tcc.app.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter
import com.tcc.app.R
import com.tcc.app.modal.HomeCounterDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_home_counter.*


class AutoImageSliderAdapter(
    private val mContext: Context,
    var list: MutableList<HomeCounterDataItem> = mutableListOf(),
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
        holder.bindData(mContext, data, listener, position)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: String)
    }

    class AutoImageSliderItemHolder(override val containerView: View) :
        ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: HomeCounterDataItem,
            listener: OnItemSelected,
            position: Int
        ) {
            txtCounterTitle.text = data.title
            txtCounter.text = data.count
            txtCounter1.text = data.count

            if (position == 0) {
                main_view.setBackgroundColor(Color.parseColor("#d32f2f"))
            } else if (position == 1) {
                main_view.setBackgroundColor(Color.parseColor("#7b1fa2"))
            } else if (position == 2) {
                main_view.setBackgroundColor(Color.parseColor("#303f9f"))
            } else if (position == 3) {
                main_view.setBackgroundColor(Color.parseColor("#0288d1"))
            } else if (position == 4) {
                main_view.setBackgroundColor(Color.parseColor("#00796b"))
            }else if (position == 5) {
                main_view.setBackgroundColor(Color.parseColor("#d32f2f"))
            } else if (position == 6) {
                main_view.setBackgroundColor(Color.parseColor("#7b1fa2"))
            } else if (position == 7) {
                main_view.setBackgroundColor(Color.parseColor("#303f9f"))
            } else if (position == 8) {
                main_view.setBackgroundColor(Color.parseColor("#0288d1"))
            } else if (position == 9) {
                main_view.setBackgroundColor(Color.parseColor("#00796b"))
            } else if (position == 10) {
                main_view.setBackgroundColor(Color.parseColor("#d32f2f"))
            } else if (position == 11) {
                main_view.setBackgroundColor(Color.parseColor("#7b1fa2"))
            } else if (position == 12) {
                main_view.setBackgroundColor(Color.parseColor("#303f9f"))
            }

        }

    }

}