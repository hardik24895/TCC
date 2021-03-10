package com.tcc.app.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tcc.app.R

class InformationActivity : AppCompatActivity() {

    var infoURL: String = "http://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cms2)

        /*  txtTitle.text = intent.getStringExtra("Title")
          intent.getStringExtra("Desc")
          imgBack.setOnClickListener { finish() }

          webview.settings.javaScriptEnabled = true
          webview.webViewClient = object : WebViewClient() {
              override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                  view?.loadUrl(url!!)
                  return true
              }
          }
          intent.getStringExtra("Desc")?.let { webview.loadUrl(it) }*/
        // webview.loadUrl(infoURL + intent.getStringExtra("Desc"))
    }
}