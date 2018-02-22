package xyz.bboylin.dailyandroid.presentation.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thefinestartist.finestwebview.FinestWebView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.weekly_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.Gank
import xyz.bboylin.dailyandroid.domain.interator.CollectOutsideInterator
import xyz.bboylin.dailyandroid.domain.interator.UncollectOutsideInterator
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.CollectionReadyEvent
import xyz.bboylin.dailyandroid.helper.util.CollectionUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.universialtoast.UniversalToast

/**
 * 周报的adapter
 * Created by lin on 2018/2/9.
 */
class WeeklyAdapter(context: Context?, private val len: Int, items: ArrayList<Any>) : BaseAdapter<WeeklyAdapter.VH>(context, items) {
    protected val compositeDisposable = CompositeDisposable()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val disposable = RxBus.get()
                .toObservable(CollectionReadyEvent::class.java)
                .subscribe({ t ->
                    collections = CollectionUtil.getCollection()
                    notifyDataSetChanged()
                })
        compositeDisposable.add(disposable)
    }

    override fun onBindViewHolder(holder: VH?, position: Int) {
        if (items[position].equals(footerElem)) {
            return
        }
        context?.let {
            holder?.bindItem(context as Context, len - position, items[position] as Gank)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.weekly_item, parent, false)
            return VH(view)
        }
        if (viewType == TYPE_FOOTER) {
            footerView.findViewById<View>(R.id.load_more_loading_view).visibility = View.VISIBLE
            footerView.findViewById<View>(R.id.load_more_failed_view).visibility = View.GONE
            footerView.findViewById<View>(R.id.load_more_end_view).visibility = View.GONE
        }
        return VH(footerView)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(context: Context, id: Int, gank: Gank) {
            val title = Constants.WEEKLY_TITLE_PREFIX + "#" + id
            itemView.titleTv.text = title
            itemView.bgImage.setImageURI(Uri.parse(gank.url))
            val url = Constants.WEEKLY_BASE_URL + Constants.WEEKLY_PATH_PREFIX + id + "/"
            itemView.bgImage.setOnClickListener { v ->
                FinestWebView.Builder(context).titleDefault("Android技术周报#${id}").show(url)
                LogUtil.d(TAG, "load url:" + url)
            }
            var hasCollected = false
            var id = -1
            for (it in collections) {
                if (it.startsWith(url)) {
                    hasCollected = true
                    id = it.split("$#$")[1].toInt()
                    break
                }
            }
            itemView.collectBtn.setImageResource(
                    if (hasCollected) R.drawable.collect_success else R.drawable.collect_black)
            itemView.collectBtn.setOnClickListener { v ->
                if (hasCollected) {
                    UncollectOutsideInterator(id).execute()
                            .subscribe({ response ->
                                if (response.errorCode == 0) {
                                    UniversalToast.makeText(context, "取消收藏成功", UniversalToast.LENGTH_SHORT).showSuccess()
                                    collections.remove(url + "$#$" + id)
                                    itemView.collectBtn.setImageResource(R.drawable.collect_black)
                                    hasCollected = false
                                    id = -1
                                }
                            }, { throwable ->
                                LogUtil.e(TAG, "取消收藏失败", throwable)
                                UniversalToast.makeText(context, "取消收藏失败", UniversalToast.LENGTH_SHORT).showError()
                            })
                } else {
                    CollectOutsideInterator(title, "脉脉不得语", url).execute()
                            .subscribe({ response ->
                                collections.add(response.data.link + "$#$" + response.data.id)
                                UniversalToast.makeText(context, "收藏成功", UniversalToast.LENGTH_SHORT).showSuccess()
                                itemView.collectBtn.setImageResource(R.drawable.collect_success)
                                hasCollected = true
                                id = response.data.id
                            }, { throwable ->
                                LogUtil.e(TAG, "收藏失败", throwable)
                                UniversalToast.makeText(context, "收藏失败", UniversalToast.LENGTH_SHORT).showError()
                            })
                }
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        CollectionUtil.saveCollections(collections)
        compositeDisposable.clear()
    }

    companion object {
        private var collections = CollectionUtil.getCollection() ?: HashSet<String>()
        private val TAG = WeeklyAdapter::class.java.simpleName
    }
}