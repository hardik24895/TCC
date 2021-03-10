package com.tcc.app.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.adapter.InspectionQuesiontAdapter
import com.tcc.app.dialog.ImagePickerBottomSheetDialog
import com.tcc.app.extention.*
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.NoScrollLinearLayoutManager
import com.tcc.app.utils.SessionManager
import com.yalantis.ucrop.UCrop
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_inspection.*
import kotlinx.android.synthetic.main.activity_add_inspection.btnSubmit
import kotlinx.android.synthetic.main.activity_add_inspection.root
import kotlinx.android.synthetic.main.activity_add_lead_reminder.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.io.File

class AddInspectionActivity : BaseActivity(), InspectionQuesiontAdapter.OnItemSelected {
    var layoutManager: NoScrollLinearLayoutManager? = null
    var QuestionListArray: ArrayList<InspectionQuesDataItem> = ArrayList()
    var adapter: InspectionQuesiontAdapter? = null


    var siteNameList: ArrayList<String>? = ArrayList()
    var adapterSite: ArrayAdapter<String>? = null
    var siteListArray: ArrayList<SiteListItem>? = ArrayList()
    var itemSite: List<SearchableItem>? = null
    var siteId: String = "-1"


    var fieldOperatorNameList: ArrayList<String>? = ArrayList()
    var adapterFieldOperator: ArrayAdapter<String>? = null
    var fieldOperatorListArray: ArrayList<UserByTypeDataItem>? = ArrayList()
    var itemFieldOperator: List<SearchableItem>? = null
    var fieldOperatorId: String = "-1"


    var operationMangerNameList: ArrayList<String>? = ArrayList()
    var adapterOperationManger: ArrayAdapter<String>? = null
    var operationMangerListArray: ArrayList<UserByTypeDataItem>? = ArrayList()
    var itemOperationManger: List<SearchableItem>? = null
    var OperationMangerId: String = "-1"


    var qualityManagerNameList: ArrayList<String>? = ArrayList()
    var adapterQualityManager: ArrayAdapter<String>? = null
    var qualityManagerListArray: ArrayList<UserByTypeDataItem>? = ArrayList()
    var itemQualityManager: List<SearchableItem>? = null
    var QualityManagerId: String = "-1"

