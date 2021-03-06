package xyz.bboylin.dailyandroid.presentation.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thefinestartist.finestwebview.FinestWebView
import ezy.ui.view.BannerView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.home_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.BannerItem
import xyz.bboylin.dailyandroid.data.entity.BaseResponse
import xyz.bboylin.dailyandroid.data.entity.Gank
import xyz.bboylin.dailyandroid.data.entity.WanHomeItem
import xyz.bboylin.dailyandroid.domain.Usecase
import xyz.bboylin.dailyandroid.domain.interator.CollectInterator
import xyz.bboylin.dailyandroid.domain.interator.CollectOutsideInterator
import xyz.bboylin.dailyandroid.domain.interator.UncollectInterator
import xyz.bboylin.dailyandroid.domain.interator.UncollectOutsideInterator
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.CollectionReadyEvent
import xyz.bboylin.dailyandroid.helper.rxevent.HomeItemUncollectedEvent
import xyz.bboylin.dailyandroid.helper.rxevent.LoginEvent
import xyz.bboylin.dailyandroid.helper.util.AccountUtil
import xyz.bboylin.dailyandroid.helper.util.CollectionUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.helper.util.WebUtil
import xyz.bboylin.dailyandroid.presentation.activity.CollectListActivity
import xyz.bboylin.universialtoast.UniversalToast

/**
 * 首页的adapter,header是轮播图
 * Created by lin on 2018/2/7.
 */
class HomeAdapter(context: Context?, items: ArrayList<Any>) : BaseAdapter<HomeAdapter.VH>(context, items) {
    private lateinit var headerView: BannerView<BannerItem>
    private val headerElem = Any()
    private val TYPE_HEADER = 3
    protected val compositeDisposable = CompositeDisposable()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val disposable = arrayOf(RxBus.get()
                .toObservable(CollectionReadyEvent::class.java)
                .subscribe({ t ->
                    collections = CollectionUtil.getCollection()
                    notifyDataSetChanged()
                }, { t -> LogUtil.e(TAG, "error", t) })
                , RxBus.get()
                .toObservable(HomeItemUncollectedEvent::class.java)
                .subscribe({ event ->
                    val originId = event.item.originId
                    if (originId > 0) {
                        items.forEach {
                            if (it is WanHomeItem) {
                                if (it.link.equals(event.item.link)) {
                                    it.collect = false
                                }
                            }
                        }
                    } else {
                        items.forEach {
                            if (it is Gank) {
                                if (it.url.equals(event.item.link)) {
                                    collections.remove("${event.item.link}$#$${event.item.id}")
                                }
                            }
                        }
                    }
                    notifyDataSetChanged()
                }, { t -> LogUtil.e(TAG, "error", t) }))
        compositeDisposable.add(disposable[0])
        compositeDisposable.add(disposable[1])
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        context?.let {
            holder.bindItem(context as Context, items[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        if (viewType == TYPE_HEADER) {
            return VH(headerView)
        }
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_item, parent, false)
            return VH(view)
        }
        if (viewType == TYPE_FOOTER) {
            footerView.findViewById<View>(R.id.load_more_loading_view).visibility = View.VISIBLE
            footerView.findViewById<View>(R.id.load_more_failed_view).visibility = View.GONE
            footerView.findViewById<View>(R.id.load_more_end_view).visibility = View.GONE
        }
        return VH(footerView)
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

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        compositeDisposable.clear()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(context: Context, item: Any) {
            if (item is Gank) {
                itemView.title.text = item.desc
                itemView.date.text = item.publishedAt.split("T")[0]
                itemView.author.text = if (TextUtils.isEmpty(item.who)) "干货营" else item.who
                var hasCollected = false
                var id = -1
                for (it in collections) {
                    if (it.startsWith(item.url)) {
                        hasCollected = true
                        id = it.split("$#$")[1].toInt()
                        break
                    }
                }
                itemView.btn_star.setImageResource(
                        if (hasCollected) R.drawable.collect_success else R.drawable.collect_black)
                itemView.btn_star.setOnClickListener { v ->
                    if (hasCollected) {
                        UncollectOutsideInterator(id).execute()
                                .subscribe({ response ->
                                    if (response.errorCode == 0) {
                                        UniversalToast.makeText(context, "取消收藏成功", UniversalToast.LENGTH_SHORT)
                                                .setGravity(Gravity.CENTER, 0, 0)
                                                .showSuccess()
                                        collections.remove(item.url + "$#$" + id)
                                        itemView.btn_star.setImageResource(R.drawable.collect_black)
                                        hasCollected = false
                                        id = -1
                                    }
                                }, { throwable ->
                                    LogUtil.e(TAG, "取消收藏失败", throwable)
                                    UniversalToast.makeText(context, "取消收藏失败", UniversalToast.LENGTH_SHORT)
                                            .setGravity(Gravity.CENTER, 0, 0)
                                            .showError()
                                })
                    } else {
                        CollectOutsideInterator(item.desc, itemView.author.text.toString(), item.url).execute()
                                .subscribe({ response ->
                                    collections.add(response.data.link + "$#$" + response.data.id)
                                    UniversalToast.makeText(context, "收藏成功", UniversalToast.LENGTH_SHORT, UniversalToast.CLICKABLE)
                                            .setClickCallBack("查看", { v ->
                                                CollectListActivity.startFrom(context)
                                            })
                                            .showSuccess()
                                    itemView.btn_star.setImageResource(R.drawable.collect_success)
                                    hasCollected = true
                                    id = response.data.id
                                }, { throwable ->
                                    LogUtil.e(TAG, "收藏失败", throwable)
                                    UniversalToast.makeText(context, "收藏失败", UniversalToast.LENGTH_SHORT)
                                            .setGravity(Gravity.CENTER, 0, 0)
                                            .showError()
                                })
                    }
                }
                itemView.title.setOnClickListener {
                    WebUtil.show(item.url)
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
                    if (AccountUtil.hasLogin()) {
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
                                                    CollectListActivity.startFrom(context)
                                                })
                                                .showSuccess()
                                    } else {
                                        UniversalToast.makeText(itemView.context, errorMsg
                                                , UniversalToast.LENGTH_SHORT).showError()
                                    }
                                }, { t -> LogUtil.e(TAG, "failed in collect or uncollect", t) })
                    } else {
                        RxBus.get().post(LoginEvent())
                    }
                }
            }
        }
    }

    companion object {
        private var collections = CollectionUtil.getCollection() ?: HashSet<String>()
        private val TAG = HomeAdapter::class.java.simpleName
    }
}