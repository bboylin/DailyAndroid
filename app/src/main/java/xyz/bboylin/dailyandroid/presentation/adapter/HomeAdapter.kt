package xyz.bboylin.dailyandroid.presentation.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thefinestartist.finestwebview.FinestWebView
import ezy.ui.view.BannerView
import kotlinx.android.synthetic.main.home_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.BannerItem
import xyz.bboylin.dailyandroid.data.entity.BaseResponse
import xyz.bboylin.dailyandroid.data.entity.Gank
import xyz.bboylin.dailyandroid.data.entity.WanHomeItem
import xyz.bboylin.dailyandroid.domain.Usecase
import xyz.bboylin.dailyandroid.domain.interator.CollectInterator
import xyz.bboylin.dailyandroid.domain.interator.UncollectInterator
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.ShowLoginWindowEvent
import xyz.bboylin.dailyandroid.helper.util.CookieSPUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.universialtoast.UniversalToast

/**
 * 首页的adapter，负责加载更多
 * Created by lin on 2018/2/7.
 */
class HomeAdapter(private val context: Context, items: ArrayList<Any>) : BaseAdapter<HomeAdapter.VH>(items) {
    private lateinit var headerView: BannerView<BannerItem>
    private val headerElem = Any()
    private val TYPE_HEADER = 3

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindItem(context, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        if (viewType == TYPE_HEADER) {
            return VH(headerView)
        }
        val view = LayoutInflater.from(parent?.context).inflate(
                if (viewType == TYPE_NORMAL) R.layout.home_item else R.layout.load_more_footer_view, parent, false)
        if (viewType == TYPE_FOOTER) {
            view.findViewById<View>(R.id.load_more_loading_view).visibility = View.VISIBLE
            view.findViewById<View>(R.id.load_more_failed_view).visibility = View.GONE
            footerView = view
        }
        return VH(view)
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> if (items[0].equals(headerElem)) TYPE_HEADER else TYPE_NORMAL
        else -> super.getItemViewType(position)
    }

    fun addHeader(factory: BannerView.ViewFactory<BannerItem>, list: List<BannerItem>) {
        headerView = LayoutInflater.from(context).inflate(R.layout.banner_header, null, false) as BannerView<BannerItem>
        headerView.setViewFactory(factory)
        headerView.setDataList(list)
        items.add(0, headerElem)
        notifyItemInserted(0)
        headerView.start()
    }

    fun refreshData(list: ArrayList<Any>) {
        items.clear()
        items.add(headerElem)
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun showError() {
        val loadMoreFailedView = footerView.findViewById<View>(R.id.load_more_failed_view)
        loadMoreFailedView.visibility = View.VISIBLE
        loadMoreFailedView.setOnClickListener { v -> onLoadMoreListener?.loadMore() }
        footerView.findViewById<View>(R.id.load_more_loading_view).visibility = View.GONE
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(context: Context, item: Any) {
            if (item is Gank) {
                itemView.title.text = item.desc
                itemView.date.text = item.publishedAt.split("T")[0]
                itemView.author.text = item.who
                itemView.btn_star.setOnClickListener { v ->
                    itemView.btn_star.setImageResource(R.drawable.collect_success)
                }
                itemView.title.setOnClickListener {
                    FinestWebView.Builder(context).show(item.url)
                }
            } else if (item is WanHomeItem) {
                itemView.title.text = item.title
                itemView.date.text = item.niceDate
                itemView.author.text = item.author
                itemView.btn_star.setImageResource(
                        if (item.collect) R.drawable.collect_success else R.drawable.collect_black)
                itemView.title.setOnClickListener {
                    FinestWebView.Builder(context).show(item.link)
                }
                itemView.btn_star.setOnClickListener { v ->
                    if (CookieSPUtil.hasLogin()) {
                        val useCase: Usecase<BaseResponse> = if (item.collect) UncollectInterator(item.id) else CollectInterator(item.id)
                        val errorMsg = if (item.collect) "取消收藏失败" else "收藏失败"
                        val successMsg = if (item.collect) "取消收藏成功" else "收藏成功"
                        useCase.execute()
                                .subscribe({ response ->
                                    if (response.errorCode == 0) {
                                        item.collect = !item.collect
                                        itemView.btn_star.setImageResource(
                                                if (item.collect) R.drawable.collect_success else R.drawable.collect_black)
                                        UniversalToast.makeText(itemView.context, successMsg
                                                , UniversalToast.LENGTH_SHORT, UniversalToast.CLICKABLE)
                                                .setClickCallBack("查看", { v ->
                                                    //todo 跳转我的收藏
                                                })
                                                .showSuccess()
                                    } else {
                                        UniversalToast.makeText(itemView.context, errorMsg
                                                , UniversalToast.LENGTH_SHORT).showError()
                                    }
                                }, { t -> LogUtil.e("HomeAdapter", "failed in collect or uncollect", t) })
                    } else {
                        RxBus.get().post(ShowLoginWindowEvent())
                    }
                }
            }
        }
    }
}