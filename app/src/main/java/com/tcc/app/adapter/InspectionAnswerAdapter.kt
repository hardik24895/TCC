package com.tcc.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tcc.app.R
import com.tcc.app.modal.InspectionQuesDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.row_inspection_answer_checkbox.*
import kotlinx.android.synthetic.main.row_inspection_answer_radio.*

class InspectionAnswerAdapter(
    private val mContext: Context,
    var quationData: InspectionQuesDataItem,
    var recview: RecyclerView,
    var list: List<String>? = mutableListOf()
) : RecyclerView.Adapter<InspectionAnswerAdapter.ItemHolder>() {
    private var lastRadioPosition: Int = -1
    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        if (quationData.type.equals("Radio"))
            return ItemHolder(
                LayoutInflater.from(mContext)
                    .inflate(R.layout.row_inspection_answer_radio, parent, false)
            )
        else
            return ItemHolder(
                LayoutInflater.from(mContext)
                    .inflate(R.layout.row_inspection_answer_checkbox, parent, false)
            )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list?.get(position)
        holder.bindData(mContext, data.toString(), quationData, lastRadioPosition)

        if (quationData.type.equals("Radio")) {
            holder.rbAnswer?.isChecked = position == lastRadioPosition
            holder.rbAnswer.setOnClickListener {
                quationData.Answer = holder.rbAnswer.text.toString()
                recview.post(Runnable {
                    notifyItemChanged(lastRadioPosition)
                    notifyItemChanged(position)
                    lastRadioPosition = position

                })

            }
        } else {
            holder.cbAnswer.setOnClickListener {


                if (holder.cbAnswer!!.isChecked) {

                    if (quationData.Answer.toString().isEmpty()) {
                        quationData.Answer = holder.cbAnswer.text.toString()
                    } else {
                        quationData.Answer =
                            "${quationData.Answer.toString()},${holder.cbAnswer.text}"
                    }
                } else {


                    var stringArray: List<String> = quationData.Answer.toString().split(',');
                    val arraytwo: ArrayList<String> = ArrayList()
                    for (item in stringArray.indices) {
                        if (!stringArray.get(item).equals(holder.cbAnswer.text.toString())) {
                            arraytwo.add(stringArray.get(item))
                        }
                    }
                    quationData.Answer = arraytwo.joinToString(",")


                }


            }
        }
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: String)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindData(
            context: Context,
            ans: String,
            quationData: InspectionQuesDataItem,
            lastRadioPosition: Int
        ) {
            if (quationData.type.equals("Radio"))
                rbAnswer.setText(ans)
            else
                cbAnswer.setText(ans)
        }

    }

}