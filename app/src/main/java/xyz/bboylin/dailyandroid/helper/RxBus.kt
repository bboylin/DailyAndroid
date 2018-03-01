package xyz.bboylin.dailyandroid.helper

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import xyz.bboylin.dailyandroid.helper.util.LogUtil


/**
 * Created by lin on 2018/2/11.
 */
class RxBus private constructor() {

    private val mBus: Subject<Any>

    init {
        // toSerialized method made bus thread safe
        mBus = PublishSubject.create<Any>().toSerialized()
    }

    fun post(obj: Any) {
        LogUtil.d("RxBus", "post:" + obj.javaClass)
        mBus.onNext(obj)
    }

    fun toObservable(): Observable<Any> {
        return mBus
    }

    // 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    fun <T> toObservable(eventType: Class<T>): Observable<T> {
        //本质是先filter再cast
        return mBus.ofType(eventType)
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    companion object {
        val instance = RxBus()
        fun get(): RxBus = instance
    }
}