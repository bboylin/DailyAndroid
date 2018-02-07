package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.GankHomeResponse
import xyz.bboylin.dailyandroid.data.repositoryImpl.RepositoryImpl
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/6.
 */
class GankHomeInterator(private val page: Int) : Usecase<GankHomeResponse>() {

    override fun execute(): Observable<GankHomeResponse>
            = repository.getGankByPage(page).compose(applySchedulers())
}