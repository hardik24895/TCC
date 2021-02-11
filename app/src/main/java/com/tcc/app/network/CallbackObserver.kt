package com.tcc.app.network

import android.os.StrictMode
import io.reactivex.observers.DisposableObserver
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.net.InetAddress


abstract class CallbackObserver<T> : DisposableObserver<Response<T>>() {

    abstract fun onSuccess(response: T)
    abstract fun onFailed(code: Int, message: String)

    override fun onComplete() {
        //Nothing happen here
    }

    override fun onNext(t: Response<T>) {
        if (t.isSuccessful) {
            t.body()?.let { onSuccess(it) }
        } else {
            //Logger.e("API failed", t.raw().request().url().toString())
            onFailed(t.code(), getErrorMessage(t.errorBody()))
        }
    }

    override fun onError(e: Throwable) {
        if (!isInternetAvailable()) {
            onFailed(0, "No Internet connection")
        } else if (e is HttpException) {
            onFailed(e.code(), getErrorMessage(e.response()?.errorBody()))
        } else {
            onFailed(0, e.localizedMessage)
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val jsonObject = JSONObject(responseBody!!.string())
            jsonObject.getString("msg")
        } catch (e: Exception) {
            e.message.toString()
        }

    }

    fun isInternetAvailable(): Boolean {
        return try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val ipAddr = InetAddress.getByName("google.com")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }
    }
}