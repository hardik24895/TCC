package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.PaleltyUserListItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_preview_inspection_question.*

class PenaltyUserListAdapter(
    private val mContext: Context,
    var list: List<PaleltyUserListItem> = mutableListOf(),


    ) : RecyclerView.Adapter<PenaltyUserListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_panelty_user,
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
            data: PaleltyUserListItem,
        ) {
            txtQuestionTitle.text = data.employeeName
            txtAns.text = "${context.getText(R.string.RS)}  ${data.penalty}"

        }

    }


}