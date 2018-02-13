package xyz.bboylin.dailyandroid.presentation.fragment

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
        loginTv.setOnClickListener {
            LoginPopupWindow.show(activity, contentView!!)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    companion object {
        val TAG = "MeFragment"
        fun getInstance(): MeFragment = MeFragment()
    }
}