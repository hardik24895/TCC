package com.tcc.app.activity


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.dialog.ImagePickerBottomSheetDialog
import com.tcc.app.extention.*
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.modal.DocumentListDataItem
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.yalantis.ucrop.UCrop
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_document.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.txtTitle
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditUploadDocument : BaseActivity() {

    var resultUri: Uri? = null
    var resultUriProfile: Uri? = null
    var document: DocumentListDataItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_document)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }

        if (intent.hasExtra(Constant.DATA)) {
            document = intent.getSerializableExtra(Constant.DATA) as DocumentListDataItem
        }
        txtTitle.text = "Edit Document"

        Glide.with(this).load(Constant.DOCUMENT_URL + document?.document)
            .placeholder(R.drawable.ic_document).into(imgDocument)

        edtTitle.setText(document?.title)


        imgDocument.setOnClickListener {

            val dialog = ImagePickerBottomSheetDialog
                .newInstance(
                    this,
                    object : ImagePickerBottomSheetDialog.OnModeSelected {
                        override fun onMediaPicked(uri: Uri) {
                            val destinationUri = Uri.fromFile(
                                File(
                                    cacheDir,
                                    "IMG_" + System.currentTimeMillis()
                                )
                            )
                            UCrop.of(uri, destinationUri)
                                .withAspectRatio(1f, 1f)
                                .start(this@EditUploadDocument, 1)
                        }

                        override fun onError(message: String) {
                            showAlert(message)
                        }
                    })
            dialog.show(supportFragmentManager, "ImagePicker")
        }




        btnSubmit.setOnClickListener {
            validation()
        }

    }


    fun validation() {
        when {
            resultUriProfile == null -> {
                root.showSnackBar("Upload  Image")
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == UCrop.REQUEST_CROP -> {
                if (resultCode == Activity.RESULT_OK) {
                    resultUri = UCrop.getOutput(data!!)
                }
            }
            requestCode == 1 -> {
                resultUriProfile = UCrop.getOutput(data!!)
                Glide.with(this)
                    .load(resultUriProfile)
                    .apply(
                        com.bumptech.glide.request.RequestOptions().centerCrop()
                            .placeholder(com.tcc.app.R.drawable.ic_profile)
                    )
                    .into(imgDocument)

            }

        }
    }


    fun AddDocument() {
        showProgressbar()

        val requestFile: RequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), File(resultUriProfile?.path))
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            "ImageData",
            File(resultUriProfile?.path).name + ".jpg",
            requestFile
        )


        val methodName: RequestBody =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                Constant.METHOD_EDIT_CUSTOMER_SITE_DOCUMENT
            )

        Networking
            .with(this)
            .getServices()
            .EditCustomerSiteDocument(
                body,
                methodName,
                RequestBody.create("text/plain".toMediaTypeOrNull(), session.user.data?.userID.toString()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtTitle.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), document?.sitesID.toString()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), document?.customerSitesDocumentID.toString())

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