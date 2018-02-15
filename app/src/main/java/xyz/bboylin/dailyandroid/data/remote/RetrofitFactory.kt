package xyz.bboylin.dailyandroid.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.util.CookieSPUtil
import java.lang.StringBuilder

/**
 * Created by lin on 2018/2/6.
 */
object RetrofitFactory {
    val GANK_SERVICE = Retrofit.Builder()
            .baseUrl(Constants.GANK_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GankService::class.java)
    val WAN_ANDROID_RETROFIT = Retrofit.Builder()
            .baseUrl(Constants.WAN_ANDROID_BASE_URL)
            .client(getClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WanAndroidService::class.java)

    private fun getClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder().apply {
            addInterceptor {
                val request = it.request()
                val response = it.proceed(request)
                val requestUrl = request.url().toString()
                if ((requestUrl.contains(Constants.LOGIN_PATH) || requestUrl.contains(Constants.REGISTER_PATH))
                        && !response.headers(Constants.HEADER_SET_COOKIE).isEmpty()) {
                    val cookies = response.headers(Constants.HEADER_SET_COOKIE)
                    val cookie = getEncodedCookie(cookies)
                    CookieSPUtil.saveCookie(cookie)
                }
                response
            }
            addInterceptor {
                val builder = it.request().newBuilder()
                if (CookieSPUtil.hasLogin()) {
                    builder.addHeader(Constants.HEADER_COOKIE, CookieSPUtil.getCookie())
                }
                it.proceed(builder.build())
            }
        }
        return builder.build()
    }

    private fun getEncodedCookie(cookies: List<String>): String {
        val builder = StringBuilder()
        cookies.forEach {
            for (item in it.split(";")) {
                if (!builder.contains(item)) {
                    builder.append(item).append(";")
                }
            }
        }
        if (builder.endsWith(";")) {
            builder.deleteCharAt(builder.length - 1)
        }
        return builder.toString()
    }
}