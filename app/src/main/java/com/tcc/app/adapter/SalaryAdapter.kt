package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.SalaryDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_salary.*
import java.text.DecimalFormat

class SalaryAdapter(
    private val mContext: Context,
    var list: MutableList<SalaryDataItem> = mutableListOf(),
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
        fun onItemSelect(position: Int, data: SalaryDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: SalaryDataItem,
            listener: SalaryAdapter.OnItemSelected
        ) {

            var df = DecimalFormat("##.##")
            val finalAmount =
                data.rate?.toBigDecimal()!! * (data.present?.toBigDecimal()!! + data.halfDay?.toBigDecimal()!! + data.halfOverTime?.toBigDecimal()!! + data.fullOverTime?.toBigDecimal()!!)
            txtAmount.text = context.getString(R.string.RS) + " " + df.format(finalAmount)
            txtDate.text = data.startDate
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }

        }

    }


}