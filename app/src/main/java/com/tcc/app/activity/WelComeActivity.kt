package com.tcc.app.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tcc.app.R
import com.tcc.app.fragment.WelcomeFragment
import com.tcc.app.utils.SessionManager.Companion.IsFirst
import kotlinx.android.synthetic.main.activity_wel_come.*
import java.util.*
import kotlin.collections.ArrayList


class WelComeActivity : BaseActivity() {


    private var mArrDrawable: ArrayList<Drawable>? = null
    private var mArrFragment: ArrayList<Fragment>? = null

    private var timer: Timer? = null
    private val DELAY_MS = 3 * 1000
    private val PERIOD_MS = 1000
    private val NUM_PAGES = 7
    private var currentPage = 0
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_wel_come)

        mArrDrawable = ArrayList<Drawable>()
        mArrFragment = ArrayList()

        session.storeDataByKey(IsFirst, false)
        setData()
        setViewPager()
        setRegister()
        setTimer()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        //        builder.setTitle("Exit");
        builder.setMessage("Would you like to exit from the App?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
                finish()
            }
        })
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dialog.dismiss()
            }
        })
        val alert = builder.create()
        alert.show()
    }

    private fun setData() {
        mArrDrawable!!.add(this.getResources().getDrawable(R.drawable.welcome1))
        mArrDrawable!!.add(this.getResources().getDrawable(R.drawable.welcome2))
        mArrDrawable!!.add(this.getResources().getDrawable(R.drawable.welcome3))
        mArrDrawable!!.add(this.getResources().getDrawable(R.drawable.welcome4))
        mArrDrawable!!.add(this.getResources().getDrawable(R.drawable.welcome5))
        mArrDrawable!!.add(this.getResources().getDrawable(R.drawable.welcome6))
        if (mArrDrawable!!.size > 0) {
            for (a in mArrDrawable!!.indices) {
                val bundle = Bundle()
                bundle.putInt("position", a)
                val fragment: Fragment = WelcomeFragment(a)
                fragment.arguments = bundle
                mArrFragment!!.add(fragment)
            }
        }
    }


    private fun setViewPager() {
        val mAdapter = ViewPagerAdapter(getSupportFragmentManager())
        view_pager_wel_come.setAdapter(mAdapter)
        tab_layout_wel_come.setupWithViewPager(view_pager_wel_come)
    }

    /**
     * Set Register Listener
     */
    private fun setRegister() {
        button_wel_come_get_started!!.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        text_wel_come_skip.setOnClickListener(View.OnClickListener {
            timer!!.cancel()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    fun setTimer() {
        /*After setting the adapter use the timer */
        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES - 1) {
                currentPage = 0
            }
            view_pager_wel_come.setCurrentItem(currentPage++, true)
        }
        timer = Timer() // This will create a new Thread
        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS.toLong())
    }

    inner class ViewPagerAdapter(fm: FragmentManager?) :
        FragmentStatePagerAdapter(fm!!) {
        override fun getItem(i: Int): Fragment {
            return mArrFragment!!.get(i)
        }

        override fun getCount(): Int {
            return mArrFragment!!.size
        }
    }

}