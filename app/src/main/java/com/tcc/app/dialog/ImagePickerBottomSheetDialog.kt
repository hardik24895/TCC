package com.tcc.app.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tcc.app.R
import com.tcc.app.ui.dialog.BaseBottomSheetDialogFragment
import com.tcc.app.utils.picker.RxImagePicker
import com.tcc.app.utils.picker.Sources
import kotlinx.android.synthetic.main.common_dialog_image_picker.*

class ImagePickerBottomSheetDialog(context: Context) : BaseBottomSheetDialogFragment(),
    View.OnClickListener {
    private var lastClickTime: Long = 0

    companion object {
        private lateinit var onModeSelected: OnModeSelected

        fun newInstance(
            context: Context,
            listener: OnModeSelected
        ): ImagePickerBottomSheetDialog {
            onModeSelected = listener
            return ImagePickerBottomSheetDialog(context)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.common_dialog_image_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCamera.setOnClickListener(this)
        tvGallery.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        preventDoubleClick(view)
        dismiss()
        when (view.id) {
            R.id.tvCamera -> {
                pickImageFromSource(Sources.CAMERA)
            }
            R.id.tvGallery -> {
                pickImageFromSource(Sources.GALLERY)
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun pickImageFromSource(source: Sources) {
        val activity = context as AppCompatActivity
        val manager = activity.supportFragmentManager

        RxImagePicker.with(manager)
            .requestImage(source)
            .subscribe({
                onModeSelected.onMediaPicked(it)
            }, {
                it.message?.let { it1 -> onModeSelected.onError(it1) }
            })
    }

    private fun preventDoubleClick(view: View) {
        // preventing double, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }

    interface OnModeSelected {
        fun onMediaPicked(uri: Uri)
        fun onError(message: String)
    }
}