    var resultUri: Uri? = null
    var resultUriProfile: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_inspection)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.inspection)
        img.invisible()
        inpRemark.invisible()
        card2.invisible()
        edtDate.setText(getCurrentDate())
        edtDate.setOnClickListener { showDateTimePicker(this@AddInspectionActivity, edtDate) }
        setupRecycler()

        getQuationQuestionList()
        getUserTypeList("FieldOperator")
        getUserTypeList("OperationManager")
        getUserTypeList("QualityManager")
        getSiteList()

        siteSpinnerListner()
        siteViewClick()


        fieldOperatorSpinnerListner()
        fieldOperatorViewClick()

        operationMangerSpinnerListner()
        operationMangerViewClick()

        qualityManagerSpinnerListner()
        qualityManagerViewClick()

        btnSubmit.setOnClickListener {
            goToNextQue()
        }

        img.setOnClickListener {

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
                                .start(this@AddInspectionActivity, 1)
                        }

                        override fun onError(message: String) {
                            showAlert(message)
                        }
                    })
            dialog.show(supportFragmentManager, "ImagePicker")
        }
    }

    private fun siteViewClick() {
        view.setOnClickListener {
            SearchableDialog(this@AddInspectionActivity,
                itemSite!!,
                getString(R.string.select_site), { item, _ ->
                    spSite.setSelection(item.id.toInt())
                }).show()
        }
    }

    private fun siteSpinnerListner() {
        spSite.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && siteListArray!!.size > position - 1) {
                    if (position == 0)
                        siteId = "-1"
                    else
                        siteId = siteListArray!!.get(position).sitesID.toString()
                }

            }
        }
    }

    private fun goToNextQue() {

        if (layoutManager?.findLastCompletelyVisibleItemPosition()!! < (QuestionListArray.size - 1)) {
            val scrollToPosition =
                layoutManager?.scrollToPosition(layoutManager!!.findLastCompletelyVisibleItemPosition() + 1)
            if (layoutManager!!.findLastCompletelyVisibleItemPosition() < 0) {
                linlaySp.visibility = View.VISIBLE
                img.invisible()
                inpRemark.invisible()
                card2.invisible()
                btnSubmit.setText(getString(R.string.next))
            } else if (layoutManager!!.findLastCompletelyVisibleItemPosition() == (QuestionListArray.size - 2)) {
                img.visible()
                inpRemark.visible()
                card2.visible()
                btnSubmit.setText(getString(R.string.submit))

            } else {
                img.invisible()
                inpRemark.invisible()
                card2.invisible()
                btnSubmit.setText(getString(R.string.next))
            }
        } else {

            validation()
        }
    }

    private fun validation() {

        when {
            siteId == "-1" -> {
                root.showSnackBar(getString(R.string.select_site))
            }
            fieldOperatorId == "-1" -> {
                root.showSnackBar(getString(R.string.select_field_operator))
            }
            OperationMangerId == "-1" -> {
                root.showSnackBar(getString(R.string.select_operation_manager))
            }
            QualityManagerId == "-1" -> {
                root.showSnackBar(getString(R.string.select_quality_manager))
            }
            resultUriProfile == null -> {
                root.showSnackBar("Upload Profile Image")
            }
            edtRemark.isEmpty() -> {
                root.showSnackBar("Enter Remark")
                edtMessage.requestFocus()
            }
            else -> {
                AddInspectionData()
            }

        }

    }

    fun setupRecycler() {

        val mSnapHelper: SnapHelper = PagerSnapHelper()
        mSnapHelper.attachToRecyclerView(rvQueAns)
        layoutManager = NoScrollLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        layoutManager!!.setScrollEnabled(false)
        rvQueAns.layoutManager = layoutManager
        adapter = InspectionQuesiontAdapter(this, QuestionListArray, this)
        rvQueAns.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: InspectionQuesDataItem) {
        TODO("Not yet implemented")
    }

    fun getQuationQuestionList() {
        showProgressbar()
        QuestionListArray.clear()
        var result = ""
        try {
            val jsonBody = JSONObject()
            result = Networking.setParentJsonData(Constant.METHOD_QUESTION, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .InspectionQuestionList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<InspectionQuestionListModel>() {
                override fun onSuccess(response: InspectionQuestionListModel) {
                    hideProgressbar()
                    if (response.error == 200) {
                        QuestionListArray.addAll(response.data)
                        adapter?.notifyDataSetChanged()
                    }
                    if (QuestionListArray.size == 1) {
                        img.visible()
                        inpRemark.visible()
                        card2.visible()
                    } else {
                        img.invisible()
                        inpRemark.invisible()
                        card2.invisible()
                    }
                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }

    fun getSiteList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", -1)
            jsonBody.put("CurrentPage", 1)
            jsonBody.put("VisitorID", -1)
            jsonBody.put("CustomerID", -1)
            jsonBody.put("SiteName", "")
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_SITE_LIST,
                jsonBody
            )


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddInspectionActivity)
            .getServices()
            .getSiteList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<SiteListModal>() {
                override fun onSuccess(response: SiteListModal) {
                    siteListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()

                    siteNameList!!.add(getString(R.string.select_site))
                    myList.add(
                        SearchableItem(0, getString(R.string.select_site))
                    )

                    for (items in response.data.indices) {
                        siteNameList!!.add(response.data.get(items).siteName.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                siteNameList!!.get(items + 1)
                            )
                        )

                    }
                    itemSite = myList

                    adapterSite = ArrayAdapter(
                        this@AddInspectionActivity,
                        R.layout.custom_spinner_item,
                        siteNameList!!
                    )
                    spSite.setAdapter(adapterSite)


                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)

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
                    .into(img)

            }

        }
    }

    fun getUserTypeList(userType: String) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserType", userType)

            result = Networking.setParentJsonData(
                Constant.METHOD_GET_USER_BY_TYPE,
                jsonBody
            )


        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddInspectionActivity)
            .getServices()
            .getUserbyTypeList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<GetUserByType>() {
                override fun onSuccess(response: GetUserByType) {

                    if (userType.equals("FieldOperator")) {
                        fieldOperatorListArray!!.addAll(response.data)
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        fieldOperatorNameList!!.add(getString(R.string.select_field_operator))
                        myList.add(
                            SearchableItem(0, getString(R.string.select_field_operator))
                        )
                        for (items in response.data.indices) {
                            fieldOperatorNameList!!.add(
                                response.data.get(items).firstName.toString() + " " + response.data.get(
                                    items
                                ).lastName.toString()
                            )
                            myList.add(
                                SearchableItem(
                                    items.toLong() + 1,
                                    fieldOperatorNameList!!.get(items + 1)
                                )
                            )

                        }
                        itemFieldOperator = myList

                        adapterFieldOperator = ArrayAdapter(
                            this@AddInspectionActivity,
                            R.layout.custom_spinner_item,
                            fieldOperatorNameList!!
                        )
                        spFieldOperator.setAdapter(adapterFieldOperator)
                    } else if (userType.equals("OperationManager")) {
                        operationMangerListArray!!.addAll(response.data)
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        operationMangerNameList!!.add(getString(R.string.select_operation_manager))
                        myList.add(
                            SearchableItem(0, getString(R.string.select_operation_manager))
                        )
                        for (items in response.data.indices) {
                            operationMangerNameList!!.add(
                                response.data.get(items).firstName.toString() + " " + response.data.get(
                                    items
                                ).lastName.toString()
                            )
                            myList.add(
                                SearchableItem(
                                    items.toLong() + 1,
                                    operationMangerNameList!!.get(items + 1)
                                )
                            )

                        }
                        itemOperationManger = myList

                        adapterOperationManger = ArrayAdapter(
                            this@AddInspectionActivity,
                            R.layout.custom_spinner_item,
                            operationMangerNameList!!
                        )
                        spOperation.setAdapter(adapterOperationManger)
                    } else {
                        qualityManagerListArray!!.addAll(response.data)
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        qualityManagerNameList!!.add(getString(R.string.select_quality_manager))
                        myList.add(
                            SearchableItem(0, getString(R.string.select_quality_manager))
                        )
                        for (items in response.data.indices) {
                            qualityManagerNameList!!.add(
                                response.data.get(items).firstName.toString() + " " + response.data.get(
                                    items
                                ).lastName.toString()
                            )
                            myList.add(
                                SearchableItem(
                                    items.toLong() + 1,
                                    qualityManagerNameList!!.get(items + 1)
                                )
                            )

                        }
                        itemQualityManager = myList

                        adapterQualityManager = ArrayAdapter(
                            this@AddInspectionActivity,
                            R.layout.custom_spinner_item,
                            qualityManagerNameList!!
                        )
                        spQuality.setAdapter(adapterQualityManager)
                    }

                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)

    }

    private fun operationMangerSpinnerListner() {
        spOperation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && operationMangerListArray!!.size > position - 1) {
                    if (position == 0)
                        OperationMangerId = "-1"
                    else
                        OperationMangerId =
                            operationMangerListArray!!.get(position).userID.toString()

                }

            }
        }
    }

    private fun operationMangerViewClick() {
        view3.setOnClickListener {
            SearchableDialog(this@AddInspectionActivity,
                itemOperationManger!!,
                getString(R.string.select_operation_manager), { item, _ ->
                    spOperation.setSelection(item.id.toInt())
                }).show()
        }
    }

    private fun fieldOperatorSpinnerListner() {
        spFieldOperator.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && fieldOperatorListArray!!.size > position - 1) {
                    if (position == 0)
                        fieldOperatorId = "-1"
                    else
                        fieldOperatorId = fieldOperatorListArray!!.get(position).userID.toString()


                }

            }
        }
    }

    private fun fieldOperatorViewClick() {
        view2.setOnClickListener {
            SearchableDialog(this@AddInspectionActivity, itemFieldOperator!!,
                getString(R.string.select_field_operator), { item, _ ->
                    spFieldOperator.setSelection(item.id.toInt())
                }).show()
        }
    }

    private fun qualityManagerSpinnerListner() {
        spQuality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && qualityManagerListArray!!.size > position - 1) {
                    if (position == 0)
                        QualityManagerId = "-1"
                    else
                        QualityManagerId = qualityManagerListArray!!.get(position).userID.toString()


                }

            }
        }
    }

    private fun qualityManagerViewClick() {
        view4.setOnClickListener {
            SearchableDialog(this@AddInspectionActivity,
                itemQualityManager!!,
                getString(R.string.select_quality_manager), { item, _ ->
                    spQuality.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun AddInspectionData() {
        showProgressbar()
        var result = ""
        try {

            val jsonArray = JSONArray()

            for (item in QuestionListArray.indices) {
                val jsonObj = JSONObject()
                jsonObj.put("QuestionID", QuestionListArray.get(item).questionID)
                jsonObj.put("Answer", QuestionListArray.get(item).Answer)
                jsonArray.put(jsonObj)
            }

            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("SitesID", siteId)
            jsonBody.put("FieldOperatorID", fieldOperatorId)
            jsonBody.put("QualityManagerID", QualityManagerId)
            jsonBody.put("OperationManagerID", OperationMangerId)
            jsonBody.put("Remarks", edtRemark.getValue())
            jsonBody.put("Item", jsonArray)


            result = Networking.setParentJsonData(Constant.METHOD_ADD_INSPECTION, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddInspectionActivity)
            .getServices()
            .AddInspection(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {

                    if (response.error == 200) {
                        AddInspetionImage(response.data.get(0).iD.toString())
                    } else {
                        hideProgressbar()
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }


    fun AddInspetionImage(Id: String) {


        val requestFile: RequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), File(resultUriProfile?.path))
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(
            "ImageData",
            File(resultUriProfile?.path).name + ".jpg",
            requestFile
        )

        val methodName: RequestBody =
            RequestBody.create("text/plain".toMediaTypeOrNull(), "addInspectionImage")

        Networking
            .with(this)
            .getServices()
            .AddInspectionImage(
                body,
                methodName,
                RequestBody.create("text/plain".toMediaTypeOrNull(), Id),
            )//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {

                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        finish()
                    } else {
                        showAlert(response.message.toString())
                    }
                    hideProgressbar()
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                }

            })


    }

}