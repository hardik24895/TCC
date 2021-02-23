package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.tabs.TabLayout
import com.tcc.app.R
import com.tcc.app.adapter.ViewPagerPagerAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import com.tcc.app.fragment.*
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
        clickEvent()
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

    private fun clickEvent() {
        imgBack.setOnClickListener { onBackPressed() }

        imgAdd.setOnClickListener {

            if (view_pager.currentItem == 1) {
                val i = Intent(this, AddSiteActivity::class.java)
                i.putExtra(Constant.VISITOR_ID, customerData?.visitorID.toString())
                i.putExtra(Constant.CUSTOMER_ID, customerData?.customerID.toString())
                startActivity(i)
                Animatoo.animateCard(this)
            }
//            } else {
//                goToActivity<AddQuotationActivity>()
//            }
        }

    }

    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(supportFragmentManager)
        viewPageradapter?.addFragment(CustomerInfoFragment(customerData), "Information")
        viewPageradapter?.addFragment(CustomerSiteFragment(customerData), "Sites")
        viewPageradapter?.addFragment(CustomerProcessFragment(customerData), "Process")
        viewPageradapter?.addFragment(QuotationFragment(customerData), "Quotation")
        viewPageradapter?.addFragment(InvoiceFragment(customerData), "Invoice")
        viewPageradapter?.addFragment(PaymentListFragment(customerData), "Payment")
        viewPageradapter?.addFragment(QuotationFragment(customerData), "Team Defination")
        viewPageradapter?.addFragment(CustomerAttendanceListFragment(customerData), "Attendace")
        view_pager.adapter = viewPageradapter
        tabs.setupWithViewPager(view_pager, true)


        tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position != 1) {
                    imgAdd.invisible()
                } else {
                    imgAdd.visible()
                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}

