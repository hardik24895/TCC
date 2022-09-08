package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.visible
import com.tcc.app.modal.PaneltyDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_penalty.*

class PenaltiAdapter(
    private val mContext: Context,
    var list: MutableList<PaneltyDataItem> = mutableListOf(),
    private val listener: PenaltiAdapter.OnItemSelected
) : RecyclerView.Adapter<PenaltiAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_penalty,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)


        val layoutManager = LinearLayoutManager(mContext)
        holder.recPanelty.layoutManager = layoutManager
        var adapter = PenaltyUserListAdapter(mContext, data.item)
        holder.recPanelty.adapter = adapter

    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: PaneltyDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: PaneltyDataItem,
            listener: PenaltiAdapter.OnItemSelected
        ) {
            txtName.text = data.sitesName
            txtReason.text = data.reason
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            if (data.sitesName?.length!! > 1)
                txtIcon.text = data.sitesName.toString().substring(0, 1)
            else
                txtIcon.text = " "
            txtIcon.visible()
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }

    }
}