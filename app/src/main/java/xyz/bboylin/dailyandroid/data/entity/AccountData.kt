package xyz.bboylin.dailyandroid.data.entity

data class AccountData(val password: String = "",
                       val icon: String = "",
                       val collectIds: List<Integer>?,
                       val id: Int = 0,
                       val type: Int = 0,
                       val username: String = "")