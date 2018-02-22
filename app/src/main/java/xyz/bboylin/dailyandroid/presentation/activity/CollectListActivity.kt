package xyz.bboylin.dailyandroid.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_collectlist.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.domain.interator.CollectListInterator
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.helper.util.NetworkUtil
import xyz.bboylin.dailyandroid.presentation.OnLoadMoreListener
import xyz.bboylin.dailyandroid.presentation.adapter.CollectionAdapter
import xyz.bboylin.dailyandroid.presentation.widget.SimpleItemDecoration
import xyz.bboylin.universialtoast.UniversalToast
import java.util.*

/**
 * todo showEnd()失效
 * Created by lin on 2018/2/21.
 */
class CollectListActivity : BaseActivity() {
    private var page = 0
    private var over = false

    override fun getLayoutId(): Int = R.layout.activity_collectlist

    override fun initView() {
        setSupportActionBar(toolbar)
        setupToolbar(R.string.collection)
        multiStatusView.setOnRetryClickListener {
            if (NetworkUtil.networkConnected(this)) {
                multiStatusView.showLoading()
                loadData(true)
            } else {
                UniversalToast.makeText(this, "请检查网络连接"
                        , UniversalToast.LENGTH_SHORT)
                        .setGravity(Gravity.CENTER, 0, 0)
                        .showWarning()
            }
        }
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            val collectionAdapter = CollectionAdapter(this@CollectListActivity, ArrayList<Any>())
            collectionAdapter.onLoadMoreListener = object : OnLoadMoreListener {
                override fun loadMore() {
                    loadData(false)
                }
            }
            adapter = collectionAdapter
            addItemDecoration(SimpleItemDecoration(8))
        }
    }

    override fun initData() {
        if (NetworkUtil.networkConnected(this)) {
            multiStatusView.showLoading()
            loadData(true)
        } else {
            multiStatusView.showNoNetwork()
        }
    }

    private fun loadData(firstTime: Boolean) {
        if (over) {
            LogUtil.d(TAG, "OVER")
            (recyclerView.adapter as CollectionAdapter).showEnd()
            return
        }
        val disposable = CollectListInterator(page).execute()
                .subscribe({ response ->
                    over = response.data.over
                    page++
                    if (firstTime) {
                        multiStatusView.showContent()
                    }
                    val list = response.data.datas
                    list?.let {
                        (recyclerView.adapter as CollectionAdapter).addData(list)
                    }
                }, { throwable ->
                    LogUtil.e(TAG, "load data error", throwable)
                    if (firstTime) {
                        multiStatusView.showError()
                    } else {
                        (recyclerView.adapter as CollectionAdapter).showError()
                    }
                })
        compositeDisposable.add(disposable)
    }

    companion object {
        private val TAG = CollectListActivity::class.java.simpleName
        fun startFrom(context: Context) {
            context.startActivity(Intent(context, CollectListActivity::class.java))
        }
    }
}