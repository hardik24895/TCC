package com.tcc.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.NotificationAdapter
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*


class NotificationActivity : AppCompatActivity() {


    private var adapter: NotificationAdapter? = null
    lateinit var notificationArray: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        txtTitle.setText("Notifications")

        imgBack.setOnClickListener {
            finish()
        }
        notificationArray = ArrayList()
        setData()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvNotification.layoutManager = layoutManager
        adapter = NotificationAdapter(this, notificationArray)
        rvNotification.adapter = adapter

    }


    private fun setData() {
        notificationArray.clear()
        notificationArray.add("")
        notificationArray.add("")
        notificationArray.add("")
        notificationArray.add("")
        notificationArray.add("")
        notificationArray.add("")
        notificationArray.add("")
        notificationArray.add("")
        notificationArray.add("")
    }


}