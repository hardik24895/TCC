package com.tcc.app.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.tcc.app.R
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.showSnackBar
import com.tcc.app.extention.visible
import com.tcc.app.modal.*
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_room_allocation.*
import kotlinx.android.synthetic.main.activity_add_training.*
import kotlinx.android.synthetic.main.activity_add_training.btnSubmit
import kotlinx.android.synthetic.main.activity_add_training.root
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class AddTrainingActivity : BaseActivity() {

    var employeeData: EmployeeDataItem? = null
    var trainingListArray: ArrayList<TrainingSpinnerDataItem>? = null
    var trainingNameArray: ArrayList<String>? = null
    var adapterTraining: ArrayAdapter<String>? = null
    var itemTraining: List<SearchableItem>? = null
    var trainingId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_training)

        txtTitle.text = getString(R.string.training)
        trainingNameArray = ArrayList()
        trainingListArray = ArrayList()
        imgBack.visible()

        imgBack.setOnClickListener {
            finish()
        }

        if (intent.hasExtra(Constant.DATA)) {
            employeeData = intent.getSerializableExtra(Constant.DATA) as EmployeeDataItem
        }
        getTrainingList()



        spTrainingType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && trainingListArray!!.size > position) {
                    trainingId = trainingListArray!!.get(position).trainingID
                }

            }
        }

        view.setOnClickListener {

            SearchableDialog(this,
                itemTraining!!,
                getString(R.string.select_city),
                { item, _ ->
                    spTrainingType.setSelection(item.id.toInt())
                }).show()
        }

        btnSubmit.setOnClickListener {
            AddTraining()
        }

    }

    fun getTrainingList() {
        var result = ""

        try {
            val jsonBody = JSONObject()
            result = Networking.setParentJsonData(Constant.METHOD_TRAINING_DATE_TIME, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddTrainingActivity)
            .getServices()
            .getTrainingData(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<TrainingSpinnerListModel>() {
                override fun onSuccess(response: TrainingSpinnerListModel) {
                    trainingListArray?.addAll(response.data)

                    var myList: MutableList<SearchableItem> = mutableListOf()
                    for (items in response.data.indices) {
                        trainingNameArray?.add(
                            response.data.get(items).training.toString() + " - " +
                                    response.data.get(items).trainingDate.toString() + " - "
                                    + response.data.get(items).trainingTime.toString()
                        )
                        myList.add(SearchableItem(items.toLong(), trainingNameArray!!.get(items)))
                    }

                    itemTraining = myList

                    adapterTraining = ArrayAdapter(
                        this@AddTrainingActivity,
                        R.layout.custom_spinner_item,
                        trainingNameArray!!
                    )
                    spTrainingType.setAdapter(adapterTraining)

                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }

    fun AddTraining() {
        var result = ""

        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", employeeData?.userID)
            jsonBody.put("TrainingDateTimeID", trainingId)
            jsonBody.put("EmployeeID", employeeData?.userID)

            result = Networking.setParentJsonData(Constant.METHOD_ADD_TRAINING, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this@AddTrainingActivity)
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

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }

}