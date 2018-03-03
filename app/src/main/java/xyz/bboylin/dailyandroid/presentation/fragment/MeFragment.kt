package xyz.bboylin.dailyandroid.presentation.fragment

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.fragment_me.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.LoginEvent
import xyz.bboylin.dailyandroid.helper.rxevent.LoginSuccessEvent
import xyz.bboylin.dailyandroid.helper.util.AccountUtil
import xyz.bboylin.dailyandroid.helper.util.WebUtil
import xyz.bboylin.dailyandroid.presentation.activity.CollectListActivity
import xyz.bboylin.dailyandroid.presentation.activity.PostGankActivity
import xyz.bboylin.dailyandroid.presentation.widget.LoginPopupWindow
import xyz.bboylin.universialtoast.UniversalToast

/**
 * Created by lin on 2018/2/5.
 */
class MeFragment : BaseFragment() {
    val TAG = MeFragment::class.java.simpleName
    private lateinit var userName: String
    override fun initData() {
        userName = AccountUtil.getUserName()
    }

    override fun initView() {
        subscribeLoginEvent()
        tv_history.setOnClickListener(EmptyListener("历史"))
        tv_clean_cache.setOnClickListener(EmptyListener("清空缓存"))
        tv_change_skin.setOnClickListener(EmptyListener("夜间模式"))
        tv_post.setOnClickListener { v -> PostGankActivity.startFrom(activity) }
        tv_about.setOnClickListener { v -> WebUtil.show(Constants.ABOUT_URL) }
        tv_feedback.setOnClickListener { v ->
            UniversalToast.makeText(activity, "欢迎提issue"
                    , UniversalToast.LENGTH_SHORT, UniversalToast.CLICKABLE)
                    .setClickCallBack("前往", { v -> WebUtil.show(Constants.ABOUT_URL) })
                    .show()
        }
        val onClickListener = { v: View? -> LoginPopupWindow.show(activity, contentView) }
        registerTv.setOnClickListener(onClickListener)
        collectionTv.setOnClickListener {
            if (!AccountUtil.hasLogin()) {
                RxBus.get().post(LoginEvent())
                return@setOnClickListener
            }
            CollectListActivity.startFrom(activity)
        }
        if (!TextUtils.isEmpty(userName)) {
            loginTv.text = userName
            registerTv.text = "切换登录账号"
        } else {
            loginTv.setOnClickListener(onClickListener)
        }
    }

    fun subscribeLoginEvent() {
        val disposable = RxBus.get().toObservable(LoginSuccessEvent::class.java)
                .subscribe({ loginSuccessEvent ->
                    loginTv.text = loginSuccessEvent.userName
                    registerTv.text = "切换登录账号"
                })
        compositeDisposable.add(disposable)
    }

    override fun getLayoutId(): Int = R.layout.fragment_me

    class EmptyListener(val msg: String) : View.OnClickListener {
        override fun onClick(v: View) {
            UniversalToast.makeText(v.context, msg, UniversalToast.LENGTH_SHORT)
                    .setGravity(Gravity.CENTER, 0, 0)
                    .show()
        }
    }
}