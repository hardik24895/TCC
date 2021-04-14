package com.tcc.app.fragment

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.tcc.app.R
import com.tcc.app.activity.BaseActivity
import com.tcc.app.adapter.ViewPagerPagerAdapter
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.activity_profile_main.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class ProfileMainActivity : BaseActivity() {
    lateinit var mParent: View
    var viewPageradapter: ViewPagerPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_main)
        setStatePageAdapter()
        imgBack.visible()
        txtTitle.text = "Profile"

        imgBack.setOnClickListener { finish() }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                hideSoftKeyboard()
                viewPager.currentItem = tab.position
                val fm = supportFragmentManager
                val ft = fm.beginTransaction()
                val count = fm.backStackEntryCount
                if (count >= 1) {
                    supportFragmentManager.popBackStack()
                }
                ft.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // setAdapter();
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //   viewPager.notifyAll();
            }
        })
    }


    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(supportFragmentManager)
        viewPageradapter?.addFragment(ProfileFragment(), "Profile")
        viewPageradapter?.addFragment(ProfileCheckInCheckoutFragment(), "Check in-out")
        viewPager.adapter = viewPageradapter
        tabs.setupWithViewPager(viewPager, true)

    }


}