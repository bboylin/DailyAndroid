package xyz.bboylin.dailyandroid.presentation.fragment

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_home.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.GankHomeItem
import xyz.bboylin.dailyandroid.data.entity.WanHomeItem
import xyz.bboylin.dailyandroid.domain.interator.GankHomeInterator
import xyz.bboylin.dailyandroid.domain.interator.WanHomeInterator
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.helper.util.NetworkUtil
import xyz.bboylin.dailyandroid.presentation.OnLoadMoreListener
import xyz.bboylin.dailyandroid.presentation.adapter.HomeAdapter
import xyz.bboylin.dailyandroid.presentation.widget.SimpleItemDecoration
import xyz.bboylin.universialtoast.UniversalToast

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
        Observable.mergeDelayError(WanHomeInterator(page).execute().map { response -> response.data!!.datas }
                , GankHomeInterator(page + 1).execute().map { response -> response.gankList })
                .subscribe({ list ->
                    (recyclerView.adapter as HomeAdapter).addData(list!!)
                    page++
                    multiStatusView.showContent()
                }, { throwable ->
                    LogUtil.e(TAG, "load data error", throwable)
                    if (firstTime) {
                        multiStatusView.showError()
                    } else {
                        (recyclerView.adapter as HomeAdapter).showError()
                    }
                })
    }

    override fun initView() {
        multiStatusView.setOnRetryClickListener {
            if (NetworkUtil.networkConnected(activity as Context)) {
                multiStatusView.showLoading()
                loadData(true)
            } else {
                UniversalToast.makeText(activity as Context, "请检查网络连接"
                        , UniversalToast.LENGTH_SHORT).showWarning()
            }
        }
        refreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.YELLOW, Color.RED)
        refreshLayout.setDistanceToTriggerSync(300)
        refreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE)
        refreshLayout.setOnRefreshListener {
            if (NetworkUtil.networkConnected(activity as Context)) {
                refreshData()
            } else {
                UniversalToast.makeText(activity as Context, "请检查网络"
                        , UniversalToast.LENGTH_SHORT, UniversalToast.EMPHASIZE).showWarning()
            }
        }
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

    private fun refreshData() {
        Observable.zip(WanHomeInterator(0).execute().map { response -> response.data!!.datas }
                , GankHomeInterator(1).execute().map { response -> response.gankList }
                , object : BiFunction<List<WanHomeItem>?, List<GankHomeItem>?, ArrayList<Any>> {
            override fun apply(t1: List<WanHomeItem>, t2: List<GankHomeItem>): ArrayList<Any> {
                val list = ArrayList<Any>()
                for (item in t1) {
                    list.add(item)
                }
                for (item in t2) {
                    list.add(item)
                }
                return list
            }
        })
                .subscribe({ list ->
                    (recyclerView.adapter as HomeAdapter).refreshData(list!!)
                    UniversalToast.makeText(activity as Context, "刷新成功", UniversalToast.LENGTH_SHORT)
                            .showSuccess()
                    page = 1
                    refreshLayout.isRefreshing = false
                }, { t ->
                    LogUtil.e(TAG, "刷新失败", t)
                    UniversalToast.makeText(activity as Context, "刷新失败", UniversalToast.LENGTH_SHORT)
                            .showError()
                    refreshLayout.isRefreshing = false
                })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    companion object {
        val TAG = "HomeFragment"
        fun getInstance(): HomeFragment = HomeFragment()
    }
}