package xyz.bboylin.dailyandroid.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import xyz.bboylin.dailyandroid.data.entity.GankHomeResponse

/**
 * Created by lin on 2018/2/6.
 */
interface GankService {
    @GET("data/Android/{num}/{page}")
    fun getAndroidData(@Path("num") num: Int, @Path("page") page: Int): Observable<GankHomeResponse>
}