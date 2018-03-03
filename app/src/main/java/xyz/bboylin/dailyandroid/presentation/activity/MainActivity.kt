package xyz.bboylin.dailyandroid.presentation.activity

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.FragmentTransaction
import android.view.Gravity
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.bottom_bar.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.LoginEvent
import xyz.bboylin.dailyandroid.helper.rxevent.LoginSuccessEvent
import xyz.bboylin.dailyandroid.helper.util.AccountUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.presentation.fragment.BaseFragment
import xyz.bboylin.dailyandroid.presentation.fragment.HomeFragment
import xyz.bboylin.dailyandroid.presentation.fragment.MeFragment
import xyz.bboylin.dailyandroid.presentation.fragment.WeeklyFragment
import xyz.bboylin.dailyandroid.presentation.service.CollectionService
import xyz.bboylin.dailyandroid.presentation.widget.LoginPopupWindow
import xyz.bboylin.universialtoast.UniversalToast


class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {
    private val NOTIFICATION_CHANNEL_ID = "dailyAndroid"
    private var homeFragment: HomeFragment? = null
    private var weeklyFragment: WeeklyFragment? = null
    private var meFragment: MeFragment? = null
    private var lastBackTime: Long = 0
    private var checkedId: Int = R.id.rb_home
    private var notificationDiaplayed = false
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
                    homeFragment = HomeFragment()
                    transaction.add(R.id.fl_main, homeFragment, (homeFragment as HomeFragment).TAG)
                } else {
                    transaction.show(homeFragment)
                }
            }
            R.id.rb_weekly -> {
                if (weeklyFragment == null) {
                    weeklyFragment = WeeklyFragment()
                    transaction.add(R.id.fl_main, weeklyFragment, (weeklyFragment as WeeklyFragment).TAG)
                } else {
                    transaction.show(weeklyFragment)
                }
            }
            R.id.rb_me -> {
                if (meFragment == null) {
                    meFragment = MeFragment()
                    transaction.add(R.id.fl_main, meFragment, (meFragment as MeFragment).TAG)
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
        if (AccountUtil.hasLogin()) {
            startService(Intent(this, CollectionService::class.java))
        }
        if (!allPermissionsGranted()) {
            requestPermissionNotification()
        }
        val disposable = RxBus.get()
                .toObservable()
                .subscribe({ t ->
                    if (t is LoginEvent) {
                        val curFragment = getCurrentFragment()
                        curFragment?.let {
                            LoginPopupWindow.show(this, curFragment.contentView)
                        }
                    } else if (t is LoginSuccessEvent) {
                        if (AccountUtil.hasLogin()) {
                            startService(Intent(this, CollectionService::class.java))
                        }
                    }
                }, { t -> LogUtil.e("MainActivity", "error", t) })
        compositeDisposable.add(disposable)
    }

    private fun requestPermissionNotification() {
        if (notificationDiaplayed) {
            return
        }
        notificationDiaplayed = true

        val pendingIntent = RequestPermissionActivity.createPendingIntent(this)
        val contentTitle = getString(R.string.permission_notification_title)
        val packageName = getPackageName()
        val contentText = getString(R.string.permission_notification_text, packageName)
        showNotification(contentTitle, contentText, pendingIntent, 0x1DDDDDDD)
    }

    fun showNotification(contentTitle: CharSequence, contentText: CharSequence
                         , pendingIntent: PendingIntent, notificationId: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = Notification.Builder(this) //
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        if (SDK_INT >= O) {
            val channelName = getString(R.string.permission_notification_channel)
            setupNotificationChannel(channelName, notificationManager, builder)
        }
        notificationManager.notify(notificationId, builder.build())
    }

    @TargetApi(O)
    private fun setupNotificationChannel(channelName: String,
                                         notificationManager: NotificationManager, builder: Notification.Builder) {
        if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName,
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
    }

    /**
     * 写存储权限和悬浮窗权限(8.0)
     */
    private fun allPermissionsGranted(): Boolean {
        if (Build.VERSION.SDK_INT < M) {
            return true
        }
        val writeStoragePermissionGranted = checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
        if (!writeStoragePermissionGranted) {
            return false
        }
        if (Build.VERSION.SDK_INT < O) {
            return true
        }
        return Settings.canDrawOverlays(this)
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
            homeFragment = null
            meFragment = null
            weeklyFragment = null
            finish()
        } else {
            lastBackTime = System.currentTimeMillis()
            UniversalToast.makeText(this, "再次返回退出应用", UniversalToast.LENGTH_SHORT)
                    .setGravity(Gravity.CENTER, 0, 0)
                    .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, CollectionService::class.java))
    }
}
