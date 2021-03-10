package com.tcc.app.adapter

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import android.widget.TextView.BufferType
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.extention.getRandomMaterialColor
import com.tcc.app.extention.invisible
import com.tcc.app.extention.openPDF
import com.tcc.app.extention.visible
import com.tcc.app.modal.InvoiceDataItem
import com.tcc.app.utils.Constant
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_invoice.*


class InvoicePaidAdapter(
    private val mContext: Context,
    var list: MutableList<InvoiceDataItem> = mutableListOf(),
    var isPaid: Boolean = false,
    var flag: String,
    private val listener: InvoicePaidAdapter.OnItemSelected
) : RecyclerView.Adapter<InvoicePaidAdapter.ItemHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.row_invoice,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]

        if (isPaid) {
            holder.txtNotes.invisible()
            holder.btnPay.invisible()
        } else {
            holder.txtNotes.visible()
            makeTextViewResizable(holder.txtNotes, 1, "View More", true)
        }

        holder.bindData(mContext, data, listener, isPaid, flag)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: InvoiceDataItem)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bindData(
            context: Context,
            data: InvoiceDataItem,
            listener: InvoicePaidAdapter.OnItemSelected,
            isPaid: Boolean,
            flag: String,
        ) {

            txtName.text = data.siteUserFrindlyName
            txtInvoiceno.text = data.invoiceNo
            txtCgst.text = data.cGST
            txtEstimateno.text = data.estimateNo
            txtInvoiceDate.text = data.invoiceDate
            txtSgst.text = data.sGST
            txtNotes.text = data.notes
            txtTerms.text = data.terms
            txtSubtotal.text = data.subTotal
            txtTotal.text = data.totalAmount


            if (!flag.equals("Paid")) {
                crd_remaining.visible()
                texRemainingPayment.text = data.remainingPayment
                texRemainingGST.text = data.remainingGSTPayment
            } else {
                crd_remaining.invisible()
            }



            imgProfile.setImageResource(R.drawable.bg_circle)
            imgProfile.setColorFilter(getRandomMaterialColor("400", context))
            txtIcon.text = data.siteUserFrindlyName.toString().substring(0, 1)
            txtIcon.visible()
            btnPay.setOnClickListener { listener.onItemSelect(adapterPosition, data) }

            imgPrint.setOnClickListener {
                openPDF(
                    Constant.PDF_INVOICE_URL + data.document,
                    context
                )
            }
        }


    }

    fun makeTextViewResizable(
        tv: TextView,
        maxLine: Int,
        expandText: String,
        viewMore: Boolean
    ) {
        if (tv.tag == null) {
            tv.tag = tv.text
        }
        val vto = tv.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val text: String
                val lineEndIndex: Int
                val obs = tv.viewTreeObserver
                obs.removeGlobalOnLayoutListener(this)
                if (maxLine == 0) {
                    lineEndIndex = tv.layout.getLineEnd(0)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                } else if (maxLine > 0 && tv.lineCount >= maxLine) {
                    lineEndIndex = tv.layout.getLineEnd(maxLine - 1)
                    text = tv.text.subSequence(0, lineEndIndex - expandText.length + 1)
                        .toString() + " " + expandText
                } else {
                    lineEndIndex = tv.layout.getLineEnd(tv.layout.lineCount - 1)
                    text = tv.text.subSequence(0, lineEndIndex).toString() + " " + expandText
                }
                tv.text = text
                tv.movementMethod = LinkMovementMethod.getInstance()
                tv.setText(
                    addClickablePartTextViewResizable(
                        Html.fromHtml(tv.text.toString()), tv, lineEndIndex, expandText,
                        viewMore
                    ), BufferType.SPANNABLE
                )
            }
        })
    }

    private fun addClickablePartTextViewResizable(
        strSpanned: Spanned, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    tv.layoutParams = tv.layoutParams
                    tv.setText(tv.tag.toString(), BufferType.SPANNABLE)
                    tv.invalidate()
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false)
                    } else {
                        makeTextViewResizable(tv, 1, "View More", true)
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }
}