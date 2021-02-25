package com.tcc.app.fragment

import android.os.Bundle
import android.view.*
import com.google.android.material.tabs.TabLayout
import com.tcc.app.R
import com.tcc.app.adapter.ViewPagerPagerAdapter
import kotlinx.android.synthetic.main.fragment_invoice.*

class ProfileMainFragment : BaseFragment() {
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
      //  setHomeScreenTitle(requireActivity(), getString(R.string.nav_invoice))
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)

        val add = menu.findItem(R.id.action_add)
        add.setVisible(false)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {

                return true
            }
            R.id.action_filter -> {

                return true
            }
            else -> super.onOptionsItemSelected(item)
         }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(childFragmentManager)
        viewPageradapter?.addFragment(ProfileFragment(), "Profile")
        viewPageradapter?.addFragment(ProfileCheckInCheckoutFragment(), "Check in-out")
        viewPager.adapter = viewPageradapter
        tabs.setupWithViewPager(viewPager, true)

    }
}