package xyz.bboylin.dailyandroid.Presentation.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_content.*
import xyz.bboylin.dailyandroid.Presentation.OnLoadMoreListener
import xyz.bboylin.dailyandroid.Presentation.adapter.HomeAdapter
import xyz.bboylin.dailyandroid.Presentation.widget.SimpleItemDecoration
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.domain.interator.GankHomeInterator
import xyz.bboylin.dailyandroid.domain.interator.WanHomeInterator
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.helper.util.NetworkUtil

/**
 * Created by lin on 2018/2/5.
 */
class HomeFragment : BaseFragment() {
    //注意，干货营的page从1开始，玩Android从0开始
    private var page = 0

    override fun initData() {
        if (NetworkUtil.networkConnected(activity as Context)) {
            multiStatusView.showLoading()
            loadData(true)
        } else {
            multiStatusView.showNoNetwork()
        }
    }

    private fun loadData(firstTime: Boolean) {
        Observable.mergeDelayError(GankHomeInterator(page + 1).execute().map { response -> response.gankList }
                , WanHomeInterator(page).execute().map { response -> response.data!!.datas })
                .subscribe({ list ->
                    (recyclerView.adapter as HomeAdapter).addData(list)
                    page++
                    multiStatusView.showContent()
                }, { throwable ->
                    LogUtil.e(TAG, "get home data error", throwable)
                    if (firstTime) {
                        multiStatusView.showError()
                    } else {
                        //加载的进度条显示加载失败
                    }
                })
    }

    override fun initView() {
        val linearLayoutManager = LinearLayoutManager(activity as Context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val homeAdapter = HomeAdapter(ArrayList<Any>())
        homeAdapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun loadMore() {
                loadData(false)
            }
        })
        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            addItemDecoration(SimpleItemDecoration())
            adapter = homeAdapter
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_content
    }

    companion object {
        val TAG = "HomeFragment"
        fun getInstance(): HomeFragment = HomeFragment()
    }
}