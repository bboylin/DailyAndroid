package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.BannerResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/2/18.
 */
class GetBannerInterator : Usecase<BannerResponse>() {
    override fun execute(): Observable<BannerResponse> {
        return repository.getBanner().compose(applySchedulers())
    }
}