package xyz.bboylin.dailyandroid.domain

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.bboylin.dailyandroid.data.repositoryImpl.RepositoryImpl

/**
 * Created by lin on 2018/2/6.
 */
abstract class Usecase<T> {
    protected val repository = RepositoryImpl.getInstance()

    abstract fun execute(): Observable<T>

    /**
     * 生成一个Transformer，组合subscribeOn和observeOn的操作
     */
    fun applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer<T, T> { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
