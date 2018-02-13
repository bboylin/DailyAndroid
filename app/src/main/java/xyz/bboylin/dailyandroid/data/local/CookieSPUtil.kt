package xyz.bboylin.dailyandroid.data.local

import android.content.Context
import android.text.TextUtils
import xyz.bboylin.dailyandroid.helper.BaseApplication
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.util.CiphserUtil

/**
 * Created by lin on 2018/2/11.
 */
object CookieSPUtil {
    val sp = BaseApplication.getContext().getSharedPreferences(Constants.SP_COOKIE_KEY, Context.MODE_PRIVATE)

    fun hasLogin(): Boolean {
        return !TextUtils.isEmpty(sp.getString(Constants.SP_COOKIE_KEY, ""))
    }

    fun saveCookie(cookie: String) {
        sp.edit().putString(Constants.SP_COOKIE_KEY, CiphserUtil.encrypt(cookie))
        //异步写cookie
        sp.edit().apply()
    }

    fun getCookie(): String = CiphserUtil.decrypt(sp.getString(Constants.SP_COOKIE_KEY, ""))
}