package xyz.bboylin.dailyandroid.domain.interator

import io.reactivex.Observable
import xyz.bboylin.dailyandroid.data.entity.PostGankResponse
import xyz.bboylin.dailyandroid.domain.Usecase

/**
 * Created by lin on 2018/3/3.
 */
class PostGankInterator(private val url: String, private val desc: String
                        , private val who: String, private val type: String = "Android"
                        , private val debug: Boolean = false) : Usecase<PostGankResponse>() {
    override fun execute(): Observable<PostGankResponse> {
        return repository.postGank(url, desc, who, type, debug).compose(applySchedulers())
    }
}