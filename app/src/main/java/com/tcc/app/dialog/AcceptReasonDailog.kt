package com.tcc.app.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.akexorcist.snaptimepicker.TimeValue
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.network.AutoDisposable
import com.tcc.app.utils.BlurDialogFragment
import com.tcc.app.utils.Logger
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
        edtStartTime.setText(getCurentTime(getCurrentDateTime()))

        edtStartDate.setOnClickListener {
            showDateTimePicker(requireActivity(), edtStartDate)
        }
        edtEndDate.setOnClickListener {
            showDateTimePicker(
                requireActivity(), edtEndDate
            )
        }

        edtEndDate.setOnClickListener {
            showNextFromStartDateTimePicker(
                requireActivity(),
                edtEndDate,
                edtStartDate.getValue()
            )
        }

        edtStartTime.setOnClickListener {

            val namepass: Array<String> = edtStartTime.getValue().split(":").toTypedArray()
            val hours = namepass[0]
            val minut = namepass[1]

            SnapTimePickerDialog.Builder().setThemeColor(R.color.colorPrimary).apply {
                setPreselectedTime(TimeValue(hours.toInt(), minut.toInt()))

                setTitle(R.string.reminder_time)
            }.build().apply {

                setListener { hour, minute ->
                    Logger.d("time", hour.toString() + ":" + minute.toString())
                    var edtStartTime: EditText = view.findViewById(R.id.edtStartTime)
                    edtStartTime.setText(
                        convertIntoTowDigit(hour) + ":" + convertIntoTowDigit(
                            minute
                        )
                    )


                }
            }.show(childFragmentManager, "")
        }

        edtStartDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                edtEndDate.setText(edtStartDate.getValue())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        btnSubmit.setOnClickListener {
            listener.onItemCLicked(
                edtStartDate.getValue(),
                edtEndDate.getValue(),
                edtStartTime.getValue()
            )
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
        fun onItemCLicked(startDate: String, endDate: String, startTime: String)
    }

    interface onDissmiss {
        fun onDismiss()
    }


}








