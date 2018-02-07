package xyz.bboylin.dailyandroid.helper.util

import android.content.Context

/**
 * Created by lin on 2018/2/6.
 */
object DensityUtil {

    fun dp2px(context: Context, dp: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun px2dp(context: Context, px: Float): Float {
        val scale = context.resources.displayMetrics.density
        return px / scale + 0.5f
    }

    fun dp2pxInt(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
