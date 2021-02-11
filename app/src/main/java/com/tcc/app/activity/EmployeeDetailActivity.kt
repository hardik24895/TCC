package com.tcc.app.activity

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.tcc.app.Adapter.ViewPagerPagerAdapter
import com.tcc.app.R
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import com.tcc.app.fragment.*
import kotlinx.android.synthetic.main.activity_employee_detail.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class EmployeeDetailActivity : BaseActivity() {
    var viewPageradapter: ViewPagerPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)
        txtTitle.text = "Hardik Kanzariya"
        imgBack.visible()
        mDeclaration()
        clickEvent()
        setStatePageAdapter()
    }


    private fun clickEvent() {
        imgBack.setOnClickListener { onBackPressed() }


        imgAdd.setOnClickListener {

            if (viewPager.currentItem == 1) {
                goToActivity<AddTrainingActivity>()
            } else if (viewPager.currentItem == 2) {
                goToActivity<AddUniformActivity>()
            } else if (viewPager.currentItem == 4) {
                goToActivity<AddRoomAllocationActivity>()
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
        viewPageradapter?.addFragment(EmployeeDetailFragment(), "Detail")
        viewPageradapter?.addFragment(EmployeeTrainingFragment(), "Training")
        viewPageradapter?.addFragment(EmployeeUniformFragment(), "Uniform")
        viewPageradapter?.addFragment(AttendanceListFragment(false), "Attendace")
        viewPageradapter?.addFragment(EmployeeRoomAllocationFragment(), "Room Allocation")
        viewPageradapter?.addFragment(EmployeeSalaryFragment(), "Salary")
        viewPager.adapter = viewPageradapter
        tabs.setupWithViewPager(viewPager, true)

        tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0 || tab.position == 3 || tab.position == 5) {
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