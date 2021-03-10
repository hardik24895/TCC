package com.tcc.app.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.tcc.app.R
import com.tcc.app.extention.showAlert
import com.tcc.app.modal.CMSModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cms2.*
import org.json.JSONException
import org.json.JSONObject


class InformationActivity : BaseActivity() {

    var infoURL: String = "http://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cms2)
        val title = intent.getStringExtra(Constant.TITLE)
        if (title.equals("1")) {
            txtTitle.text = "TERM AND CONDITION"
        } else if (title.equals("2")) {
            txtTitle.text = "ABOUT US"
        } else {
            txtTitle.text = "PRIVACY POLICY"
        }
        intent.getStringExtra("Desc")?.let { getCMSData(it) }
        imgBack.setOnClickListener { onBackPressed() }
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

    fun getCMSData(pageID: String) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageName", pageID)

            result = Networking.setParentJsonData(
                Constant.METHOD_GET_PAGE,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .getCMS(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CMSModal>() {
                override fun onSuccess(response: CMSModal) {
                    hideProgressbar()

                    if (response.error == 200) {
                        val htmlStrig = response.data

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val htmlAsSpanned: Spanned =
                                Html.fromHtml(htmlStrig, HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtDesc.setText(htmlAsSpanned);
                        } else {
                            val htmlAsSpanned: Spanned = Html.fromHtml(htmlStrig)
                            txtDesc.setText(htmlAsSpanned);
                        }
                    }
                }

                override fun onFailed(code: Int, message: String) {

                    hideProgressbar()

                    showAlert(message)
                }

            }).addTo(autoDisposable)
    }
}