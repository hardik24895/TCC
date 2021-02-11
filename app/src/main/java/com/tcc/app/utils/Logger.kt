package com.tcc.app.utils

import android.util.Log
import com.tcc.app.BuildConfig


object Logger {
    private val TAG = "Logger"

    fun e(Msg: String) {
        logIt(Log.ERROR, TAG, Msg)
    }

    fun e(Tag: String, Msg: String) {
        logIt(Log.ERROR, Tag, Msg)
    }

    fun i(Msg: String) {
        logIt(Log.INFO, TAG, Msg)
    }

    fun i(Tag: String, Msg: String) {
        logIt(Log.INFO, Tag, Msg)
    }

    fun d(Msg: String) {
        logIt(Log.DEBUG, TAG, Msg)
    }

    fun d(Tag: String, Msg: String) {
        logIt(Log.DEBUG, Tag, Msg)
    }

    fun v(Msg: String) {
        logIt(Log.VERBOSE, TAG, Msg)
    }

    fun v(Tag: String, Msg: String) {
        logIt(Log.VERBOSE, Tag, Msg)
    }

    fun w(Msg: String) {
        logIt(Log.WARN, TAG, Msg)
    }

    fun w(Tag: String, Msg: String) {
        logIt(Log.WARN, Tag, Msg)
    }

    private fun logIt(LEVEL: Int, Tag: String?, Message: String) {
        if (BuildConfig.DEBUG) Log.println(LEVEL, Tag ?: TAG, Message)
    }
}
