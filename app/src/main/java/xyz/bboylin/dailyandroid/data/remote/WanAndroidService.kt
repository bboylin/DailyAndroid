package xyz.bboylin.dailyandroid.data.remote

import io.reactivex.Observable
import retrofit2.http.*
import xyz.bboylin.dailyandroid.data.entity.AccountResponse
import xyz.bboylin.dailyandroid.data.entity.WanHomeResponse

/**
 * Created by lin on 2018/2/6.
 */
interface WanAndroidService {
    @GET("article/list/{page}/json")
    fun getHomeData(@Path("page") page: Int): Observable<WanHomeResponse>

    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("username") userName: String, @Field("password") password: String): Observable<AccountResponse>

    @POST("user/register")
    @FormUrlEncoded
    fun register(@Field("username") userName: String, @Field("password") password: String, @Field("repassword") repassword: String): Observable<AccountResponse>
}