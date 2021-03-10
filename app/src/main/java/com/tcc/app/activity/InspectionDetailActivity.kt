package com.tcc.app.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.adapter.InspectionQuestionPreviewAdapter
import com.tcc.app.extention.visible
import com.tcc.app.modal.InspectionDataItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_inspection_detail.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class InspectionDetailActivity : BaseActivity() {

    lateinit var inspectionDataItem: InspectionDataItem
    lateinit var adapter: InspectionQuestionPreviewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspection_detail)
        if (intent.hasExtra(Constant.DATA)) {
            inspectionDataItem = intent.getSerializableExtra(Constant.DATA) as InspectionDataItem

        }

        txtTitle.text = inspectionDataItem.companyName
        imgBack.visible()
        setData()
    }


    private fun setData() {
        imgBack.setOnClickListener { onBackPressed() }
        txtSiteName.text = inspectionDataItem.sitesName
        txtEmployeeName.text = inspectionDataItem.employeeName
        txtEmployeeType.text = inspectionDataItem.userType
        txtInspectionDate.text = inspectionDataItem.inspectionDate
        txtIRemarks.text = inspectionDataItem.remarks

        Glide.with(this).load(Constant.PDF_INSPECTION_URL + inspectionDataItem.image)
            .placeholder(R.drawable.ic_profile).into(ivUpload)


        setupRecyclerView()

    }

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        recPreview.layoutManager = layoutManager
        adapter = InspectionQuestionPreviewAdapter(this, inspectionDataItem.item)
        recPreview.adapter = adapter

    }


}