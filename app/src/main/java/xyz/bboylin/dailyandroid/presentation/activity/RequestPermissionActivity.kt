package xyz.bboylin.dailyandroid.presentation.activity

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import com.squareup.leakcanary.internal.LeakCanaryInternals.setEnabledBlocking
import com.squareup.leakcanary.internal.RequestStoragePermissionActivity
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.universialtoast.UniversalToast


/**
 * 用来申请权限的透明activity。
 * 由notification 点击启动
 * Created by lin on 2018/3/3.
 */
@TargetApi(M)
class RequestPermissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            if (!hasStoragePermission()) {
                val permissions = arrayOf(WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, 123)
            }
            if (Build.VERSION.SDK_INT >= O && !Settings.canDrawOverlays(this)) {
                UniversalToast.makeText(this, "请允许悬浮窗权限", UniversalToast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName))
                startActivity(intent)
            }
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (!hasStoragePermission()) {
            UniversalToast.makeText(application, getString(R.string.permission_not_granted), UniversalToast.LENGTH_SHORT)
                    .setGravity(Gravity.CENTER, 0, 0)
                    .show()
        }
        finish()
    }

    override fun finish() {
        // Reset the animation to avoid flickering.
        overridePendingTransition(0, 0)
        super.finish()
    }

    private fun hasStoragePermission(): Boolean {
        return checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
    }

    companion object {
        fun createPendingIntent(context: Context): PendingIntent {
            setEnabledBlocking(context, RequestStoragePermissionActivity::class.java, true)
            val intent = Intent(context, RequestStoragePermissionActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP
            return PendingIntent.getActivity(context, 1, intent, FLAG_UPDATE_CURRENT)
        }
    }
}