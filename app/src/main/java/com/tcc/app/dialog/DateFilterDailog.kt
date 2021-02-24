package com.tcc.app.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.tcc.app.R
import com.tcc.app.extention.getCurrentDate
import com.tcc.app.extention.getValue
import com.tcc.app.extention.showDateTimePicker
import com.tcc.app.network.AutoDisposable
import com.tcc.app.utils.BlurDialogFragment
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp
import kotlinx.android.synthetic.main.dialog_date_filter.*


class DateFilterDailog(context: Context) : BlurDialogFragment(), LifecycleOwner {
    private val autoDisposable = AutoDisposable()
    private lateinit var session: SessionManager


    companion object {
        private lateinit var listener: onItemClick
        fun newInstance(
            context: Context,
            listeners: onItemClick
        ): DateFilterDailog {
            this.listener = listeners
            return DateFilterDailog(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Custom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        session = SessionManager(requireContext())
        autoDisposable.bindTo(this.lifecycle)
        return inflater.inflate(R.layout.dialog_date_filter, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        btnSubmit.setOnClickListener {
            listener.onItemCLicked(edtStartDate.getValue(), edtEndDate.getValue())
            dismissAllowingStateLoss()
        }
        btn_close.setOnClickListener {
            dismissAllowingStateLoss()
        }

        edtStartDate.setText(TimeStamp.getStartDateRange())
        edtEndDate.setText(getCurrentDate())

        edtStartDate.setOnClickListener { showDateTimePicker(requireActivity(), edtStartDate) }

        edtEndDate.setOnClickListener { showDateTimePicker(requireActivity(), edtEndDate) }

    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    private fun populateData() {
        val bundle = arguments
        if (bundle != null) {
            /* val title = bundle.getString(Constant.TITLE)
             val text = bundle.getString(Constant.TEXT)
             txtTitle.text = title
             tvText.text = text*/
        }
    }

    interface onItemClick {
        fun onItemCLicked( strdate:String, enddate:String)
    }

    interface onDissmiss {
        fun onDismiss()
    }
}








