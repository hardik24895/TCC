package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.tcc.app.Adapter.ViewPagerPagerAdapter
import com.tcc.app.R
import com.tcc.app.extention.setHomeScreenTitle
import kotlinx.android.synthetic.main.fragment_invoice.*

class InvoiceFragment : BaseFragment() {
    lateinit var mParent: View

    var viewPageradapter: ViewPagerPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mParent = inflater.inflate(R.layout.fragment_invoice, container, false)
        return mParent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHomeScreenTitle(requireActivity(), getString(R.string.nav_invoice))
        setStatePageAdapter()
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                val fm = childFragmentManager
                val ft = fm.beginTransaction()
                val count = fm.backStackEntryCount
                if (count >= 1) {
                    childFragmentManager.popBackStack()
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
        viewPageradapter = ViewPagerPagerAdapter(childFragmentManager)
        viewPageradapter?.addFragment(InvoicePaidFragment(), "Paid")
        viewPageradapter?.addFragment(InvoiceUnPaidFragment(), "Unpaid")
        viewPager.adapter = viewPageradapter
        tabs.setupWithViewPager(viewPager, true)

    }
}