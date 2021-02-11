package com.tcc.app.activity

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.tcc.app.Adapter.ViewPagerPagerAdapter
import com.tcc.app.R
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import com.tcc.app.fragment.CustomerSiteFragment
import com.tcc.app.fragment.QuotationFragment
import com.tcc.app.fragment.VisitorInfoFragment
import kotlinx.android.synthetic.main.activity_employee_detail.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class VisitorDetailActivity : BaseActivity() {
    var viewPageradapter: ViewPagerPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visitor_detail)
        txtTitle.text = "Hardik Kanzariya"
        imgBack.visible()
        mDeclaration()
        setStatePageAdapter()
        clickEvent()
    }


    private fun clickEvent() {
        imgBack.setOnClickListener { onBackPressed() }

        imgAdd.setOnClickListener {

            if (viewPager.currentItem == 1) {
                goToActivity<AddSiteActivity>()
            } else {
                goToActivity<AddQuotationActivity>()
            }
        }

    }

    private fun mDeclaration() {

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                // if (viewPager.currentItem == 1) ImgADDPhoto?.visible() else ImgADDPhoto?.invisible()
                val fm = supportFragmentManager
                val ft = fm.beginTransaction()
                val count = fm.backStackEntryCount
                if (count >= 1) {
                    supportFragmentManager.popBackStack()
                }
                ft.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(supportFragmentManager)
        viewPageradapter?.addFragment(VisitorInfoFragment(), "Detail")
        viewPageradapter?.addFragment(CustomerSiteFragment(), "Sites")
        viewPageradapter?.addFragment(QuotationFragment(), "Quotation")

        viewPager.adapter = viewPageradapter
        tabs.setupWithViewPager(viewPager, true)


        tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
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