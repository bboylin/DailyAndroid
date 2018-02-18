package xyz.bboylin.dailyandroid.helper.util

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import xyz.bboylin.dailyandroid.helper.Constants

/**
 * Created by lin on 2018/2/11.
 */
object CookieSPUtil {
    private lateinit var context: Context
    private lateinit var sp: SharedPreferences
    private val TAG = CookieSPUtil.javaClass.simpleName

    fun initContext(context: Context) {
        CookieSPUtil.context = context
        sp = context.getSharedPreferences(Constants.SP_COOKIE_KEY, Context.MODE_PRIVATE)
    }

    fun hasLogin(): Boolean {
        LogUtil.d(TAG, "hasLogin:" + sp.getString(Constants.SP_COOKIE_KEY, ""))
        return !TextUtils.isEmpty(sp.getString(Constants.SP_COOKIE_KEY, ""))
    }

    fun saveCookie(cookie: String) {
//        sp?.edit()?.putString(Constants.SP_COOKIE_KEY, CiphserUtil.encrypt(cookie))
        sp.edit().putString(Constants.SP_COOKIE_KEY, cookie).apply()
    }

    fun getCookie(): String = sp.getString(Constants.SP_COOKIE_KEY, "")
}