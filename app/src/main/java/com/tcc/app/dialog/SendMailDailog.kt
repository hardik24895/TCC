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
import com.tcc.app.extention.getValue
import com.tcc.app.network.AutoDisposable
import com.tcc.app.utils.BlurDialogFragment
import com.tcc.app.utils.SessionManager
import kotlinx.android.synthetic.main.dialog_send_email.*


class SendMailDailog(context: Context) : BlurDialogFragment(), LifecycleOwner {
    private val autoDisposable = AutoDisposable()
    private lateinit var session: SessionManager



    companion object {
        private lateinit var listener: onItemClick
        private lateinit var Lead: String
        private lateinit var Email: String
        fun newInstance(
            context: Context,
            Lead: String,
            Email: String,
            listeners: onItemClick
        ): SendMailDailog {
            this.listener = listeners
            this.Lead = Lead
            this.Email = Email
            return SendMailDailog(context)
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
        return inflater.inflate(R.layout.dialog_send_email, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        edtLead.setText(Lead)
        edtEmail.setText(Email)


        btnSubmit.setOnClickListener {

            validation()

        }


    }

    fun validation() {
        when {
            edtSubject.getValue().isEmpty() -> {
                edtSubject.setError("Enter Subject")
                edtSubject.requestFocus()

            }
            edtDescription.getValue().isEmpty() -> {
                edtDescription.setError("Enter Description")
                edtDescription.requestFocus()
            }
            else -> {
                listener.onItemCLicked(edtSubject.getValue(), edtDescription.getValue())
                dismissAllowingStateLoss()
            }


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








