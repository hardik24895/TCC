package com.tcc.app.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.tcc.app.R
import com.tcc.app.adapter.InspectionQuesiontAdapter
import com.tcc.app.extention.*
import com.tcc.app.modal.InspectionQuesDataItem
import com.tcc.app.modal.InspectionQuestionListModel
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.NoScrollLinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_inspection.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class AddInspectionActivity : BaseActivity(), InspectionQuesiontAdapter.OnItemSelected {
    var layoutManager: NoScrollLinearLayoutManager? = null
    var QuestionListArray: ArrayList<InspectionQuesDataItem> = ArrayList()
    var adapter: InspectionQuesiontAdapter? = null
    lateinit var chipArray: ArrayList<String>

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
        edtDate.setText(getCurrentDate())
        edtDate.setOnClickListener { showDateTimePicker(this@AddInspectionActivity, edtDate) }
        setupRecyclerViewMarchant()
        getQuationQuestionList()

        btnSubmit.setOnClickListener {
            goToNextQue()
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
                btnSubmit.setText(getString(R.string.next))
            } else if (layoutManager!!.findLastCompletelyVisibleItemPosition() == (QuestionListArray.size - 2)) {
                img.visible()
                inpRemark.visible()
                btnSubmit.setText(getString(R.string.submit))

            } else {
                img.invisible()
                inpRemark.invisible()
                btnSubmit.setText(getString(R.string.next))
            }
        } else {
            for (item in QuestionListArray.indices)
                Log.e("TAG", "goToNextQue:    " + QuestionListArray.get(item).Answer)
        }
    }


    fun setupRecyclerViewMarchant() {

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
                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }

}