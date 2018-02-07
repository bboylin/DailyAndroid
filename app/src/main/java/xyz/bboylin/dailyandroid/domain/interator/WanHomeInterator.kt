package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.WanHomeResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/7.
 */
class WanHomeInterator(private val page: Int) : Usecase<WanHomeResponse>() {
    override fun execute(): Observable<WanHomeResponse> {
        return repository.getWanByPage(page).compose(applySchedulers())
    }

}