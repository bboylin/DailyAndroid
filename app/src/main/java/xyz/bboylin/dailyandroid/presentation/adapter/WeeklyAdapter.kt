package xyz.bboylin.dailyandroid.presentation.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.weekly_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.Gank
import xyz.bboylin.dailyandroid.domain.interator.CollectOutsideInterator
import xyz.bboylin.dailyandroid.domain.interator.UncollectInterator
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.util.CollectionUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.universialtoast.UniversalToast

/**
 * 周报的adapter
 * Created by lin on 2018/2/9.
 */
class WeeklyAdapter(private val context: Context, private val len: Int, items: ArrayList<Any>) : BaseAdapter<WeeklyAdapter.VH>(items) {

    override fun onBindViewHolder(holder: VH?, position: Int) {
        if (items[position].equals(footerElem)) {
            return
        }
        holder?.bindItem(context, len - position, items[position] as Gank)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val view = LayoutInflater.from(parent?.context).inflate(
                if (viewType == TYPE_NORMAL) R.layout.weekly_item else R.layout.load_more_footer_view, parent, false)
        if (viewType == TYPE_FOOTER) {
            view.findViewById<View>(R.id.load_more_loading_view).visibility = View.VISIBLE
            view.findViewById<View>(R.id.load_more_failed_view).visibility = View.GONE
            view.findViewById<View>(R.id.load_more_end_view).visibility = View.GONE
            footerView = view
        }
        return VH(view)
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
        footerView.findViewById<View>(R.id.load_more_loading_view).visibility = View.GONE
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
                    UncollectInterator(id).execute()
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

    companion object {
        private val collections = CollectionUtil.getCollection()
        private val TAG = WeeklyAdapter::class.java.simpleName
    }
}