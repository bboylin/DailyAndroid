package xyz.bboylin.dailyandroid.helper.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by lin on 2018/2/8.
 */
object NetworkUtil {
    fun networkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}