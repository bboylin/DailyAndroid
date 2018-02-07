package xyz.bboylin.dailyandroid.Presentation.widget

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.home_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.GankHomeItem
import xyz.bboylin.dailyandroid.data.entity.WanHomeItem

/**
 * Created by lin on 2018/2/7.
 */
class HomeAdapter(val items: List<Any>) : RecyclerView.Adapter<HomeAdapter.VH>() {
    val TYPE_HEADER = 0
    val TYPE_NORMAL = 1
    val TYPE_FOOTER = 2
    var headerView: View? = null
    var footerView: View? = null

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindItem(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_item, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size +
            (if (headerView == null) 0 else 1) + if (footerView == null) 0 else 1

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> if (headerView == null) TYPE_NORMAL else TYPE_HEADER
        itemCount - 1 -> if (footerView == null) TYPE_NORMAL else TYPE_FOOTER
        else -> TYPE_NORMAL
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