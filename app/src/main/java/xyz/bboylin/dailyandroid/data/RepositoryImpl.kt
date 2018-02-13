package xyz.bboylin.dailyandroid.data

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.AccountResponse
import xyz.bboylin.dailyandroid.data.entity.GankHomeResponse
import xyz.bboylin.dailyandroid.data.entity.WanHomeResponse
import xyz.bboylin.dailyandroid.data.local.CookieSPUtil
import xyz.bboylin.dailyandroid.data.remote.RetrofitFactory
import xyz.bboylin.dailyandroid.domain.repository.IRepository

/**
 * Created by lin on 2018/2/6.
 */
class RepositoryImpl : IRepository {
    override fun register(userName: String, password: String): Observable<AccountResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.register(userName, password, password)
    }

    override fun login(userName: String, password: String): Observable<AccountResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.login(userName, password)
    }

    override fun hasLogin(): Boolean = CookieSPUtil.hasLogin()

    override fun saveCookie(cookie: String) {
        CookieSPUtil.saveCookie(cookie)
    }

    override fun getCookie(): String = CookieSPUtil.getCookie()

    override fun getWanByPage(page: Int): Observable<WanHomeResponse> {
        val service = RetrofitFactory.WAN_ANDROID_RETROFIT
        return service.getHomeData(page)
    }

    override fun getGankByPage(page: Int): Observable<GankHomeResponse> {
        val service = RetrofitFactory.GANK_SERVICE
        return service.getAndroidData(10, page)
    }

    companion object {
        fun getInstance(): RepositoryImpl = RepositoryImpl()
    }
}