package xyz.bboylin.dailyandroid.presentation.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.presentation.OnLoadMoreListener

/**
 * 加载更多的基础adapter
 * Created by lin on 2018/2/15.
 */
abstract class BaseAdapter<T : RecyclerView.ViewHolder?>(protected var context: Context?, protected var items: ArrayList<Any>) : RecyclerView.Adapter<T>() {
    protected val TYPE_NORMAL = 1
    protected val TYPE_FOOTER = 2
    var onLoadMoreListener: OnLoadMoreListener? = null
    protected var loading = false
    protected val footerElem = Any()
    protected val footerView: View

    init {
        footerView = LayoutInflater.from(context)
                .inflate(R.layout.load_more_footer_view, null, false)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        footerView.layoutParams = layoutParams
    }

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
        items.add(footerElem)
        notifyItemInserted(itemCount - 1)
        loading = true
        onLoadMoreListener?.loadMore()
    }

    private fun getLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager?): Int {
        if (layoutManager is LinearLayoutManager) {
            LogUtil.d("====", layoutManager.findLastVisibleItemPosition().toString())
            return layoutManager.findLastVisibleItemPosition()
        } else {
            return RecyclerView.NO_POSITION
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (position) {
        itemCount - 1 -> if (items[position].equals(footerElem)) TYPE_FOOTER else TYPE_NORMAL
        else -> TYPE_NORMAL
    }


    fun removeItemAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addData(list: List<Any>) {
        LogUtil.d("BaseAdapter", "add data")
        loading = false
        if (items.remove(footerElem)) {
            notifyItemRemoved(itemCount)
        }
        items.addAll(list)
        notifyItemRangeInserted(itemCount - list.size, list.size)
    }

    fun showError() {
        val loadMoreFailedView = footerView.findViewById<View>(R.id.load_more_failed_view)
        val loadMoreEndView = footerView.findViewById<View>(R.id.load_more_end_view)
        loadMoreFailedView.visibility = View.VISIBLE
        loadMoreEndView.visibility = View.GONE
        footerView.findViewById<View>(R.id.load_more_loading_view).visibility = View.GONE
        loadMoreFailedView.setOnClickListener { v -> onLoadMoreListener?.loadMore() }
    }

    fun showEnd() {
        val loadMoreFailedView = footerView.findViewById<View>(R.id.load_more_failed_view)
        val loadMoreEndView = footerView.findViewById<View>(R.id.load_more_end_view)
        loadMoreFailedView.visibility = View.GONE
        loadMoreEndView.visibility = View.VISIBLE
        LogUtil.d("showEnd", footerView.findViewById<View>(R.id.load_more_loading_view).width.toString())
        footerView.findViewById<View>(R.id.load_more_loading_view).visibility = View.GONE
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        onLoadMoreListener = null
        context = null
        items.clear()
    }
}