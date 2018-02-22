package xyz.bboylin.dailyandroid.presentation.activity

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_collectlist.*
import xyz.bboylin.dailyandroid.R

/**
 * Created by lin on 2018/2/21.
 */
class CollectListActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_collectlist

    override fun initView() {
        setSupportActionBar(toolbar)
        setupToolbar(R.string.collection)
    }

    override fun initData() {
    }

    companion object {
        fun startFrom(context: Context) {
            context.startActivity(Intent(context, CollectListActivity::class.java))
        }
    }
}