package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.GankCategoryResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/15.
 */
class GankWelfareInterator(private val page: Int) : Usecase<GankCategoryResponse>() {
    override fun execute(): Observable<GankCategoryResponse> {
        return repository.getWelfareByPage(page).compose(applySchedulers())
    }
}