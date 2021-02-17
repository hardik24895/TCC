package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.InspectionQuesDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_inspection_question.*

class InspectionQuesiontAdapter(
    private val mContext: Context,
    var list: MutableList<InspectionQuesDataItem> = mutableListOf(),
    private val listener: InspectionQuesiontAdapter.OnItemSelected
) : RecyclerView.Adapter<InspectionQuesiontAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_inspection_question,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)

        holder.txtQuestion.text = data.question
        var stringArray = data.questionoption?.split(",")
        val layoutManager = LinearLayoutManager(mContext)
        holder.rvAns.layoutManager = layoutManager
        var adapter = InspectionAnswerAdapter(mContext, data, holder.rvAns, stringArray)
        holder.rvAns.adapter = adapter
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: InspectionQuesDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: InspectionQuesDataItem,
            listener: InspectionQuesiontAdapter.OnItemSelected
        ) {

        }

    }

}