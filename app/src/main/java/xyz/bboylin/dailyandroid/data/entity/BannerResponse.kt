package xyz.bboylin.dailyandroid.data.entity

data class BannerResponse(val data: List<BannerItem>,
                          val errorCode: Int = 0,
                          val errorMsg: String = "")