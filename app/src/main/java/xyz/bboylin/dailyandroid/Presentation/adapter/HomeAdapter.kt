package xyz.bboylin.dailyandroid.Presentation.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.home_item.view.*
import xyz.bboylin.dailyandroid.Presentation.OnLoadMoreListener
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.GankHomeItem
import xyz.bboylin.dailyandroid.data.entity.WanHomeItem

/**
 * Created by lin on 2018/2/7.
 */
class HomeAdapter(private val items: ArrayList<Any>) : RecyclerView.Adapter<HomeAdapter.VH>() {
    private val TYPE_NORMAL = 1
    private val TYPE_FOOTER = 2
    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var loading = false
    private val footElem = Any()
    private val tag = "HomeAdapter"

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindItem(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val view = LayoutInflater.from(parent?.context).inflate(
                if (viewType == TYPE_NORMAL) R.layout.home_item else R.layout.load_more_footer_view, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = when (position) {
        itemCount - 1 -> if (items[position].equals(footElem)) TYPE_FOOTER else TYPE_NORMAL
        else -> TYPE_NORMAL
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
                    startLoadMore(recyclerView?.context)
                }
            }
        })
    }

    private fun startLoadMore(context: Context?) {
        items.add(footElem)
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

    fun addData(list: List<Any>?) {
        if (items.remove(footElem)) {
            notifyItemRemoved(itemCount)
        }
        for (item in list!!) {
            items.add(item)
        }
        loading = false
        notifyItemRangeInserted(itemCount - list.size, list.size)
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: Any) {
            if (item is GankHomeItem) {
                itemView.title.text = item.desc
                itemView.date.text = item.publishedAt!!.split("T")[0]
                itemView.author.text = item.who ?: "佚名"
                itemView.btn_star.setOnClickListener { v ->
                    itemView.btn_star.setImageResource(R.drawable.collect_success)
                }
            } else if (item is WanHomeItem) {
                itemView.title.text = item.title
                itemView.date.text = item.niceDate
                itemView.author.text = item.author
                itemView.btn_star.setImageResource(
                        if (item.collect) R.drawable.collect_success else R.drawable.collect_black)
                itemView.btn_star.setOnClickListener { v ->
                    itemView.btn_star.setImageResource(R.drawable.collect_success)
                }
            }
        }
    }
}