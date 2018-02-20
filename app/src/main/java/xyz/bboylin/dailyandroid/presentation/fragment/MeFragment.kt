package xyz.bboylin.dailyandroid.presentation.fragment

import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.fragment_me.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.LoginSuccessEvent
import xyz.bboylin.dailyandroid.helper.util.AccountUtil
import xyz.bboylin.dailyandroid.presentation.widget.LoginPopupWindow

/**
 * Created by lin on 2018/2/5.
 */
class MeFragment : BaseFragment() {
    private lateinit var userName: String
    override fun initData() {
        userName = AccountUtil.getUserName()
    }

    override fun initView() {
        subscribeLoginEvent()
        val onClickListener = { v: View? -> LoginPopupWindow.show(activity, contentView) }
        registerTv.setOnClickListener(onClickListener)
        collectionTv.setOnClickListener {
            //todo 收藏列表
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

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    companion object {
        val TAG = "MeFragment"
        private val INSTANCE = MeFragment()
        fun getInstance(): MeFragment = INSTANCE
    }
}