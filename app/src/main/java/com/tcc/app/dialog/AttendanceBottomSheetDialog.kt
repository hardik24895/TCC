package com.tcc.app.dialog

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tcc.app.R
import com.tcc.app.ui.dialog.BaseBottomSheetDialogFragment
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.bottom_dailog_attendance.*

class AttendanceBottomSheetDialog(context: Context) : BaseBottomSheetDialogFragment(),
    View.OnClickListener {
    private var lastClickTime: Long = 0

    companion object {
        private lateinit var oniteamClick: OnItemClick

        fun newInstance(
            context: Context,
            listener: OnItemClick
        ): AttendanceBottomSheetDialog {
            this.oniteamClick = listener
            return AttendanceBottomSheetDialog(context)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dailog_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtOvertime.setOnClickListener(this)
        txtLatefine.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        preventDoubleClick(view)
        dismiss()
        when (view.id) {
            R.id.txtOvertime -> {
                oniteamClick.onItemClicked(Constant.OVERTIME)
                dismissAllowingStateLoss()
            }
            R.id.txtLatefine -> {
                oniteamClick.onItemClicked(Constant.LATEFINE)
                dismissAllowingStateLoss()
            }
        }
    }


    private fun preventDoubleClick(view: View) {
        // preventing double, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }

    interface OnItemClick {
        fun onItemClicked(message: String)
        fun onError(message: String)
    }
}