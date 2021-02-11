package com.tcc.app.network

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.tcc.app.activity.MainActivity
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class SessionInterceptor(val context: Context, var token: String?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val session = SessionManager(context)

        val builder = original.newBuilder()
        builder.header("Accept", "application/json")
        if (session.isLoggedIn) {
            builder.header("Authorization", token.toString())
        }
        builder.method(original.method, original.body)
        val request = builder.build()
        val response = chain.proceed(request)
        if (response.code == 401) {
            Logger.e("Session Expired : Endpoint", response.request.url.encodedPath)

            val intent = Intent(context, MainActivity::class.java)
            try {
                val jsonObject = JSONObject(response.body?.string())
                val msg = jsonObject.optString("msg")
                intent.putExtra(Constant.ERROR, msg)
            } catch (e: Exception) {
                intent.putExtra(Constant.ERROR, "${e.message}")
            }
            session.clearSession()
            intent.putExtra(Constant.UNAUTHORIZED, response.code)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            ContextCompat.startActivity(context, intent, null)
        }
        return response
    }
}