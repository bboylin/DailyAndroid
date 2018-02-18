package xyz.bboylin.dailyandroid.presentation.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.weekly_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.Gank
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.presentation.activity.WebActivity

/**
 * Created by lin on 2018/2/9.
 */
class WeeklyAdapter(private val context: Context, private val len: Int, items: List<Gank>) : BaseAdapter<WeeklyAdapter.VH>(items) {

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
            itemView.titleTv.text = Constants.WEEKLY_TITLE_PREFIX + "#" + id
            itemView.bgImage.setImageURI(Uri.parse(gank.url))
            itemView.bgImage.setOnClickListener { v ->
                val url = Constants.WEEKLY_BASE_URL + Constants.WEEKLY_PATH_PREFIX + id + "/"
                //todo load url in webview
                WebActivity.start(context, url)
                LogUtil.d("weeklyAdapter", "load url:" + url)
            }
            itemView.collectBtn.setOnClickListener { v ->
                LogUtil.d("weeklyAdapter", "collect btn clicked!!!")
                itemView.collectBtn.setImageResource(R.drawable.collect_success)
            }
        }
    }
}