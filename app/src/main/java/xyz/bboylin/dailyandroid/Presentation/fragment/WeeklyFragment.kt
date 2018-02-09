package xyz.bboylin.dailyandroid.Presentation.fragment

import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_weekly.*
import org.jsoup.Jsoup
import xyz.bboylin.dailyandroid.Presentation.adapter.WeeklyAdapter
import xyz.bboylin.dailyandroid.Presentation.widget.SimpleItemDecoration
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.util.NetworkUtil
import kotlin.concurrent.thread

/**
 * Created by lin on 2018/2/5.
 */
class WeeklyFragment : BaseFragment() {
    private var latestIssueId = -1
    private val handler = Handler()
    override fun initData() {
        if (NetworkUtil.networkConnected(activity as Context)) {
            multiStatusView.showLoading()
            loadData()
        } else {
            multiStatusView.showNoNetwork()
        }
    }

    private fun loadData() {
        try {
            thread(start = true) {
                val doc = Jsoup.connect(Constants.WEEKLY_BASE_URL).get()
                val latestTitle = doc.getElementsByClass("h4 title")[0].text().split("#")
                latestIssueId = latestTitle[1].toInt()
                handler.post {
                    val list = ArrayList<Int>()
                    for (i in 0..latestIssueId - 1) {
                        list.add(latestIssueId - i)
                    }
                    recyclerView.adapter = WeeklyAdapter(list)
                    multiStatusView.showContent()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            multiStatusView.showError()
        }
    }

    override fun initView() {
        multiStatusView.setOnRetryClickListener {
            multiStatusView.showLoading()
            loadData()
        }
        val linearLayoutManager = LinearLayoutManager(activity as Context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            addItemDecoration(SimpleItemDecoration())
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_weekly
    }

    companion object {
        val TAG = "WeeklyFragment"
        fun getInstance(): WeeklyFragment = WeeklyFragment()
    }
}