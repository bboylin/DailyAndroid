package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.AccountResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/13.
 */
class RegisterInterator(private val userName: String, private val password: String) : Usecase<AccountResponse>() {
    override fun execute(): Observable<AccountResponse> {
        return repository.register(userName, password).compose(applySchedulers())
    }
}