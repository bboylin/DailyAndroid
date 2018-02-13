package xyz.bboylin.dailyandroid.presentation.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by lin on 2018/2/5.
 * 公用的上层activity
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initData()
        initView()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()
}