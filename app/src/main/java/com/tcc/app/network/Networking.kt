package com.tcc.app.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class Networking(private val context: Context) {

    val prefs = context.getSharedPreferences("Session", Context.MODE_PRIVATE)

    companion object {
        /**
         * @param context
         * @return Instance of this class
         * create instance of this class
         */
        fun with(context: Context): Networking {
            return Networking(context)
        }


        /*fun wrapParams(params: HashMap<String, *>): RequestBody {
            return JSONObject(params).toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        }*/

        /*fun wrapParams(params: String): RequestBody {
            return params
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        }*/

        fun wrapParams(params: String): RequestBody {
            return params
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        }

        fun setParentJsonData(
            methodName: String,
            jsonBody: JSONObject
        ): String {
            val jsonObject = JSONObject()
            try {
                jsonObject.put(Constant.METHOD, methodName)
                jsonObject.put(Constant.BODY, jsonBody)
                Logger.d("Request::::> $jsonObject")
                return jsonObject.toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return jsonObject.toString()
        }

    }

    fun getServices(): APIInterface {
        val cacheSize = (10 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(60, TimeUnit.SECONDS)
        httpClient.connectTimeout(60, TimeUnit.SECONDS)

        //Session
        val token = prefs.getString("TOKEN", null)
        httpClient.interceptors().add(SessionInterceptor(context, token))

        //Log
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        //GSON converter
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(ItemTypeAdapterFactory())
            .create()

        return retrofit2.Retrofit.Builder()
            .baseUrl(Constant.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build().create(APIInterface::class.java)
    }
}

