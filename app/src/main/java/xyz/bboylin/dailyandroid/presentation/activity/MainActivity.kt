package xyz.bboylin.dailyandroid.presentation.activity

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.Gravity
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.bottom_bar.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.presentation.fragment.BaseFragment
import xyz.bboylin.dailyandroid.presentation.fragment.HomeFragment
import xyz.bboylin.dailyandroid.presentation.fragment.MeFragment
import xyz.bboylin.dailyandroid.presentation.fragment.WeeklyFragment
import xyz.bboylin.dailyandroid.presentation.rxevent.ShowLoginWindowEvent
import xyz.bboylin.dailyandroid.presentation.widget.LoginPopupWindow
import xyz.bboylin.universialtoast.UniversalToast

class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {
    private var homeFragment: HomeFragment? = null
    private var weeklyFragment: WeeklyFragment? = null
    private var meFragment: MeFragment? = null
    private var lastBackTime: Long = 0
    private var checkedId: Int = R.id.rb_home
    override fun onCheckedChanged(radioGroup: RadioGroup?, checkedId: Int) {
        switchFragment(checkedId)
        this.checkedId = checkedId
    }

    private fun switchFragment(checkedId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (checkedId) {
            R.id.rb_home -> {
                if (homeFragment == null) {
                    homeFragment = HomeFragment.getInstance()
                    transaction.add(R.id.fl_main, homeFragment, HomeFragment.TAG)
                } else {
                    transaction.show(homeFragment)
                }
            }
            R.id.rb_weekly -> {
                if (weeklyFragment == null) {
                    weeklyFragment = WeeklyFragment.getInstance()
                    transaction.add(R.id.fl_main, weeklyFragment, WeeklyFragment.TAG)
                } else {
                    transaction.show(weeklyFragment)
                }
            }
            R.id.rb_me -> {
                if (meFragment == null) {
                    meFragment = MeFragment.getInstance()
                    transaction.add(R.id.fl_main, meFragment, MeFragment.TAG)
                } else {
                    transaction.show(meFragment)
                }
            }
        }
        transaction.commit()
    }

    private fun hideFragments(transaction: FragmentTransaction?) {
        homeFragment?.let {
            transaction?.hide(homeFragment)
        }
        weeklyFragment?.let {
            transaction?.hide(weeklyFragment)
        }
        meFragment?.let {
            transaction?.hide(meFragment)
        }
    }

    override fun initData() {
        val disposable = RxBus.get()
                .toObservable(ShowLoginWindowEvent::class.java)
                .subscribe({ t ->
                    LoginPopupWindow.show(this, getCurrentFragment()?.contentView!!)
                }, { t -> LogUtil.e("MainActivity", "弹出登录界面失败", t) })
        compositeDisposable.add(disposable)
    }

    fun getCurrentFragment(): BaseFragment? = when (checkedId) {
        R.id.rb_home -> homeFragment
        R.id.rb_weekly -> weeklyFragment
        R.id.rb_me -> meFragment
        else -> null
    }

    override fun initView() {
        rb_home.isChecked = true
        rg_bottom.setOnCheckedChangeListener(this)
        switchFragment(R.id.rb_home)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - lastBackTime < 2000) {
            finish()
        } else {
            lastBackTime = System.currentTimeMillis()
            UniversalToast.makeText(this, "再次返回退出应用", UniversalToast.LENGTH_SHORT)
                    .setGravity(Gravity.CENTER, 0, 0)
                    .show()
        }
    }
}
