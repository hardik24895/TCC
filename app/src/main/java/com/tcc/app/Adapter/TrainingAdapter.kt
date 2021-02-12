package com.tcc.app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.TrainingDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_training.*

class TrainingAdapter(
        private val mContext: Context,
        var list: MutableList<TrainingDataItem> = mutableListOf(),
        private val listener: OnItemSelected
) : RecyclerView.Adapter<TrainingAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_training,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: TrainingDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
                context: Context,
                data: TrainingDataItem,
                listener: TrainingAdapter.OnItemSelected
        ) {
            txtName.text = data.training
            txtDate.text = data.trainingDate

        }

    }


}