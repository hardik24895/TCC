package com.tcc.app.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
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
import com.tcc.app.utils.TimeStamp
import com.yalantis.ucrop.UCrop
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


import kotlinx.android.synthetic.main.activity_add_ticket.*
import kotlinx.android.synthetic.main.activity_add_ticket.rg
import kotlinx.android.synthetic.main.activity_add_ticket.root
import kotlinx.android.synthetic.main.activity_add_ticket.spCity
import kotlinx.android.synthetic.main.activity_add_ticket.spState
import kotlinx.android.synthetic.main.activity_add_ticket.view

import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.io.File

class AddTicketActivity : BaseActivity() {

    var resultUri: Uri? = null
    var resultUriProfile: Uri? = null
    var cityNameList: ArrayList<String>? = null
    var cityListArray: ArrayList<CityDataItem>? = null
    var adapterCity: ArrayAdapter<String>? = null
    var itemCity: List<SearchableItem>? = null
    var cityId: String = ""
    var stateNameList: ArrayList<String> = ArrayList()
    var adapterState: ArrayAdapter<String>? = null
    var stateIteams: List<SearchableItem>? = null
    var stateID: String = "-1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_ticket)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        cityNameList = ArrayList()
        cityListArray = ArrayList()
        rbHigh.isChecked = true


        txtTitle.text = "Add Ticket"

        getCityList(stateID)


        ivSelectImage.setOnClickListener {

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
                                .start(this@AddTicketActivity, 1)
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

            SearchableDialog(this@AddTicketActivity,
                itemCity!!,
                getString(R.string.select_city),
                { item, _ ->
                    spCity.setSelection(item.id.toInt())
                }).show()
        }

        getStateSppinerData()
        stateSpinnerListner()

        stateViewClick()


        btnSubmit.setOnClickListener {
            validation()
        }

    }

    fun stateSpinnerListner() {
        spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && stateNameList.size > position) {
                    stateID = session.stetList.get(position).stateID.toString()
                    Logger.d("state", stateID)
                    getCityList(stateID)
                }

            }
        }
    }

    fun stateViewClick() {
        view.setOnClickListener {

            SearchableDialog(this,
                stateIteams!!,
                getString(R.string.select_state),
                { item, _ ->
                    spState.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun getStateSppinerData() {
        var myList: MutableList<SearchableItem> = mutableListOf()
        for (i in session.stetList.indices) {
            stateNameList.add(session.stetList.get(i).stateName.toString())
            myList.add(SearchableItem(i.toLong(), stateNameList.get(i)))
        }
        stateIteams = myList

        adapterState = ArrayAdapter(this, R.layout.custom_spinner_item, stateNameList)
        spState.setAdapter(adapterState)

        for (i in session.stetList.indices) {
            if (session.stetList.get(i).stateID.toString().equals(stateID)) {
                spState.setSelection(i)
            }

        }

        if (stateID.equals("-1")) {
            spState.setSelection(11)
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
            edtDescription.isEmpty() -> {
                root.showSnackBar("Enter Description")
                edtDescription.requestFocus()
            }
            else -> {
                AddTicket()
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
                    .into(ivSelectImage)

            }

        }
    }

    fun getCityList(stateID: String) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", stateID)

            result = Networking.setParentJsonData(Constant.METHOD_CITY_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddTicketActivity)
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
                        this@AddTicketActivity,
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


    fun AddTicket() {
        showProgressbar()

        val selectedId: Int = rg.getCheckedRadioButtonId()
        val rbTicket = findViewById<View>(selectedId) as? RadioButton
        
        val requestFile: RequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), File(resultUriProfile?.path))
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            "ImageData",
            File(resultUriProfile?.path).name + ".jpg",
            requestFile
        )


        val methodName: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), Constant.METHOD_ADD_TICKET)

        Networking
            .with(this)
            .getServices()
            .AddTicket(
                body,
                methodName,
                RequestBody.create("text/plain".toMediaTypeOrNull(), session.user.data?.userID.toString()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtTitle.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), edtDescription.getValue()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), rbTicket?.text.toString()),
                RequestBody.create("text/plain".toMediaTypeOrNull(), cityId)

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