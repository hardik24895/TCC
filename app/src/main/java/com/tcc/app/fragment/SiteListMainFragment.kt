package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import com.tcc.app.R
import com.tcc.app.adapter.ViewPagerPagerAdapter
import com.tcc.app.extention.setHomeScreenTitle
import com.tcc.app.extention.showAlert
import com.tcc.app.modal.CustomerDataItem
import com.tcc.app.modal.DynemicSiteTabDataItem
import com.tcc.app.modal.DynemicSiteTabListModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_invoice.*
import org.json.JSONException
import org.json.JSONObject

class SiteListMainFragment() : BaseFragment() {

    var customerId: Int? = -1
    var visitorId: Int? = -1

    var dynemicSiteTabListModal: MutableList<DynemicSiteTabDataItem> = mutableListOf()


    constructor(customerData: CustomerDataItem?) : this() {
        customerId = customerData?.customerID?.toInt()
        visitorId = customerData?.visitorID?.toInt()
    }

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

        setHomeScreenTitle(requireActivity(), getString(R.string.nav_site))

        getSiteListByTab()

        tabs.tabMode = MODE_SCROLLABLE

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

    fun getSiteListByTab() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", -1)
            jsonBody.put("CurrentPage", 1)
            jsonBody.put("VisitorID", visitorId.toString())
            jsonBody.put("CustomerID", customerId)
            jsonBody.put("SiteName", CustomerSiteFragment.name)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_SITE_LIST_TAB,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .getSiteTabList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<DynemicSiteTabListModal>() {
                override fun onSuccess(response: DynemicSiteTabListModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        dynemicSiteTabListModal.addAll(response.data)
                        setStatePageAdapter(dynemicSiteTabListModal)
                    }


                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }


    private fun setStatePageAdapter(list: MutableList<DynemicSiteTabDataItem>) {
        viewPageradapter = ViewPagerPagerAdapter(childFragmentManager)
        for (i in list.indices) {
            viewPageradapter?.addFragment(
                SiteTabFragment(list.get(i).sites),
                list.get(i).title.toString()
            )
        }

        viewPager.adapter = viewPageradapter
        tabs.setupWithViewPager(viewPager, true)

    }


}