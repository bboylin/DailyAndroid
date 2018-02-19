package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.CollectOutsideResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/19.
 */
class CollectOutsideInterator(val title: String, val author: String, val link: String) : Usecase<CollectOutsideResponse>() {
    override fun execute(): Observable<CollectOutsideResponse> {
        return repository.collectOutside(title, author, link).compose(applySchedulers())
    }
}