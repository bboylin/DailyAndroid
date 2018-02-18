package xyz.bboylin.dailyandroid.presentation.fragment

import android.view.View
import kotlinx.android.synthetic.main.fragment_me.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.presentation.widget.LoginPopupWindow

/**
 * Created by lin on 2018/2/5.
 */
class MeFragment : BaseFragment() {
    override fun initData() {

    }

    override fun initView() {
        val onClickListener = { v: View? -> LoginPopupWindow.show(activity, contentView) }
        loginTv.setOnClickListener(onClickListener)
        registerTv.setOnClickListener(onClickListener)
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