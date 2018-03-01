package xyz.bboylin.dailyandroid.helper.util

import android.util.Log

/**
 * Created by lin on 2018/2/5.
 */
object LogUtil {
    val DEBUG = true
    fun d(tag: String, msg: String) {
        if (DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String, th: Throwable) {
        if (DEBUG) {
            Log.e(tag, msg, th)
        }
    }

    fun e(tag: String, msg: String) {
        if (DEBUG) {
            Log.e(tag, msg)
        }
    }
}