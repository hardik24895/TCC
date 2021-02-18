package com.tcc.app.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.tcc.app.R
import com.tcc.app.extention.addFragment
import com.tcc.app.fragment.ProfileMainFragment


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        var headerview: View = navView.getHeaderView(0)
        var nav_main: ConstraintLayout = headerview?.findViewById(R.id.nav_main);
        nav_main.setOnClickListener {
            addFragment(ProfileMainFragment(), R.id.nav_host_fragment)
            drawerLayout.closeDrawers()
        }


        val navController = findNavController(R.id.nav_host_fragment)


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_visitor,
                R.id.nav_setting,
                R.id.nav_invoice,
                R.id.nav_customer,
                R.id.nav_employee,
                R.id.nav_quotation,
                R.id.nav_penalti,
                R.id.nav_inspection,
                R.id.nav_payment,
                R.id.nav_attendance,
                R.id.nav_ticket,
                R.id.nav_site,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//
//        menuInflater.inflate(R.menu.home, menu)
//        return true
//    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}