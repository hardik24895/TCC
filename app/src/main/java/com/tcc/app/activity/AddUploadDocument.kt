package com.tcc.app.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.modal.SiteListItem
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_document.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class AddUploadDocument : BaseActivity() {

    var resultUri: Uri? = null
    var siteListItem: SiteListItem? = null
    var path = ""
    var file: File? = null
    private val REQUEST_STORAGE = 201


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_document)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }

        if (intent.hasExtra(Constant.DATA)) {
            siteListItem = intent.getSerializableExtra(Constant.DATA) as SiteListItem
        }
        txtTitle.text = "Upload Document"

        imgDocument.setOnClickListener {
            checkPermission()

        }
        btnSubmit.setOnClickListener {
            validation()
        }

    }


    fun checkPermission() {
        askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
            selectImageFromStorage()
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this).setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .show();
            }

            if (e.hasForeverDenied()) {
                AlertDialog.Builder(this).setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.goToSettings()
                    } //ask again
                    .show();
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        selectImageFromStorage()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun selectImageFromStorage() {

        var intent: Intent? = null
        if (android.os.Build.MANUFACTURER.equals("samsung")) {
            intent = Intent("com.sec.android.app.myfiles.PICK_DATA")
            intent.putExtra("CONTENT_TYPE", "*/*")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
        } else {

            val mimeTypes = arrayOf(
                "application/pdf",
                "image/jpeg",
                "image/jpg",
                "image/png",
                "image/PNG",
                "application/msword",
                "application/doc",
                "application/docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            )


            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("*/*")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        }
        startActivityForResult(intent, REQUEST_STORAGE)
    }


    fun validation() {
        when {
            resultUri == null -> {
                root.showSnackBar("Upload Document")
            }

            edtTitle.isEmpty() -> {
                root.showSnackBar("Enter Title")
                edtTitle.requestFocus()
            }

            else -> {
                AddDocument()
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data1: Intent?) {
        super.onActivityResult(requestCode, resultCode, data1)
        // when {
//            requestCode == UCrop.REQUEST_CROP -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    resultUri = UCrop.getOutput(data!!.getParcelableExtra(EXTRA_OUTPUT_URI))
//                }
//            }
//            requestCode == 1 -> {
//                resultUriProfile = UCrop.getOutput(data!!)
//                Glide.with(this)
//                    .load(resultUriProfile)
//                    .apply(
//                        com.bumptech.glide.request.RequestOptions().centerCrop()
//                            .placeholder(com.rm.enterprise.R.drawable.ic_profile)
//                    )
//                    .into(imgDocument)
//
//            }

        if (resultCode === RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            when (requestCode) {
                REQUEST_STORAGE -> {
                    var selectedFile = File(getRealPathFromURI(data1?.data))


                    var selectedFileExt = selectedFile.extension

                    if (selectedFileExt.toLowerCase().equals("jpg")
                        || selectedFileExt.toLowerCase().equals("png")
                        || selectedFileExt.toLowerCase().equals("jpeg")
                        || selectedFileExt.toLowerCase().equals("PNG")
                        || selectedFileExt.toLowerCase().equals("pdf")
                        || selectedFileExt.toLowerCase().equals("doc")
                        || selectedFileExt.toLowerCase().equals("docs")
                        || selectedFileExt.toLowerCase().equals("xls")
                        || selectedFileExt.toLowerCase().equals("xlsx")
                        || selectedFileExt.toLowerCase().equals("ppt")
                    ) {
                        resultUri = data1?.data

                        if (selectedFileExt.toLowerCase()
                                .equals("jpg") || selectedFileExt.toLowerCase()
                                .equals("png") || selectedFileExt.toLowerCase()
                                .equals("jpeg") || selectedFileExt.toLowerCase().equals("PNG")
                        ) {
                            Glide.with(this)
                                .load(resultUri)
                                .apply(
                                    com.bumptech.glide.request.RequestOptions().centerCrop()
                                        .placeholder(R.drawable.ic_profile)
                                )
                                .into(imgDocument)
                        } else {
                            imgDocument.setImageResource(R.drawable.ic_selected_doc)
                        }


                    } else {
                        root.showSnackBar("This File Format not supported")
                    }
                }
            }
        }
    }


   /* fun getRealPathFromURI(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = this.getContentResolver().query(contentUri!!, proj, null, null, null)
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor!!.moveToFirst()
            cursor!!.getString(column_index)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }*/


    fun getRealPathFromURI(uri: Uri?): String? {
        var path: String? = null
        var image_id: String? = null
        val cursor1 = contentResolver.query(uri!!, null, null, null, null)
        if (cursor1 != null) {
            cursor1.moveToFirst()
            image_id = cursor1.getString(0)
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1)
            cursor1.close()
        }
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Images.Media._ID + " = ? ",
            arrayOf(image_id),
            null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor.close()
        }
        return path
    }

    fun AddDocument() {

        var selectedFile = File(getRealPathFromURI(resultUri))

        val methodName: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            Constant.METHOD_ADD_CUSTOMER_SITE_DOCUMENT
        )
        val requestFile: RequestBody =
            RequestBody.create("*/*".toMediaTypeOrNull(), File(selectedFile.path))
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            "ImageData",
            selectedFile.name,
            requestFile
        )

        Networking
            .with(this)
            .getServices()
            .AddCustomerSiteDocument(
                body,
                methodName,
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    session.user.data?.userID.toString()
                ),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtTitle.getValue()),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    siteListItem?.sitesID.toString()
                )

            )//wrapParams Wraps parameters in to Request body Json format

            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()

                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        finish()
                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    Logger.d("data", message.toString())
                }

            })


    }
}