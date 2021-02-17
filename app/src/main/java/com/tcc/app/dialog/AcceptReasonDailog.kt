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
import kotlinx.android.synthetic.main.dialog_accept_reason.*
import kotlinx.android.synthetic.main.dialog_reject_reason.btnSubmit


class AcceptReasonDailog(context: Context) : BlurDialogFragment(), LifecycleOwner {
    private val autoDisposable = AutoDisposable()
    private lateinit var session: SessionManager


    companion object {
        private lateinit var listener: onItemClick
        fun newInstance(
            context: Context,
            listeners: onItemClick
        ): AcceptReasonDailog {
            this.listener = listeners
            return AcceptReasonDailog(context)
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
        return inflater.inflate(R.layout.dialog_accept_reason, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        edtStartDate.setText(getCurrentDate())
        edtEndDate.setText(getCurrentDate())

        edtStartDate.setOnClickListener {
            showDateTimePicker(requireActivity(), edtStartDate)
        }
        edtEndDate.setOnClickListener {
            showDateTimePicker(
                requireActivity(), edtEndDate
            )
        }

        btnSubmit.setOnClickListener {
            listener.onItemCLicked(edtStartDate.getValue(), edtEndDate.getValue())
            dismissAllowingStateLoss()
        }


    }

    fun validation() {
        when {
            // edtPhone.isEmpty() -> {
            //     root.showSnackBar("Enter Mobile Number")
            //     edtPhone.requestFocus()
            // }
            // edtPhone.getValue().length < 10 -> {
            //     root.showSnackBar("Enter Valid Mobile Number")
            //     edtPhone.requestFocus()
            // }
            // else -> {
            //     listener.onItemCLicked(edtPhone.getValue())
            //     dismissAllowingStateLoss()
            // }

        }
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
        fun onItemCLicked(startDate: String, endDate: String)
    }

    interface onDissmiss {
        fun onDismiss()
    }


}







