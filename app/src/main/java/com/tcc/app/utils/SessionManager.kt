package com.tcc.app.utils

import android.app.NotificationManager
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tcc.app.R
import com.tcc.app.modal.ConfigDataModel
import com.tcc.app.modal.KeyRoleData
import com.tcc.app.modal.LoginModal
import com.tcc.app.modal.StateItem
import java.lang.reflect.Type


class SessionManager(val context: Context) {
    val pref =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = pref.contains(KEY_IS_LOGIN) && pref.getBoolean(KEY_IS_LOGIN, false)
        set(isLoggedIn) = storeDataByKey(KEY_IS_LOGIN, isLoggedIn)

    var isRegester: Boolean
        get() = pref.contains(KEY_IS_REGISTER) && pref.getBoolean(KEY_IS_REGISTER, false)
        set(isRegester) = storeDataByKey(KEY_IS_REGISTER, isRegester)

    var user: LoginModal
        get() {
            val gson = Gson()
            val json = getDataByKey(KEY_USER_INFO, "")
            return gson.fromJson(json, LoginModal::class.java)
        }
        set(user) {
            val gson = Gson()
            val json = gson.toJson(user)
            pref.edit().putString(KEY_USER_INFO, json).apply()
            isLoggedIn = true
        }

    var configData: ConfigDataModel
        get() {
            val gson = Gson()
            val json = getDataByKey(KEY_CONFIG_INFO, "")
            return gson.fromJson(json, ConfigDataModel::class.java)
        }
        set(configData) {
            val gson = Gson()
            val json = gson.toJson(configData)
            pref.edit().putString(KEY_CONFIG_INFO, json).apply()
        }
    var stetList: MutableList<StateItem>
        get() {
            val gson = Gson()
            val json = getDataByKey(STATE_DATA, "")
            val type: Type = object : TypeToken<MutableList<StateItem>>() {}.type
            return gson.fromJson(json, type)
        }
        set(BannerData) {
            val gson = Gson()
            val json = gson.toJson(BannerData)
            pref.edit().putString(STATE_DATA, json).apply()
        }
    var roleData: KeyRoleData
        get() {
            val gson = Gson()
            val json = getDataByKey(KEY_ROLE_DATA, "")
            return gson.fromJson(json, KeyRoleData::class.java)
        }
        set(roleData) {
            val gson = Gson()
            val json = gson.toJson(roleData)
            pref.edit().putString(KEY_ROLE_DATA, json).apply()
        }

    @JvmOverloads
    fun getDataByKey(Key: String, DefaultValue: String = ""): String {
        return if (pref.contains(Key)) {
            pref.getString(Key, DefaultValue)!!
        } else {
            DefaultValue
        }
    }

    @JvmOverloads
    fun getDataByKeyBoolean(Key: String, DefaultValue: Boolean): Boolean {
        return if (pref.contains(Key)) {
            pref.getBoolean(Key, DefaultValue)!!
        } else {
            DefaultValue
        }
    }

    @JvmOverloads
    fun getDataByKeyInt(Key: String, DefaultValue: Int = 0): Int {
        return if (pref.contains(Key)) {
            pref.getInt(Key, DefaultValue)!!
        } else {
            DefaultValue
        }
    }

    fun storeDataByKey(key: String, Value: String) {
        pref.edit().putString(key, Value).apply()
    }

    fun storeDataByKey(key: String, Value: Int) {
        pref.edit().putInt(key, Value).apply()
    }

    fun storeDataByKey(key: String, Value: Boolean) {
        pref.edit().putBoolean(key, Value).apply()
    }

    operator fun contains(key: String): Boolean {
        return pref.contains(key)
    }

    fun remove(key: String) {
        pref.edit().remove(key).apply()
    }

    fun clearSession() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        val editor = context.getSharedPreferences("Session", Context.MODE_PRIVATE).edit()
        editor.clear().apply()

        pref.edit().clear().apply()
    }

    companion object {
        const val KEY_IS_LOGIN = "isLogin"
        const val KEY_IS_REGISTER = "isRegister"
        const val KEY_USER_INFO = "user"
        const val KEY_CONFIG_INFO = "configData"
        const val KEY_ROLE = "key_role"
        const val KEY_ROLE_DATA = "key_role_data"
        const val KEY_EVENT_STATICS = "eventStatics"
        const val HOME_DATA = "homeData"
        const val STATE_DATA = "stateData"
        const val EVENT_DETAIL_BANNER = "eventDetailBanner"
        const val EVENT_LIST = "eventList"
        const val KEY_CITY_ID = "cityId"
        const val KEY_CHECKIN_ID = "checkIn"
    }
}