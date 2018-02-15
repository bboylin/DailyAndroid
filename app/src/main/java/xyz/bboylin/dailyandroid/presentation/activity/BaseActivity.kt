package xyz.bboylin.dailyandroid.presentation.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lin on 2018/2/5.
 * 公用的上层activity
 */
abstract class BaseActivity : AppCompatActivity() {
    protected val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initData()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()
}