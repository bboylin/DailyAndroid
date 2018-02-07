package xyz.bboylin.dailyandroid.domain.repository

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.GankHomeResponse
import xyz.bboylin.dailyandroid.data.entity.WanHomeResponse

/**
 * Created by lin on 2018/2/6.
 */
interface IRepository {
    fun getGankByPage(page: Int): Observable<GankHomeResponse>
    fun getWanByPage(page: Int): Observable<WanHomeResponse>
}