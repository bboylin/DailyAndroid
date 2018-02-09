package xyz.bboylin.dailyandroid.Presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.weekly_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.helper.Constants

/**
 * Created by lin on 2018/2/9.
 */
class WeeklyAdapter(private val items: ArrayList<Int>) : RecyclerView.Adapter<WeeklyAdapter.VH>() {
    override fun onBindViewHolder(holder: VH?, position: Int) {
        holder?.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.weekly_item, parent, false)
        return VH(view)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(id: Int) {
            itemView.title.text = Constants.WEEKLY_TITLE_PREFIX + "#" + id
            itemView.title.setOnClickListener { v ->
                val url = Constants.WEEKLY_BASE_URL + Constants.WEEKLY_PATH_PREFIX + id + "/"
                //todo load url in webview
            }
            itemView.setOnClickListener { v ->
                //todo 收藏
            }
        }
    }
}