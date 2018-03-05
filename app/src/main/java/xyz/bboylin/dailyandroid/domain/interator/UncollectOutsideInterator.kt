package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.BaseResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/20.
 */
class UncollectOutsideInterator(val id: Int, val originId: Int = -1) : Usecase<BaseResponse>() {
    override fun execute(): Observable<BaseResponse> {
        return repository.uncollectOutside(id, originId).compose(applySchedulers())
    }
}