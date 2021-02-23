package com.tcc.app.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.tcc.app.R
import kotlinx.android.synthetic.main.activity_information.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class InformationActivity : AppCompatActivity() {

    var infoURL: String = "http://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        txtTitle.text = intent.getStringExtra("Title")
        intent.getStringExtra("Desc")
        imgBack.setOnClickListener { finish() }

        webview.settings.javaScriptEnabled = true
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
        }
        intent.getStringExtra("Desc")?.let { webview.loadUrl(it) }
        // webview.loadUrl(infoURL + intent.getStringExtra("Desc"))
    }
}