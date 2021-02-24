package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.RejectReasonDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_inspection_answer_checkbox.*


class ReasonListAdapter(
    private val mContext: Context,
    var list: MutableList<RejectReasonDataItem> = mutableListOf(),
    private val listener: OnItemSelected
) : RecyclerView.Adapter<ReasonListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_inspection_answer_checkbox,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)

    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: RejectReasonDataItem, flag: Boolean)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context, data: RejectReasonDataItem, listener: OnItemSelected
        ) {
            cbAnswer.setText(data.reason)
            cbAnswer.setOnClickListener {
                listener.onItemSelect(adapterPosition, data, cbAnswer.isChecked)
            }

        }


    }
}