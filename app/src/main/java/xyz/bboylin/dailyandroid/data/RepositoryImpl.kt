package xyz.bboylin.dailyandroid.data

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.*
import xyz.bboylin.dailyandroid.data.remote.RetrofitFactory
import xyz.bboylin.dailyandroid.domain.repository.IRepository

/**
 * Created by lin on 2018/2/6.
 */
class RepositoryImpl : IRepository {
    override fun getBanner(): Observable<BannerResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.getBanner()
    }

    override fun unCollect(id: Int): Observable<BaseResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.uncollect(id)
    }

    override fun collect(id: Int): Observable<BaseResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.collect(id)
    }

    override fun register(userName: String, password: String): Observable<AccountResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.register(userName, password, password)
    }

    override fun login(userName: String, password: String): Observable<AccountResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.login(userName, password)
    }

    override fun getWanByPage(page: Int): Observable<WanHomeResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.getHomeData(page)
    }

    override fun getGankByPage(page: Int): Observable<GankCategoryResponse> {
        val service = RetrofitFactory.GANK_SERVICE
        return service.getAndroidData("Android", 10, page)
    }

    override fun getWelfareByPage(page: Int): Observable<GankCategoryResponse> {
        val service = RetrofitFactory.GANK_SERVICE
        return service.getAndroidData("福利", 10, page)
    }

    companion object {
        val INSTANCE = RepositoryImpl()
        fun getInstance(): RepositoryImpl = INSTANCE
    }
}