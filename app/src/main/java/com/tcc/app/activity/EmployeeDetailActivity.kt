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
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_employee_detail.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class EmployeeDetailActivity : BaseActivity() {

    var viewPageradapter: ViewPagerPagerAdapter? = null
    var employeeData: EmployeeDataItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_detail)
        imgAdd.visible()
        imgBack.visible()

        if (intent.hasExtra(Constant.DATA)) {
            employeeData = intent.getSerializableExtra(Constant.DATA) as EmployeeDataItem
            txtTitle.text = employeeData?.firstName + " " + employeeData?.lastName
        }




        mDeclaration()
        clickEvent()
        setStatePageAdapter()
    }


    private fun clickEvent() {
        imgBack.setOnClickListener { finish() }


        imgAdd.setOnClickListener {


            var intent: Intent? = null
            if (viewPager.currentItem == 0) {
                intent = Intent(this@EmployeeDetailActivity, AddTrainingActivity::class.java)
            } else if (viewPager.currentItem == 1) {

                intent = Intent(this@EmployeeDetailActivity, AddUniformActivity::class.java)
            } else if (viewPager.currentItem == 3) {
                intent = Intent(this@EmployeeDetailActivity, AddRoomAllocationActivity::class.java)
            }
            intent?.putExtra(Constant.DATA, employeeData)
            startActivity(intent)
            Animatoo.animateCard(this@EmployeeDetailActivity)
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
        args.putBoolean("flag", false)
        args.putSerializable(Constant.DATA, employeeData)

        viewPageradapter = ViewPagerPagerAdapter(supportFragmentManager)
        viewPageradapter?.addFragment(EmployeeTrainingFragment(employeeData), "Training")
        viewPageradapter?.addFragment(EmployeeUniformFragment(employeeData), "Uniform")
        viewPageradapter?.addFragment(AttendanceListFragment(false, employeeData), "Attendace")
        viewPageradapter?.addFragment(
            EmployeeRoomAllocationFragment(employeeData),
            "Room Allocation"
        )
        viewPageradapter?.addFragment(EmployeeSalaryFragment(employeeData), "Salary")
        viewPager.adapter = viewPageradapter
        tabs.setupWithViewPager(viewPager, true)


        tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 2 || tab.position == 4) {
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