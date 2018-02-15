package xyz.bboylin.dailyandroid.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import xyz.bboylin.dailyandroid.data.entity.GankCategoryResponse

/**
 * Created by lin on 2018/2/6.
 */
interface GankService {
    /**
     * 获取分类的数据，主要是用Android和福利这两个分类
     * @param category 分类
     * @param num 数量
     * @param page 第几页
     * @return
     */
    @GET("data/{category}/{num}/{page}")
    fun getAndroidData(@Path("category") category: String, @Path("num") num: Int, @Path("page") page: Int): Observable<GankCategoryResponse>
}