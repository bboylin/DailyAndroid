package xyz.bboylin.dailyandroid.helper.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.bboylin.dailyandroid.helper.Constants

/**
 * Created by lin on 2018/2/6.
 */
class RetrofitFactory {
    companion object {
        val GANK_SERVICE = Retrofit.Builder()
                .baseUrl(Constants.GANK_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GankService::class.java)
        val WAN_ANDROID_RETROFIT = Retrofit.Builder()
                .baseUrl(Constants.WAN_ANDROID_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WanAndroidService::class.java)
    }
}