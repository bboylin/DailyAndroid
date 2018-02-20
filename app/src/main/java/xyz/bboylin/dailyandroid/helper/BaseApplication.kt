package xyz.bboylin.dailyandroid.helper

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import xyz.bboylin.dailyandroid.helper.util.SpUtil

/**
 * Created by lin on 2018/2/11.
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        SpUtil.initContext(applicationContext)
    }
}