package com.tcc.app.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.visible
import com.tcc.app.modal.CustomerDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_customer.*


class CustomerListAdapter(
    private val mContext: Context,
    var list: MutableList<CustomerDataItem> = mutableListOf(),
    private val listener: CustomerListAdapter.OnItemSelected
) : RecyclerView.Adapter<CustomerListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_customer,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: CustomerDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: CustomerDataItem,
            listener: CustomerListAdapter.OnItemSelected
        ) {

            txtName.text = data.name
            txtEmail.text = data.emailID
            txtMobile.text = data.mobileNo
            txtAddress.text = data.address
            txtSiteCount.text = "2"
            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400"))
            txtIcon.text = data.name.toString().substring(0, 1)
            txtIcon.visible()
            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
            txtMobile.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${txtMobile.text}")
                context.startActivity(intent)
            }
        }

        /**
         * chooses a random color from array.xml
         */
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