package xyz.bboylin.dailyandroid.helper

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


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
        mBus.onNext(obj)
    }

    fun <T> toObservable(tClass: Class<T>): Observable<T> {
        return mBus.ofType(tClass)
    }

    fun toObservable(): Observable<Any> {
        return mBus
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

    companion object {
        fun get(): RxBus = RxBus()
    }
}