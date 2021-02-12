package com.tcc.app.activity

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tcc.app.R
import com.tcc.app.adapter.ViewPagerPagerAdapter
import com.tcc.app.extention.visible
import com.tcc.app.fragment.CustomerInfoFragment
import com.tcc.app.fragment.CustomerProcessFragment
import com.tcc.app.fragment.CustomerSiteFragment
import com.tcc.app.fragment.QuotationFragment
import kotlinx.android.synthetic.main.activity_add_customer.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class CustomerDetailActivity : BaseActivity() {
    var viewPageradapter: ViewPagerPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_customer)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Customer Details"
        val bottomSheet: View = findViewById(R.id.view_pager)
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        setStatePageAdapter()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var height = displayMetrics.heightPixels


        main_view.post(Runnable { //height is ready
            System.out.println(
                "imageview width:" + main_view.getWidth()
                    .toString() + " height:" + main_view.getHeight()
            )
            behavior.peekHeight = (height - (main_view.getHeight() + 56))

        })
    }

    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(supportFragmentManager)
        viewPageradapter?.addFragment(CustomerInfoFragment(), "Information")
        viewPageradapter?.addFragment(CustomerSiteFragment(), "Sites")
        viewPageradapter?.addFragment(CustomerProcessFragment(), "Process")
        viewPageradapter?.addFragment(QuotationFragment(), "Quotation")
        viewPageradapter?.addFragment(QuotationFragment(), "Team Defination")
        view_pager.adapter = viewPageradapter

    }
}