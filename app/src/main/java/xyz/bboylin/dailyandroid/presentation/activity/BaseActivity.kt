package xyz.bboylin.dailyandroid.presentation.activity

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.ActionBar
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

    protected fun setupToolbar(@StringRes res: Int, hasParent: Boolean = true) {
        val actionBar = supportActionBar as ActionBar
        actionBar?.let {
            if (hasParent) {
                actionBar.setHomeButtonEnabled(true)
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(res);
        }
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()
}