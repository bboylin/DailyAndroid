package xyz.bboylin.dailyandroid.domain.repository

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.AccountResponse
import xyz.bboylin.dailyandroid.data.entity.BaseResponse
import xyz.bboylin.dailyandroid.data.entity.GankCategoryResponse
import xyz.bboylin.dailyandroid.data.entity.WanHomeResponse

/**
 * Created by lin on 2018/2/6.
 */
interface IRepository {
    fun getGankByPage(page: Int): Observable<GankCategoryResponse>
    fun getWelfareByPage(page: Int): Observable<GankCategoryResponse>
    fun getWanByPage(page: Int): Observable<WanHomeResponse>
    fun register(userName: String, password: String): Observable<AccountResponse>
    fun login(userName: String, password: String): Observable<AccountResponse>
    fun collect(id: Int): Observable<BaseResponse>
    fun unCollect(id: Int): Observable<BaseResponse>
}