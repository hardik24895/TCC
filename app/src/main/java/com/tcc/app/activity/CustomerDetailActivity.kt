package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.adapter.ViewPagerPagerAdapter
import com.tcc.app.extention.visible
import com.tcc.app.fragment.CustomerInfoFragment
import com.tcc.app.fragment.CustomerProcessFragment
import com.tcc.app.fragment.CustomerSiteFragment
import com.tcc.app.fragment.QuotationFragment
import com.tcc.app.modal.CustomerDataItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class CustomerDetailActivity : BaseActivity() {
    var viewPageradapter: ViewPagerPagerAdapter? = null

    var customerData: CustomerDataItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_customer)
        if (intent.hasExtra(Constant.DATA)) {
            customerData = intent.getSerializableExtra(Constant.DATA) as CustomerDataItem

        }

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Customer Details"
        setStatePageAdapter()
        // val bottomSheet: View = findViewById(R.id.view_pager)
        // val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
//        val displayMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(displayMetrics)
//
//        var height = displayMetrics.heightPixels
//
//
//        main_view.post(Runnable { //height is ready
//            System.out.println(
//                "imageview width:" + main_view.getWidth()
//                    .toString() + " height:" + main_view.getHeight()
//            )
//            behavior.peekHeight = (height)
//
//        })
    }

    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(supportFragmentManager)
        viewPageradapter?.addFragment(CustomerInfoFragment(customerData), "Information")
        viewPageradapter?.addFragment(CustomerSiteFragment(customerData), "Sites")
        viewPageradapter?.addFragment(CustomerProcessFragment(customerData), "Process")
        viewPageradapter?.addFragment(QuotationFragment(customerData), "Quotation")
        viewPageradapter?.addFragment(QuotationFragment(customerData), "Team Defination")
        view_pager.adapter = viewPageradapter

    }
}

