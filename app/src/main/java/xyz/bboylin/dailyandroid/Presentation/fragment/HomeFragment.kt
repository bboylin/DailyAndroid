package xyz.bboylin.dailyandroid.Presentation.fragment

import android.support.v7.widget.LinearLayoutManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_content.*
import xyz.bboylin.dailyandroid.Presentation.widget.HomeAdapter
import xyz.bboylin.dailyandroid.Presentation.widget.SimpleItemDecoration
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.domain.interator.GankHomeInterator
import xyz.bboylin.dailyandroid.domain.interator.WanHomeInterator
import xyz.bboylin.dailyandroid.helper.util.LogUtil

/**
 * Created by lin on 2018/2/5.
 */
class HomeFragment : BaseFragment() {
    private val items = ArrayList<Any>()
    private var page = 1

    override fun initData() {
        Observable.mergeDelayError(GankHomeInterator(page).execute().map { response -> response.gankList }
                , WanHomeInterator(page).execute().map { response -> response.data!!.datas })
                .subscribe({ list ->
                    for (item in list!!) {
                        items.add(item)
                    }
                    recyclerView.adapter = HomeAdapter(items)
                }, { throwable -> LogUtil.e(TAG, "get home data error", throwable) })
    }

    override fun initView() {
        var linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        with(recyclerView) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            addItemDecoration(SimpleItemDecoration())
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_content
    }

    companion object {
        val TAG = "HomeFragment"
        fun getInstance(): HomeFragment = HomeFragment()
    }
}