package xyz.bboylin.dailyandroid.helper.retrofit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import xyz.bboylin.dailyandroid.data.entity.WanHomeResponse

/**
 * Created by lin on 2018/2/6.
 */
interface WanAndroidService {
    @GET("article/list/{page}/json")
    fun getHomeData(@Path("page") page: Int): Observable<WanHomeResponse>
}