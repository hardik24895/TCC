package com.tcc.app.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.visible
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_invoice.imgProfile
import kotlinx.android.synthetic.main.row_invoice.txtIcon
import kotlinx.android.synthetic.main.row_visitor.*

class VisitorAdapter(
    private val mContext: Context,
    var list: MutableList<String> = mutableListOf(),
    private val listener: VisitorAdapter.OnItemSelected
) : RecyclerView.Adapter<VisitorAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_visitor,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]


        holder.linbtnCall.setOnClickListener {
            holder.linbtnCall.isSelected = true
            holder.linbtnSMS.isSelected = false
            holder.linbtnEmail.isSelected = false

        }
        holder.linbtnSMS.setOnClickListener {
            holder.linbtnCall.isSelected = false
            holder.linbtnSMS.isSelected = true
            holder.linbtnEmail.isSelected = false

        }
        holder.linbtnEmail.setOnClickListener {
            holder.btnCall.isSelected = false
            holder.btnSMS.isSelected = false
            holder.btnEmail.isSelected = true

        }

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
            listener: VisitorAdapter.OnItemSelected
        ) {
            /* var txtName = containerView.findViewById<TextView>(R.id.txtName)
             txtName.text= data*/

            //chips.text= data

            /* if (data.user?.profileImage != null) {
                 Glide.with(context)
                     .load(data)
                     .circleCrop()
                     .placeholder(R.drawable.no_profile)
                     .into(imgProfile);
                 imgProfile.setColorFilter(null)
                 txtIcon.invisible()
             } else {
                 imgProfile.setImageResource(R.drawable.bg_circle)
                 imgProfile.setColorFilter(getRandomMaterialColor("400"))
                 txtIcon.text = data.user?.firstName.toString().substring(0, 1)
                 txtIcon.visible()
             }*/
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400"))
            txtIcon.text = "H"
            txtIcon.visible()
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
        }


        private fun getRandomMaterialColor(typeColor: String): Int {
            var returnColor = Color.GRAY
            val arrayId = itemView.context.resources.getIdentifier(
                "mdcolor_$typeColor",
                "array",
                itemView.context!!.packageName
            )
            if (arrayId != 0) {
                val colors = itemView.context.resources.obtainTypedArray(arrayId)
                val index = (Math.random() * colors.length()).toInt()
                returnColor = colors.getColor(index, Color.GRAY)
                colors.recycle()
            }
            return returnColor
        }

    }
}