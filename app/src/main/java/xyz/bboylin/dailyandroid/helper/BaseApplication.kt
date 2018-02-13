package xyz.bboylin.dailyandroid.helper

import android.app.Application
import android.content.Context

/**
 * Created by lin on 2018/2/11.
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BaseApplication.Companion.base = applicationContext
    }

    companion object {
        private var base: Context? = null
        fun getContext(): Context = base!!
    }
}