package xyz.bboylin.dailyandroid.helper.util

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import xyz.bboylin.dailyandroid.helper.Constants


/**
 * Created by lin on 2018/3/6.
 */
object AppUtil {
    private lateinit var context: Context
    private lateinit var sp: SharedPreferences

    fun initContext(context: Context) {
        this.context = context
        sp = context.getSharedPreferences(Constants.SP_UPDATE_KEY, Context.MODE_PRIVATE)
    }

    /**
     * 得到软件版本号
     *
     * @param context 上下文
     * @return 当前版本Code
     */
    fun getVersionCode(): Int {
        var verCode = -1
        try {
            val packageName = context.getPackageName()
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verCode
    }

    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    fun getVersionName(): String {
        var verName = ""
        try {
            val packageName = context.getPackageName()
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return verName
    }

    fun checkUpdate(versionName: String): Boolean {
        try {
            val currVer = getVersionName().split(".")
            val futureVer = versionName.split(".")
            for (i in 0..futureVer.size) {
                if (futureVer[i].toInt() > currVer[i].toInt()) {
                    return true
                } else if (futureVer[i].toInt() < currVer[i].toInt()) {
                    return false
                }
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun setUpdateFlag(hasUpdate: Boolean) {
        sp.edit().putBoolean(Constants.SP_UPDATE_KEY, hasUpdate).apply()
    }

    fun getUpdateFlag(): Boolean = sp.getBoolean(Constants.SP_UPDATE_KEY, false)
}