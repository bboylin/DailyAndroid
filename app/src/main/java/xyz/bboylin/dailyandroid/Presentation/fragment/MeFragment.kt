package xyz.bboylin.dailyandroid.Presentation.fragment

import xyz.bboylin.dailyandroid.R

/**
 * Created by lin on 2018/2/5.
 */
class MeFragment : BaseFragment() {
    override fun initData() {

    }

    override fun initView() {
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_content
    }

    companion object {
        val TAG = "MeFragment"
        fun getInstance(): MeFragment = MeFragment()
    }
}