package xyz.bboylin.dailyandroid.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by lin on 2018/2/6.
 */
class Gank {
    /**
     * _id : 56cc6d23421aa95caa707bba
     * createdAt : 2015-08-06T02:05:32.826Z
     * desc : 一个查看设备规格的库，并且可以计算哪一年被定为“高端”机
     * publishedAt : 2015-08-06T04:16:55.575Z
     * type : android
     * url : https://github.com/facebook/device-year-class
     * used : true
     * who : 有时放纵
     */
    @SerializedName("_id")
    var id: String? = null
    @SerializedName("createdAt")
    var createdAt: String? = null
    @SerializedName("desc")
    var desc: String? = null
    @SerializedName("publishedAt")
    var publishedAt: String? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("url")
    var url: String? = null
    @SerializedName("used")
    var used: Boolean = false
    @SerializedName("who")
    var who: String? = null
}