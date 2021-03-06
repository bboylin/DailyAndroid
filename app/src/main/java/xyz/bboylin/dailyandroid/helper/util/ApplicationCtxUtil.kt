package xyz.bboylin.dailyandroid.helper.util

import android.content.Context

/**
 * Created by lin on 2018/2/19.
 */
object ApplicationCtxUtil {
    fun initContext(context: Context) {
        AccountUtil.initContext(context)
        CollectionUtil.initContext(context)
        WebUtil.initContext(context)
        AppUtil.initContext(context)
    }
}