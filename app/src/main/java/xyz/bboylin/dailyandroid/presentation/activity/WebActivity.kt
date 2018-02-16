package xyz.bboylin.dailyandroid.presentation.activity

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_web.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.helper.Constants

/**
 * 加载URL页面的activity
 * Created by lin on 2018/2/16.
 */
class WebActivity : BaseActivity() {
    private var url: String? = null
    override fun getLayoutId(): Int = R.layout.activity_web

    override fun initView() {
        tv.text = url
    }

    override fun initData() {
        url = intent.getStringExtra(Constants.WEB_ACTIVITY_EXTRA_KEY)
    }

    companion object {
        fun start(context: Context, url: String) {
            //这里不能用WebActivity.javaClass，否则结果是WebActivity$Companion
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(Constants.WEB_ACTIVITY_EXTRA_KEY, url)
            context.startActivity(intent)
        }
    }
}