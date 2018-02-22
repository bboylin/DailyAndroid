package xyz.bboylin.dailyandroid.helper

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.squareup.leakcanary.LeakCanary
import xyz.bboylin.dailyandroid.helper.util.SpUtil

/**
 * Created by lin on 2018/2/11.
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this)
        Fresco.initialize(this)
        SpUtil.initContext(applicationContext)
    }
}