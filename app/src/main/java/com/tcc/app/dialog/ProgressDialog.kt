package com.tcc.app.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import com.tcc.app.R
import kotlinx.android.synthetic.main.common_dialog_progress.*


/**
 * Created by Rajesh Kushvaha
 */

class ProgressDialog @JvmOverloads constructor(
    context: Context,
    private var message: String? = null
) :
    AlertDialog(context, R.style.ProgressDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_dialog_progress)
        setCanceledOnTouchOutside(false)
        setCancelable(false)

        if (message != null) tvMessage.text = message
    }

    fun updateMessage(message: String) {
        tvMessage.text = message
    }

    fun getProgressBar(): ProgressBar {
        return progressBar
    }
}
