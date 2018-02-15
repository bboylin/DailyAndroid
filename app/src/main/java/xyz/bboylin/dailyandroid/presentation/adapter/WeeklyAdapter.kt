package xyz.bboylin.dailyandroid.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.weekly_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.Gank
import xyz.bboylin.dailyandroid.helper.Constants

/**
 * Created by lin on 2018/2/9.
 */
class WeeklyAdapter(private val len: Int, items: List<Gank>) : BaseAdapter<WeeklyAdapter.VH>(items) {

    override fun onBindViewHolder(holder: VH?, position: Int) {
        if (items[position].equals(footerElem)) {
            return
        }
        holder?.bindItem(len - position, items[position] as Gank)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val view = LayoutInflater.from(parent?.context).inflate(
                if (viewType == TYPE_NORMAL) R.layout.weekly_item else R.layout.load_more_footer_view, parent, false)
        if (viewType == TYPE_FOOTER) {
            view.findViewById<View>(R.id.load_more_loading_view).visibility = View.VISIBLE
            view.findViewById<View>(R.id.load_more_failed_view).visibility = View.GONE
            footerView = view
        }
        return VH(view)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(id: Int, gank: Gank) {
            itemView.titleTv.text = Constants.WEEKLY_TITLE_PREFIX + "#" + id
//            itemView.bgImage.setImageURI(Uri.parse(gank.url))
            itemView.bgImage.setOnClickListener { v ->
                val url = Constants.WEEKLY_BASE_URL + Constants.WEEKLY_PATH_PREFIX + id + "/"
                //todo load url in webview
            }
            itemView.collectBtn.setOnClickListener { v ->
                //todo 收藏
            }
        }
    }
}