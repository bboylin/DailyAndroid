package xyz.bboylin.dailyandroid.helper.util

import android.content.Context
import com.thefinestartist.finestwebview.FinestWebView

/**
 * Created by lin on 2018/3/2.
 */
object WebUtil {
    private lateinit var context: Context

    fun initContext(context: Context) {
        this.context = context
    }

    fun show(url: String) {
        FinestWebView.Builder(context)
                .webViewAppCacheEnabled(true)
                .webViewBuiltInZoomControls(true)
                .webViewJavaScriptEnabled(true)
                .webViewSupportZoom(true)
                .show(url)
    }
}