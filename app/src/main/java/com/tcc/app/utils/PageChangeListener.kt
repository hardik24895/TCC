package com.tcc.app.utils

import androidx.viewpager.widget.ViewPager

abstract class PageChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        onPageChanged(position)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    internal abstract fun onPageChanged(position: Int)
}
