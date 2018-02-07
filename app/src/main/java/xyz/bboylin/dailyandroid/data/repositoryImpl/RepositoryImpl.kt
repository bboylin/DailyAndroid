package xyz.bboylin.dailyandroid.data.repositoryImpl

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.GankHomeResponse
import xyz.bboylin.dailyandroid.data.entity.WanHomeResponse
import xyz.bboylin.dailyandroid.domain.repository.IRepository
import xyz.bboylin.dailyandroid.helper.retrofit.RetrofitFactory

/**
 * Created by lin on 2018/2/6.
 */
class RepositoryImpl : IRepository {
    override fun getWanByPage(page: Int): Observable<WanHomeResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.getHomeData(page)
    }

    override fun getGankByPage(page: Int): Observable<GankHomeResponse> {
        val service = RetrofitFactory.GANK_SERVICE
        return service.getAndroidData(20, page)
    }

    companion object {
        fun getInstance(): RepositoryImpl = RepositoryImpl()
    }
}