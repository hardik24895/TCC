package com.tcc.app.dialog

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tcc.app.R
import com.tcc.app.ui.dialog.BaseBottomSheetDialogFragment


class AddressListBottomSheetDialog(context: Context) : BaseBottomSheetDialogFragment(),
    View.OnClickListener {
    private var lastClickTime: Long = 0

    companion object {
        private lateinit var oniteamClick: OnItemClick

        fun newInstance(
            context: Context,
            listener: OnItemClick
        ): AddressListBottomSheetDialog {
            this.oniteamClick = listener
            return AddressListBottomSheetDialog(context)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_dialog_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(view: View) {
        preventDoubleClick(view)


    }

    private fun populateData() {
        val bundle = arguments
        if (bundle != null) {

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