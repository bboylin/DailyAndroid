package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.CollectListResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/19.
 */
class CollectListInterator(private val page: Int) : Usecase<CollectListResponse>() {
    override fun execute(): Observable<CollectListResponse> {
        return repository.getCollectList(page).compose(applySchedulers())
    }
}