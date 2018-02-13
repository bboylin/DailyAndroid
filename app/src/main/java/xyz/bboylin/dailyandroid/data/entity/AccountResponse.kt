package xyz.bboylin.dailyandroid.data.entity

data class AccountResponse(val data: AccountData,
                           val errorCode: Int = 0,
                           val errorMsg: String = "")