package xyz.bboylin.dailyandroid.data.remote

import io.reactivex.Observable
import retrofit2.http.*
import xyz.bboylin.dailyandroid.data.entity.*

/**
 * Created by lin on 2018/2/6.
 */
interface WanAndroidService {
    /**
     * 获取首页数据
     * @param page 页数
     * @return
     */
    @GET("article/list/{page}/json")
    fun getHomeData(@Path("page") page: Int): Observable<WanHomeResponse>

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("username") userName: String, @Field("password") password: String): Observable<AccountResponse>

    /**
     * 注册
     * @param userName
     * @param password
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    fun register(@Field("username") userName: String, @Field("password") password: String, @Field("repassword") repassword: String): Observable<AccountResponse>

    /**
     * 收藏站内文章
     * @param id 文章id
     * @return 返回response中errorCode为0即为收藏成功
     */
    @POST("lg/collect/{id}/json")
    fun collect(@Path("id") id: Int): Observable<BaseResponse>

    /**
     * 取消收藏站内文章
     * @param id 文章id
     * @return 返回response中errorCode为0即为取消收藏成功
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun uncollect(@Path("id") id: Int): Observable<BaseResponse>

    /**
     * 获取banner
     */
    @GET("banner/json")
    fun getBanner(): Observable<BannerResponse>

    /**
     * 获取收藏列表
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int): Observable<CollectListResponse>

    /**
     * 收藏站外文章
     * @param title
     * @param author
     * @param link
     * @return
     */
    @POST("lg/collect/add/json")
    @FormUrlEncoded
    fun collectOutside(@Field("title") title: String
                       , @Field("author") author: String
                       , @Field("link") link: String): Observable<CollectOutsideResponse>

    /**
     * 取消站外收藏
     * todo 还有问题，待跟进
     * @param id
     * @return
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun uncollectOutside(@Path("id") id: Int
                         , @Field("originId") originId: Int = -1): Observable<BaseResponse>
}