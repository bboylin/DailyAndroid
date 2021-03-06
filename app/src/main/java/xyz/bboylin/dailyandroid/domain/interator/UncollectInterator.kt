package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.BaseResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/13.
 */
class UncollectInterator(private val id: Int) : Usecase<BaseResponse>() {
    override fun execute(): Observable<BaseResponse> {
        return repository.unCollect(id).compose(applySchedulers())
    }
}