package xyz.bboylin.dailyandroid.helper

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import xyz.bboylin.dailyandroid.helper.util.CookieSPUtil

/**
 * Created by lin on 2018/2/11.
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        CookieSPUtil.initContext(applicationContext)
    }
}