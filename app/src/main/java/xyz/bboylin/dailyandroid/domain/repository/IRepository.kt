package xyz.bboylin.dailyandroid.domain.repository

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.*

/**
 * Created by lin on 2018/2/6.
 */
interface IRepository {
    fun postGank(url: String, desc: String, who: String, type: String, debug: Boolean): Observable<PostGankResponse>
    fun getGankByPage(page: Int): Observable<GankCategoryResponse>
    fun getWelfareByPage(page: Int): Observable<GankCategoryResponse>
    fun getWanByPage(page: Int): Observable<WanHomeResponse>
    fun register(userName: String, password: String): Observable<AccountResponse>
    fun login(userName: String, password: String): Observable<AccountResponse>
    fun collect(id: Int): Observable<BaseResponse>
    fun unCollect(id: Int): Observable<BaseResponse>
    fun getBanner(): Observable<BannerResponse>
    fun getCollectList(page: Int): Observable<CollectListResponse>
    fun collectOutside(title: String, author: String, link: String): Observable<CollectOutsideResponse>
    fun uncollectOutside(id: Int, originId: Int): Observable<BaseResponse>
}