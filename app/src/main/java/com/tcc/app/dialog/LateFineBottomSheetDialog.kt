package com.tcc.app.dialog

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.tcc.app.R
import com.tcc.app.ui.dialog.BaseBottomSheetDialogFragment
import com.tcc.app.utils.Logger
import kotlinx.android.synthetic.main.bottom_dialog_late_fine.*


class LateFineBottomSheetDialog(context: Context) : BaseBottomSheetDialogFragment(),
    View.OnClickListener {
    private var lastClickTime: Long = 0

    companion object {
        private lateinit var oniteamClick: OnItemClick

        fun newInstance(
            context: Context,
            listener: OnItemClick
        ): LateFineBottomSheetDialog {
            this.oniteamClick = listener
            return LateFineBottomSheetDialog(context)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_late_fine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edtLateFine.setOnClickListener(this)
        //  tvGallery.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        preventDoubleClick(view)
        // dismiss()
        when (view.id) {
            R.id.edtLateFine -> {
                SnapTimePickerDialog.Builder().apply {
                    setTitle(R.string.late_fine)
                }.build().apply {
                    setListener { hour, minute ->
                        Logger.d("time", hour.toString() + ":" + minute.toString())
                        val edit = view.findViewById<View>(R.id.edtLateFine) as EditText
                        edit.setText(hour.toString() + " Hrs " + " : " + minute.toString() + " Min")
                    }
                }.show(childFragmentManager, tag)

                // pickImageFromSource(Sources.CAMERA)
            }
//           R.id.tvGallery -> {
//                pickImageFromSource(Sources.GALLERY)
//            }
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