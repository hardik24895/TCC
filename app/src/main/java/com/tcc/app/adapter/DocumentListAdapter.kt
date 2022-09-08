package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.extention.hide
import com.tcc.app.extention.visible
import com.tcc.app.modal.DocumentListDataItem
import com.tcc.app.utils.Constant
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_document_list.*


class DocumentListAdapter(
    private val mContext: Context,
    var list: MutableList<DocumentListDataItem> = mutableListOf(),
    private val listener: DocumentListAdapter.OnItemSelected
) : RecyclerView.Adapter<DocumentListAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_document_list,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        holder.bindData(mContext, data, listener)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: DocumentListDataItem)
        fun editDocument(position: Int, data: DocumentListDataItem)
        fun deleteDocument(position: Int, data: DocumentListDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: DocumentListDataItem,
            listener: DocumentListAdapter.OnItemSelected
        ) {
            Glide.with(context).load(Constant.DOCUMENT_URL + data.document)
                .placeholder(R.drawable.ic_document).into(imgProfile)
            txtTitle.text = data.title

            /*  val builder: Zoomy.Builder = Zoomy.Builder(context as Activity).target(itemView)
              builder.register()*/


            val namepass: Array<String> = data.document!!.split(".").toTypedArray()

            if (namepass[namepass.size - 1].toLowerCase().equals("jpg") ||
                namepass[namepass.size - 1].toLowerCase().equals("png") ||
                namepass[namepass.size - 1].toLowerCase().equals("jpeg") ||
                namepass[namepass.size - 1].toLowerCase().equals("PNG")
            ) {
                Glide.with(context).load(Constant.DOCUMENT_URL + data.document)
                    .placeholder(R.drawable.ic_document).into(imgProfile)

                txtIcon.hide()
                imgProfile.visible()
            } else {


                imgProfile.hide()
                txtIcon.visible()
                Glide.with(context).load(R.drawable.ic_selected_doc)
                    .placeholder(R.drawable.ic_document).into(txtIcon)
            }

            txtIcon.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
            imgProfile.setOnClickListener { listener.onItemSelect(adapterPosition, data) }
            imgEdit.setOnClickListener { listener.editDocument(adapterPosition, data) }
            imgDelete.setOnClickListener { listener.deleteDocument(adapterPosition, data) }
        }

    }
}