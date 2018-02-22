package xyz.bboylin.dailyandroid.presentation.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import xyz.bboylin.dailyandroid.data.entity.CollectItem
import xyz.bboylin.dailyandroid.domain.interator.CollectListInterator
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.CollectionReadyEvent
import xyz.bboylin.dailyandroid.helper.util.AccountUtil
import xyz.bboylin.dailyandroid.helper.util.CollectionUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil

/**
 * Created by lin on 2018/2/19.
 */
class CollectionService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    private val compositeDisposable = CompositeDisposable()
    private val TAG = CollectionService::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        LogUtil.d(TAG, "onCreate")
        if (!AccountUtil.hasLogin()) {
            return
        }
        LogUtil.d(TAG, "hasLogin")
        val set = HashSet<String>()
        val disposable = CollectListInterator(0).execute()
                .flatMap { response -> mergedObservable(response.data.pageCount, response.data.datas) }
                .subscribe({ list ->
                    LogUtil.d(TAG, "list size:${list?.size}")
                    list?.forEach {
                        if (it.originId == -1) {
                            set.add("${it.link}$#$${it.id}")
                        }
                    }
                    //持久化set
                    set.forEach {
                        LogUtil.d(TAG, "link:${it}")
                    }
                    CollectionUtil.saveCollections(set)
                    if (set.size > 0) {
                        RxBus.get().post(CollectionReadyEvent())
                    }
                }, { t -> LogUtil.e(TAG, "获取收藏列表失败", t) })
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}