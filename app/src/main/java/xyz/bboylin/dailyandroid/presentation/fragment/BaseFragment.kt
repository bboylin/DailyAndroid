package xyz.bboylin.dailyandroid.presentation.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by lin on 2018/2/5.
 */
abstract class BaseFragment : Fragment() {
    protected val compositeDisposable = CompositeDisposable()
    var contentView: View? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflater?.let {
            contentView = inflater.inflate(getLayoutId(), container, false)
        }
        return contentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentView = null
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initData()
}