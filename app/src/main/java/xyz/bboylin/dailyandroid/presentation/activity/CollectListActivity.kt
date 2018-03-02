package xyz.bboylin.dailyandroid.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_collectlist.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.CollectItem
import xyz.bboylin.dailyandroid.domain.interator.CollectListInterator
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.helper.util.NetworkUtil
import xyz.bboylin.dailyandroid.presentation.adapter.CollectionAdapter
import xyz.bboylin.dailyandroid.presentation.widget.SimpleItemDecoration
import xyz.bboylin.universialtoast.UniversalToast

/**
 * Created by lin on 2018/2/21.
 */
class CollectListActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_collectlist

    override fun initView() {
        setSupportActionBar(toolbar)
        setupToolbar(R.string.collection)
        multiStatusView.setOnRetryClickListener {
            if (NetworkUtil.networkConnected(this)) {
                multiStatusView.showLoading()
                loadData()
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
            addItemDecoration(SimpleItemDecoration(8))
        }
    }

    override fun initData() {
        if (NetworkUtil.networkConnected(this)) {
            multiStatusView.showLoading()
            loadData()
        } else {
            multiStatusView.showNoNetwork()
        }
    }

    private fun loadData() {
        val disposable = CollectListInterator(0).execute()
                .flatMap { response -> mergedObservable(response.data.pageCount, response.data.datas) }
                .subscribe({ list ->
                    if (list != null && list.size > 0) {
                        recyclerView.adapter = CollectionAdapter(ArrayList(list))
                        multiStatusView.showContent()
                    } else {
                        multiStatusView.showEmpty()
                    }
                }, { throwable ->
                    LogUtil.e(TAG, "load data error", throwable)
                    multiStatusView.showError()
                })
        compositeDisposable.add(disposable)
    }

    private fun mergedObservable(pageCount: Int, list: List<CollectItem>?): Observable<List<CollectItem>?> {
        var observable = Observable.just(list)
        for (i in 1..pageCount - 1) {
            LogUtil.d(TAG, "i:${i}")
            observable = Observable.zip(observable
                    , CollectListInterator(i).execute().map { t -> t.data.datas }
                    , object : BiFunction<List<CollectItem>?, List<CollectItem>?, List<CollectItem>?> {
                override fun apply(t1: List<CollectItem>, t2: List<CollectItem>)
                        : List<CollectItem> = t1.plus(t2.asIterable())
            })
        }
        return observable
    }

    companion object {
        private val TAG = CollectListActivity::class.java.simpleName
        fun startFrom(context: Context) {
            context.startActivity(Intent(context, CollectListActivity::class.java))
        }
    }
}