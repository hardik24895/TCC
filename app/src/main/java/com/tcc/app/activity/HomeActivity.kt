package com.tcc.app.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.tcc.app.R
import com.tcc.app.extention.goToActivity
import com.tcc.app.fragment.ProfileMainActivity
import com.tcc.app.fragment.QuotationFragment
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import de.hdodenhof.circleimageview.CircleImageView


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var session: SessionManager
    var txtName: TextView? = null
    var txtEmail: TextView? = null
    var profileImage: CircleImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        session = SessionManager(this)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        var headerview: View = navView.getHeaderView(0)
        var nav_main: ConstraintLayout = headerview.findViewById(R.id.nav_main)
        txtName = headerview.findViewById(R.id.txtName)
        txtEmail = headerview.findViewById(R.id.txtEmail)
        profileImage = headerview.findViewById(R.id.circleImageView)
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {


                txtName!!.setText("${session.user.data?.firstName} ${session.user.data?.lastName}")
                txtEmail!!.setText("${session.user.data?.emailID}")

                Glide.with(this@HomeActivity)
                    .load(Constant.EMP_PROFILE + session.user.data?.photoURL)
                    .apply(
                        com.bumptech.glide.request.RequestOptions().centerCrop()
                            .placeholder(com.tcc.app.R.drawable.ic_profile)
                    )
                    .into(profileImage!!)
                //removeAllFragments(supportFragmentManager)
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), 0)

            }
        })

        nav_main.setOnClickListener {
            goToActivity<ProfileMainActivity>()
            drawerLayout.closeDrawers()

        }

        val navController = findNavController(R.id.nav_host_fragment)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_visitor,
                R.id.nav_setting,
                R.id.nav_invoice,
                R.id.nav_customer,
                R.id.nav_employee,
                R.id.nav_quotation,
                R.id.nav_inspection,
                R.id.nav_payment,
                R.id.nav_attendance,
                R.id.nav_site
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (session.roleData.data?.customer?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_customer).setVisible(false)
            navView.invalidate()
        }
        if (session.roleData.data?.employee?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_employee).setVisible(false)
            navView.invalidate()
        }
        if (session.roleData.data?.attendance?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_attendance).setVisible(false)
            navView.invalidate()
        }
        if (session.roleData.data?.inspection?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_inspection).setVisible(false)
            navView.invalidate()
        }
        if (session.roleData.data?.invoice?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_invoice).setVisible(false)
            navView.invalidate()
        }
        if (session.roleData.data?.payment?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_payment).setVisible(false)
            navView.invalidate()
        }
        if (session.roleData.data?.penlty?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_penalti).setVisible(false)
            navView.invalidate()
        }
        if (session.roleData.data?.sites?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_site).setVisible(false)
            navView.invalidate()
        }
        if (session.roleData.data?.visitor?.isView.equals("0")) {
            navView.getMenu().findItem(R.id.nav_visitor).setVisible(false)
            navView.invalidate()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            // val msg = getString(R.string.msg_token_fmt, token)
            Log.d("token", token.toString())
            //  Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })



        handleRedirection(intent)
    }

    private fun removeAllFragments(fragmentManager: FragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate()
        }
    }

    override fun onResume() {
        txtName!!.setText("${session.user.data?.firstName} ${session.user.data?.lastName}")
        txtEmail!!.setText("${session.user.data?.emailID}")

        Glide.with(this)
            .load(Constant.EMP_PROFILE + session.user.data?.photoURL)
            .apply(
                com.bumptech.glide.request.RequestOptions().centerCrop()
                    .placeholder(com.tcc.app.R.drawable.ic_profile)
            )
            .into(profileImage!!)


        if (QuotationFragment.isFromQuotation) {
            val navView: NavigationView = findViewById(R.id.nav_view)
            val view: View = navView.findViewById(R.id.nav_quotation)
            view.performClick()
        }
        super.onResume()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {


        super.onBackPressed()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleRedirection(intent)
    }

    private fun handleRedirection(intent: Intent?) {
        val extras = intent?.extras
        val navController = findNavController(R.id.nav_host_fragment)

        if (extras != null) {
            for (key in intent.extras!!.keySet()) {
                val type = intent.extras!!.getString(key)

                if (type.equals("AddSite", true)
                ) {

                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(R.id.nav_site)
                    }, 1000)

                } else if (type.equals("AddLead", true)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(R.id.nav_visitor)
                    }, 1000)
                } else if (type.equals("AddQuotation", true)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(R.id.nav_quotation)
                    }, 1000)
                } else if (type.equals("AddInvoice", true)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(R.id.nav_invoice)
                    }, 1000)
                } else if (type.equals("AddPayment", true)) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(R.id.nav_invoice)
                    }, 1000)
                }


            }

        }

    }

}