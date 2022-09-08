package com.tcc.app.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.TimeStamp.formatDateFromString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_uniform.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class AddUniformActivity : BaseActivity() {
    var employeeData: EmployeeDataItem? = null

    var uniformListArray: ArrayList<UniformSpinnerDataItem>? = null
    var UniformNameArray: ArrayList<String>? = null
    var adapterUniform: ArrayAdapter<String>? = null
    var itemUniform: List<SearchableItem>? = null
    var UniformId: String? = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_uniform)
        uniformListArray = ArrayList()
        UniformNameArray = ArrayList()

        if (intent.hasExtra(Constant.DATA)) {
            employeeData = intent.getSerializableExtra(Constant.DATA) as EmployeeDataItem
        }

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = getString(R.string.uniform)

        edtDate.setOnClickListener { showDateTimePicker(this@AddUniformActivity, edtDate) }
        edtDate.setText(getCurrentDate())
        getUniformList()

        spUniform.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && uniformListArray!!.size > position) {
                    UniformId = uniformListArray!!.get(position).uniformTypeID
                }

                if (position == 0) {
                    UniformId = "-1"
                } else {
                    UniformId = uniformListArray!!.get(position - 1).uniformTypeID
                }
            }
        }

        view.setOnClickListener {

            SearchableDialog(this,
                itemUniform!!,
                getString(R.string.select_uniform_date),
                { item, _ ->
                    spUniform.setSelection(item.id.toInt())
                }).show()
        }

        btnSubmit.setOnClickListener {
            if (UniformId.equals("-1")) {
                root.showSnackBar("Please select Uniform type")
            } else {
                AddUniform()
            }
        }


    }


    fun getUniformList() {
        var result = ""

        try {
            val jsonBody = JSONObject()
            result = Networking.setParentJsonData(Constant.METHOD_UNIFORM_SPINNER_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddUniformActivity)
            .getServices()
            .AddUniformData(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<UniformSpinnerListModel>() {
                override fun onSuccess(response: UniformSpinnerListModel) {
                    uniformListArray?.addAll(response.data)

                    var myList: MutableList<SearchableItem> = mutableListOf()
                    UniformNameArray!!.add(getString(R.string.select_uniform_date))
                    myList.add(SearchableItem(0, getString(R.string.select_uniform_date)))

                    for (items in response.data.indices) {
                        UniformNameArray?.add(response.data.get(items).uniformtype.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                UniformNameArray!!.get(items + 1)
                            )
                        )
                    }

                    itemUniform = myList

                    adapterUniform = ArrayAdapter(
                        this@AddUniformActivity,
                        R.layout.custom_spinner_item,
                        UniformNameArray!!
                    )
                    spUniform.setAdapter(adapterUniform)

                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun AddUniform() {
        var result = ""

        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", employeeData?.userID)
            jsonBody.put("UniformTypeID", UniformId)
            jsonBody.put("UniformDate", formatDateFromString(edtDate.text.toString()))

            result = Networking.setParentJsonData(Constant.METHOD_ADD_UNIFORM, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddUniformActivity)
            .getServices()
            .AddTrainingData(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        finish()
                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }


}