package xyz.bboylin.dailyandroid.presentation.widget

import android.app.Activity
import android.graphics.Point
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.domain.interator.LoginInterator
import xyz.bboylin.dailyandroid.domain.interator.RegisterInterator
import xyz.bboylin.dailyandroid.helper.util.DensityUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.universialtoast.UniversalToast


/**
 * todo 背景配色太丑了，改改。
 * todo 键盘挡住button了，改。
 * Created by lin on 2018/2/11.
 */
object LoginPopupWindow {
    private val TAG = LoginPopupWindow.javaClass.simpleName
    fun show(context: Activity, parent: View) {
        val view = LayoutInflater.from(context).inflate(R.layout.window_login, null)
        val popupWindow = PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT)
        val display = context.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        popupWindow.width = size.x - DensityUtil.dp2pxInt(context, 90f)
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(context.resources.getDrawable(R.drawable.popupwindow_bg))
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true)
        popupWindow.setOutsideTouchable(true)
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow)
        //设置位置
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0)
        //设置消失监听
        popupWindow.setOnDismissListener(PopupWindow.OnDismissListener { setBackgroundAlpha(context, 1f) })
        //设置PopupWindow的View点击事件
        val usernameTv = view.findViewById<TextView>(R.id.accountTv)
        val passwordTv = view.findViewById<TextView>(R.id.passwordTv)
        val registerTv = view.findViewById<TextView>(R.id.registerTv)
        val loginTv = view.findViewById<TextView>(R.id.loginTv)
        registerTv.setOnClickListener {
            val username = usernameTv.text.toString()
            val password = passwordTv.text.toString()
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                UniversalToast.makeText(context, "请补全账号和密码"
                        , UniversalToast.LENGTH_SHORT, UniversalToast.EMPHASIZE).showWarning()
            } else {
                RegisterInterator(username, password).execute()
                        .flatMap { t -> LoginInterator(username, password).execute() }
                        .subscribe({ t ->
                            //todo 保存收藏列表 t.data.collectIds
                            UniversalToast.makeText(context, "注册成功"
                                    , UniversalToast.LENGTH_SHORT, UniversalToast.EMPHASIZE).showSuccess()
                            popupWindow.dismiss()
                        }, { throwable ->
                            LogUtil.e(TAG, "注册失败", throwable)
                            UniversalToast.makeText(context, "注册失败"
                                    , UniversalToast.LENGTH_SHORT, UniversalToast.EMPHASIZE).showError()
                        })
            }
        }
        loginTv.setOnClickListener {
            val username = usernameTv.text.toString()
            val password = passwordTv.text.toString()
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                UniversalToast.makeText(context, "请补全账号和密码"
                        , UniversalToast.LENGTH_SHORT, UniversalToast.EMPHASIZE).showWarning()
            } else {
                LoginInterator(username, password).execute()
                        .subscribe({ t ->
                            //todo 保存收藏列表 t.data.collectIds
                            UniversalToast.makeText(context, "登录成功"
                                    , UniversalToast.LENGTH_SHORT, UniversalToast.EMPHASIZE).showSuccess()
                            popupWindow.dismiss()
                        }, { throwable ->
                            LogUtil.e(TAG, "登录失败", throwable)
                            UniversalToast.makeText(context, "登录失败"
                                    , UniversalToast.LENGTH_SHORT, UniversalToast.EMPHASIZE).showError()
                        })
            }
        }
        //设置背景色
        setBackgroundAlpha(context, 0.5f)
    }

    //设置屏幕背景透明效果
    fun setBackgroundAlpha(activity: Activity, alpha: Float) {
        val lp = activity.getWindow().getAttributes()
        lp.alpha = alpha
        activity.getWindow().setAttributes(lp)
    }
}