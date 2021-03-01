package com.tcc.app.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.dialog.ImagePickerBottomSheetDialog
import com.tcc.app.extention.*
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.GSTINValidator
import com.tcc.app.utils.Logger
import com.tcc.app.utils.TimeStamp.formatDateFromString
import com.yalantis.ucrop.UCrop
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_employee.*
import kotlinx.android.synthetic.main.activity_add_employee.btnSubmit
import kotlinx.android.synthetic.main.activity_add_employee.edtAddress
import kotlinx.android.synthetic.main.activity_add_employee.edtGST
import kotlinx.android.synthetic.main.activity_add_employee.root
import kotlinx.android.synthetic.main.activity_add_employee.spCity
import kotlinx.android.synthetic.main.activity_add_site.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.io.File

class AddEmployeeActivity : BaseActivity() {

    var resultUri: Uri? = null
    var resultUriProfile: Uri? = null
    var resultUriAdhar: Uri? = null
    var resultUriOfferLetter: Uri? = null
    var cityNameList: ArrayList<String>? = null
    var cityListArray: ArrayList<CityDataItem>? = null
    var userTypeNameList: ArrayList<String>? = null
    var userTypeListArray: ArrayList<UserTypeDataItem>? = null
    var adapterCity: ArrayAdapter<String>? = null
    var itemCity: List<SearchableItem>? = null
    var itemUserType: List<SearchableItem>? = null
    var usertypeId: String = ""
    var cityId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_employee)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        cityNameList = ArrayList()
        cityListArray = ArrayList()
        userTypeNameList = ArrayList()
        userTypeListArray = ArrayList()

        txtTitle.text = getString(R.string.employee)
        edtJoiningDate.setText(getCurrentDate())
        edtJoiningDate.setOnClickListener {
            showDateTimePicker(this@AddEmployeeActivity, edtJoiningDate)
        }
        getCityList()
        getUserTypeList()

        ivUpload.setOnClickListener {

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
                                .start(this@AddEmployeeActivity, 1)
                        }

                        override fun onError(message: String) {
                            showAlert(message)
                        }
                    })
            dialog.show(supportFragmentManager, "ImagePicker")
        }
        ivSelectAdharImage.setOnClickListener {

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
                                .start(this@AddEmployeeActivity, 2)
                        }

                        override fun onError(message: String) {
                            showAlert(message)
                        }
                    })
            dialog.show(supportFragmentManager, "ImagePicker")
        }
        ivSelectOfferLetter.setOnClickListener {

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
                                .start(this@AddEmployeeActivity, 3)
                        }

                        override fun onError(message: String) {
                            showAlert(message)
                        }
                    })
            dialog.show(supportFragmentManager, "ImagePicker")
        }

        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && cityListArray!!.size > position) {
                    //session.storeDataByKey(SessionManager.KEY_CITY_ID, cityListArray!!  .get(position).cityID.toString())
                    cityId = cityListArray!!.get(position).cityID.toString()

                }

            }
        }

        viewCity.setOnClickListener {

            SearchableDialog(this@AddEmployeeActivity,
                itemCity!!,
                getString(R.string.select_city),
                { item, _ ->
                    spCity.setSelection(item.id.toInt())
                }).show()
        }

        spUserType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && userTypeListArray!!.size > position) {
                    usertypeId = userTypeListArray!!.get(position).usertypeID.toString()

                }

            }
        }

        viewUserType.setOnClickListener {

            SearchableDialog(this@AddEmployeeActivity,
                itemUserType!!,
                getString(R.string.select_usertype), { item, _ ->
                    spUserType.setSelection(item.id.toInt())
                }).show()
        }

        btnSubmit.setOnClickListener {
            validation()
        }

    }


    fun validation() {

        when {
            resultUriProfile == null -> {
                root.showSnackBar("Upload Profile Image")
            }
            resultUriAdhar == null -> {
                root.showSnackBar("Upload Adhaar Card Image")
            }
            resultUriOfferLetter == null -> {
                root.showSnackBar("Upload Offer Letter Image")
            }

            edtFirstName.isEmpty() -> {
                root.showSnackBar("Enter First Name")
                edtFirstName.requestFocus()
            }
            edtLastName.isEmpty() -> {
                root.showSnackBar("Enter Last Name")
                edtLastName.requestFocus()
            }
            edtEmail.length() > 0 -> {
                if (!isValidEmail(edtEmail.getValue())) {
                    root.showSnackBar("Enter valid email")
                    edtEmail.requestFocus()
                }
            }
            edtMobile.isEmpty() -> {
                root.showSnackBar("Enter Mobile No.")
                edtMobile.requestFocus()
            }
            edtMobile.getValue().length < 10 -> {
                root.showSnackBar("Enter Valid Mobile No.")
                edtMobile.requestFocus()
            }
            edtPassword.isEmpty() -> {
                root.showSnackBar("Enter Password")
                edtPassword.requestFocus()
            }
            edtPassword.getValue().length < 6 -> {
                root.showSnackBar("Enter Minimum six character")
                edtPassword.requestFocus()
            }
            !edtPassword.getValue().equals(edtConfirmPassword.getValue()) -> {
                edtConfirmPassword.requestFocus()
                root.showSnackBar("Password and Confirm Password not matched")
            }
            edtAddress.isEmpty() -> {
                edtAddress.requestFocus()
                root.showSnackBar("Enter Address")
            }
            edAddSalary.isEmpty() -> {
                root.showSnackBar("Enter Salary")
                edAddSalary.requestFocus()
            }
            edtWorkingHours.isEmpty() -> {
                root.showSnackBar("Enter Working Hrs")
                edtWorkingHours.requestFocus()
            }
            !edtGST.isEmpty() -> {
                /*  root.showSnackBar("Enter GST")
                  edtGST.requestFocus()*/
                if (!GSTINValidator.validGSTIN(edtGST.getValue())) {
                    root.showSnackBar("Enter Valid GST No.")
                    edtGST.requestFocus()
                }


            }


            else -> {
                AddEmployee()
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
                    .into(circleImageView2)

            }

            requestCode == 2 -> {
                resultUriAdhar = UCrop.getOutput(data!!)
                Glide.with(this)
                    .load(resultUriAdhar)
                    .apply(
                        com.bumptech.glide.request.RequestOptions().centerCrop()
                            .placeholder(R.drawable.ic_add_btn)
                    )
                    .into(ivSelectAdharImage)

            }
            requestCode == 3 -> {
                resultUriOfferLetter = UCrop.getOutput(data!!)
                Glide.with(this)
                    .load(resultUriOfferLetter)
                    .apply(
                        com.bumptech.glide.request.RequestOptions().centerCrop()
                            .placeholder(R.drawable.ic_add_btn)
                    )
                    .into(ivSelectOfferLetter)
            }
        }
    }

    fun getCityList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", "-1")

            result = Networking.setParentJsonData(Constant.METHOD_CITY_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddEmployeeActivity)
            .getServices()
            .getCityList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CityListModel>() {
                override fun onSuccess(response: CityListModel) {
                    cityListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    for (items in response.data.indices) {
                        cityNameList!!.add(response.data.get(items).cityName.toString())
                        myList.add(SearchableItem(items.toLong(), cityNameList!!.get(items)))

                    }
                    itemCity = myList

                    adapterCity = ArrayAdapter(
                        this@AddEmployeeActivity,
                        R.layout.custom_spinner_item,
                        cityNameList!!
                    )
                    spCity.setAdapter(adapterCity)


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }

    fun getUserTypeList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", "-1")

            result = Networking.setParentJsonData(Constant.METHOD_USERTYPE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddEmployeeActivity)
            .getServices()
            .getUserTypeList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<UserTypeListModel>() {
                override fun onSuccess(response: UserTypeListModel) {
                    userTypeListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    for (items in response.data.indices) {
                        userTypeNameList!!.add(response.data.get(items).usertype.toString())
                        myList.add(SearchableItem(items.toLong(), userTypeNameList!!.get(items)))

                    }
                    itemUserType = myList

                    adapterCity = ArrayAdapter(
                        this@AddEmployeeActivity,
                        R.layout.custom_spinner_item,
                        userTypeNameList!!
                    )
                    spUserType.setAdapter(adapterCity)


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }


    fun AddEmployee() {
        showProgressbar()

        val requestFile: RequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), File(resultUriProfile?.path))
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            "ImageData",
            File(resultUriProfile?.path).name + ".jpg",
            requestFile
        )


        val requestFile1: RequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), File(resultUriAdhar?.path))
        val body1: MultipartBody.Part = MultipartBody.Part.createFormData(
            "DocumentImageData",
            File(resultUriAdhar?.path).name + ".jpg",
            requestFile1
        )

        val requestFile2: RequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), File(resultUriOfferLetter?.path))
        val body2: MultipartBody.Part = MultipartBody.Part.createFormData(
            "OfferletterData",
            File(resultUriOfferLetter?.path).name + ".jpg",
            requestFile2
        )


        val methodName: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), "addEmployee")

        Networking
            .with(this)
            .getServices()
            .AddEmployeeApi(
                body,
                body1,
                body2,
                methodName,
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtFirstName.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtLastName.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtEmail.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtPassword.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtMobile.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtAddress.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), usertypeId),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edAddSalary.getValue()),
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    formatDateFromString(edtJoiningDate.getValue())
                ),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtWorkingHours.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtBankName.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtBranchName.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtAccountNumber.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtIfsc.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtGST.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), cityId)

            )//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
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