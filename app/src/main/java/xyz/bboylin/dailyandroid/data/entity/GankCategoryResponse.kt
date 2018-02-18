package xyz.bboylin.dailyandroid.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by lin on 2018/2/6.
 */
data class GankCategoryResponse(var error: Boolean = false,
                                @SerializedName("results")
                                var gankList: List<Gank>)