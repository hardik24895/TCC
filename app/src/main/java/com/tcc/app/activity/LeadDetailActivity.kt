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
import com.tcc.app.fragment.CustomerSiteFragment
import com.tcc.app.fragment.LeadInfoFragment
import com.tcc.app.fragment.LeadReminderFragment
import com.tcc.app.fragment.QuotationFragment
import com.tcc.app.modal.LeadItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_employee_detail.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class LeadDetailActivity : BaseActivity() {
    var viewPageradapter: ViewPagerPagerAdapter? = null
    var leadItem: LeadItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lead_detail)
        leadItem = intent.getSerializableExtra(Constant.DATA) as LeadItem
        txtTitle.text = leadItem?.name
        imgBack.visible()


        mDeclaration()
        setStatePageAdapter()
        clickEvent()
    }


    private fun clickEvent() {
        imgBack.setOnClickListener { onBackPressed() }

        imgAdd.setOnClickListener {

            if (viewPager.currentItem == 1) {
                if (checkUserRole(session.roleData.data?.sites?.isInsert.toString(), this)) {
                    val i = Intent(this, AddSiteActivity::class.java)
                    i.putExtra(Constant.VISITOR_ID, leadItem?.visitorID.toString())
                    i.putExtra(Constant.DATA_LEAD, leadItem)
                    i.putExtra(Constant.CUSTOMER_ID, "0")
                    startActivity(i)
                    Animatoo.animateCard(this)
                }
            } else if (viewPager.currentItem == 3) {
                val i = Intent(this, AddLeadReminderActivity::class.java)
                i.putExtra(Constant.VISITOR_ID, leadItem?.visitorID.toString())
                i.putExtra(Constant.CUSTOMER_ID, "0")
                startActivity(i)
                Animatoo.animateCard(this)
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
        val args = Bundle()
        args.putSerializable(Constant.DATA, leadItem)

        viewPageradapter = ViewPagerPagerAdapter(supportFragmentManager)
        viewPageradapter?.addFragment(LeadInfoFragment.getInstance(args), "Details")
        viewPageradapter?.addFragment(CustomerSiteFragment.getInstance(args), "Sites")
        viewPageradapter?.addFragment(QuotationFragment.getInstance(args), "Quotations")
        viewPageradapter?.addFragment(LeadReminderFragment.getInstance(args), "Reminders")

        viewPager.adapter = viewPageradapter
        tabs.setupWithViewPager(viewPager, true)


        tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 1 || tab.position == 3) {
                    imgAdd.visible()
                } else {
                    imgAdd.invisible()
                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }


}