package xyz.bboylin.dailyandroid.Presentation.fragment

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*
import xyz.bboylin.dailyandroid.Presentation.OnLoadMoreListener
import xyz.bboylin.dailyandroid.Presentation.adapter.HomeAdapter
import xyz.bboylin.dailyandroid.Presentation.widget.SimpleItemDecoration
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.domain.interator.GankHomeInterator
import xyz.bboylin.dailyandroid.domain.interator.WanHomeInterator
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.helper.util.NetworkUtil
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
        Observable.concat(WanHomeInterator(page).execute().map { response -> response.data!!.datas }
                , GankHomeInterator(page + 1).execute().map { response -> response.gankList })
                .subscribe({ list ->
                    (recyclerView.adapter as HomeAdapter).addData(list!!)
                    page++
                    multiStatusView.showContent()
                }, { throwable ->
                    if (firstTime) {
                        multiStatusView.showError()
                    } else {
                        (recyclerView.adapter as HomeAdapter).showError()
                    }
                })
    }

    override fun initView() {
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
        Observable.concat(WanHomeInterator(0).execute().map { response -> response.data!!.datas }
                , GankHomeInterator(1).execute().map { response -> response.gankList })
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