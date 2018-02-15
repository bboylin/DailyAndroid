package xyz.bboylin.dailyandroid.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by lin on 2018/2/6.
 */
class GankCategoryResponse {
    var error: Boolean? = null
    @SerializedName("results")
    var gankList: List<Gank>? = null
}