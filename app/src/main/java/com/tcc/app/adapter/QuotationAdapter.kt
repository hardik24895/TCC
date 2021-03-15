package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.invisible
import com.tcc.app.extention.openPDF
import com.tcc.app.extention.visible
import com.tcc.app.modal.QuotationItem
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_quoatation.*

class QuotationAdapter(
    private val mContext: Context,
    var list: MutableList<QuotationItem> = mutableListOf(),
    val isAccept: String,
    val session: SessionManager,
    private val listener: OnItemSelected
) : RecyclerView.Adapter<QuotationAdapter.ItemHolder>() {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(R.layout.row_quoatation, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        if (isAccept.equals("Accept")) {
            holder.constrain_accepted.visible()
            holder.constrain_pending.invisible()
        } else if (isAccept.equals("Pending")) {
            holder.constrain_pending.visible()
            holder.constrain_accepted.invisible()
        } else {
            holder.constrain_accepted.invisible()
            holder.constrain_pending.invisible()
        }

        if (session.roleData.data?.invoice?.isInsert.toString().equals("0"))
            holder.btnInvoice.invisible()

        holder.bindData(mContext, data, listener, isAccept)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: QuotationItem, action: String)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            data: QuotationItem,
            listener: QuotationAdapter.OnItemSelected,
            isAccept: String
        ) {
            txtCompanyName.text = data.name
            txtSiteName.text = data.siteName
            txtEstinationNo.text = data.estimateNo
            txtHRS.text = data.service
            //txtName.text = data.name
            txtStatus.text = data.quotationStatus

            if (data.cGST.equals(""))
                txtCGST.text = "N/A"
            else
                txtCGST.text = data.cGST

            if (data.sGST.equals(""))
                txtSGST.text = "N/A"
            else
                txtSGST.text = data.sGST


            if (data.iGST.equals(""))
                txtIGST.text = "N/A"
            else
                txtIGST.text = data.iGST

            txtTotal.text = data.total
            if (data.pinCode.equals(""))
                txtAddress.text =
                    data.address + ", " + data.address2 + ",  " + data.cityName + ", " + data.stateName
            else
                txtAddress.text =
                    data.address + ", " + data.address2 + ", " + data.pinCode + ", " + data.cityName + ", " + data.stateName

            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.name.toString().substring(0, 1)
            txtIcon.visible()


            btnAccept.setOnClickListener { listener.onItemSelect(adapterPosition, data, "ACCEPT") }
            btnReject.setOnClickListener { listener.onItemSelect(adapterPosition, data, "REJECT") }
            btnTeamDefination.setOnClickListener {
                listener.onItemSelect(adapterPosition, data, "TEAM-DEFINATION")
            }
            btnInvoice.setOnClickListener {
                listener.onItemSelect(adapterPosition, data, "INVOICE")
            }


            if (data.service?.equals("Deep cleaning")!! || data.service.equals("Sanetize")) {
                btnAttencence.invisible()
                btnTeamDefination.invisible()
            } else {
                btnAttencence.visible()
                btnTeamDefination.visible()
            }

            if (data.teamSize.equals("0")) {
                btnAttencence.invisible()
            } else {
                btnAttencence.visible()
            }

            btnAttencence.setOnClickListener {
                listener.onItemSelect(adapterPosition, data, "ATTENDANCE")
            }

            imgPrint.setOnClickListener {
                openPDF(
                    Constant.PDF_QUOTATION_URL + data.document,
                    context
                )
            }

        }

    }


}