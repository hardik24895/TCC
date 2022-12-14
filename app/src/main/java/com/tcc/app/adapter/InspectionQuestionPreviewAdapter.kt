package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.InspectionItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_preview_inspection_question.*

class InspectionQuestionPreviewAdapter(
    private val mContext: Context,
    var list: List<InspectionItem> = mutableListOf(),


    ) : RecyclerView.Adapter<InspectionQuestionPreviewAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_preview_inspection_question,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: String)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: InspectionItem,
        ) {
            txtQuestionTitle.text = data.question
            txtAns.text = data.answer

        }

    }


}