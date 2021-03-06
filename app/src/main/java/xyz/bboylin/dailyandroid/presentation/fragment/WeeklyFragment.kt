package xyz.bboylin.dailyandroid.presentation.fragment

import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_weekly.*
import org.jsoup.Jsoup
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.domain.interator.GankWelfareInterator
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.helper.util.NetworkUtil
import xyz.bboylin.dailyandroid.presentation.OnLoadMoreListener
import xyz.bboylin.dailyandroid.presentation.adapter.WeeklyAdapter
import xyz.bboylin.universialtoast.UniversalToast
import kotlin.concurrent.thread

/**
 * Created by lin on 2018/2/5.
 */
class WeeklyFragment : BaseFragment() {
    val TAG = WeeklyFragment::class.java.simpleName
    private var latestIssueId = -1
    private var page = 1
    private val list = ArrayList<Any>()
    private var loadMoreEnabled = true
    private val handler = Handler() { msg ->
        if (msg.what == 0x404) {
            multiStatusView.showError()
            true
        }
        if ((latestIssueId > 0) && (page > 1)) {
            val weeklyAdapter = WeeklyAdapter(activity, latestIssueId, list)
            weeklyAdapter.onLoadMoreListener = object : OnLoadMoreListener {
                override fun loadMore() {
                    loadMoreData()
                }
            }
            recyclerView.adapter = weeklyAdapter
            multiStatusView.showContent()
        }
        true
    }

    override fun initData() {
        if (NetworkUtil.networkConnected(activity as Context)) {
            multiStatusView.showLoading()
            loadData()
        } else {
            multiStatusView.showNoNetwork()
        }
    }

    private fun loadData() {
        thread(start = true) {
            try {
                val doc = Jsoup.connect(Constants.WEEKLY_BASE_URL).get()
                val latestTitle = doc.getElementsByClass("h4 title")[0].text().split("#")
                latestIssueId = latestTitle[1].toInt()
                handler.sendEmptyMessage(0x200)
            } catch (e: Exception) {
                e.printStackTrace()
                handler.sendEmptyMessage(0x404)
            }
        }
        val disposable = GankWelfareInterator(page).execute()
                .subscribe({ response ->
                    page++
                    response.gankList.forEach {
                        list.add(it)
                    }
                    handler.sendEmptyMessage(0x200)
                }, { t -> LogUtil.e(TAG, "get falware error", t) })
        compositeDisposable.add(disposable)
    }

    private fun loadMoreData() {
        if (!loadMoreEnabled) {
            (recyclerView.adapter as WeeklyAdapter).showEnd()
            return
        }
        val disposable = GankWelfareInterator(page).execute()
                .subscribe({ response ->
                    if (page * 10 > latestIssueId) {
                        loadMoreEnabled = false
                        val extra = latestIssueId + 10 - page * 10
                        if (extra > 0) {
                            response.gankList = response.gankList.subList(0, extra)
                        }
                    }
                    page++
                    (recyclerView.adapter as WeeklyAdapter).addData(response.gankList)
                }, { t ->
                    LogUtil.e(TAG, "load more failed!", t)
                    (recyclerView.adapter as WeeklyAdapter).showError()
                })
        compositeDisposable.add(disposable)
    }

    override fun initView() {
        multiStatusView.setOnRetryClickListener {
            if (NetworkUtil.networkConnected(activity as Context)) {
                multiStatusView.showLoading()
                loadData()
            } else {
                UniversalToast.makeText(activity as Context, "请检查网络连接"
                        , UniversalToast.LENGTH_SHORT).showWarning()
            }
        }
        val linearLayoutManager = LinearLayoutManager(activity as Context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_weekly
    }
}