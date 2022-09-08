package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.tabs.TabLayout
import com.tcc.app.R
import com.tcc.app.adapter.ViewPagerPagerAdapter
import com.tcc.app.extention.checkUserRole
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

    }

    companion object {
        var startDate: String = ""
        var endDate: String = ""
        var invoiceNum: String = ""
        var siteName: String = ""
    }


    private fun clickEvent() {
        imgBack.setOnClickListener { onBackPressed() }

        imgAdd.setOnClickListener {

            if (view_pager.currentItem == 1) {
                if (checkUserRole(session.roleData.data?.sites?.isInsert.toString(), this)) {
                    val i = Intent(this, AddSiteActivity::class.java)
                    i.putExtra(Constant.VISITOR_ID, customerData?.visitorID.toString())
                    i.putExtra(Constant.CUSTOMER_ID, customerData?.customerID.toString())
                    i.putExtra(Constant.DATA, customerData)
                    startActivity(i)
                    Animatoo.animateCard(this)
                }
            }


//            } else {
//                goToActivity<AddQuotationActivity>()
//            }
        }
        imgFilter.setOnClickListener {
            if (view_pager.currentItem == 7) {

                val intent = Intent(this, SearchActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.PAYMENT)
                startActivity(intent)
                Animatoo.animateCard(this)
            }
        }

    }

    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(supportFragmentManager)
        viewPageradapter?.addFragment(CustomerInfoFragment(customerData), "Information")
        //     if (checkUserRole(session.roleData.data?.sites?.isView.toString(), this)) {
        viewPageradapter?.addFragment(CustomerSiteFragment(customerData), "Sites")
        //     }

        viewPageradapter?.addFragment(CustomerProcessFragment(customerData), "Process")
        viewPageradapter?.addFragment(QuotationFragment(customerData), "Quotation")
        viewPageradapter?.addFragment(TeamDefinitionListFragment(customerData), "Team Defination")
        //  if (checkUserRole(session.roleData.data?.attendance?.isView.toString(), this)) {
        viewPageradapter?.addFragment(CustomerAttendanceListFragment(customerData), "Attendance")
        //   }
        //   if (checkUserRole(session.roleData.data?.invoice?.isView.toString(), this)) {
        viewPageradapter?.addFragment(CustomerInvoiceFragment(customerData), "Invoice")
        //}
        //      if (checkUserRole(session.roleData.data?.payment?.isView.toString(), this)) {
        viewPageradapter?.addFragment(PaymentListFragment(customerData), "Payment")
        //     }

        view_pager.adapter = viewPageradapter
        tabs.setupWithViewPager(view_pager, true)


        tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                imgAdd.invisible()
                imgFilter.invisible()

                if (tab.position == 1) {
                    imgAdd.visible()
                }
                if (tab.position == 7) {
                    imgFilter.visible()
                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun onResume() {
        if (QuotationFragment.isFromQuotation) {
            finish()
        }

        super.onResume()
    }
}

