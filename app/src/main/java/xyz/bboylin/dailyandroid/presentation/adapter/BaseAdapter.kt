package xyz.bboylin.dailyandroid.presentation.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.presentation.OnLoadMoreListener

/**
 * 加载更多的基础adapter
 * Created by lin on 2018/2/15.
 */
abstract class BaseAdapter<T : RecyclerView.ViewHolder?>(protected var items: List<Any>) : RecyclerView.Adapter<T>() {
    protected val TYPE_NORMAL = 1
    protected val TYPE_FOOTER = 2
    var onLoadMoreListener: OnLoadMoreListener? = null
    protected var loading = false
    protected val footerElem = Any()
    protected var footerView: View? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        onLoadMoreListener ?: return
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!loading && getLastVisibleItemPosition(recyclerView?.layoutManager) + 1 >= itemCount) {
                    startLoadMore()
                }
            }
        })
    }

    private fun startLoadMore() {
        LogUtil.d("BaseAdapter", "start loadmore")
        items = items.plus(footerElem)
        notifyItemInserted(itemCount - 1)
        loading = true
        onLoadMoreListener?.loadMore()
    }

    private fun getLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int {
        if (layoutManager is LinearLayoutManager)
            return layoutManager.findLastVisibleItemPosition()
        else
            return RecyclerView.NO_POSITION
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (position) {
        itemCount - 1 -> if (items[position].equals(footerElem)) TYPE_FOOTER else TYPE_NORMAL
        else -> TYPE_NORMAL
    }

    fun addData(list: List<Any>) {
        if (items.contains(footerElem)) {
            items = items.minus(footerElem)
            notifyItemRemoved(itemCount)
        }
        items = items.plus(list.asIterable())
        loading = false
        notifyItemRangeInserted(itemCount - list.size, list.size)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        onLoadMoreListener = null
        footerView = null
    }
}