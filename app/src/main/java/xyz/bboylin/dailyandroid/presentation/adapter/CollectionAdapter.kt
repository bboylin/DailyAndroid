package xyz.bboylin.dailyandroid.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.home_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.CollectItem
import xyz.bboylin.dailyandroid.domain.interator.UncollectInterator
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.LoginEvent
import xyz.bboylin.dailyandroid.helper.util.AccountUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.dailyandroid.helper.util.WebUtil
import xyz.bboylin.universialtoast.UniversalToast

/**
 * Created by lin on 2018/2/22.
 */
class CollectionAdapter(private val items: ArrayList<Any>) : RecyclerView.Adapter<CollectionAdapter.VH>() {
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH?, position: Int) {
        holder?.bindItem(this, items[position] as CollectItem, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_item, parent, false)
        return CollectionAdapter.VH(view)
    }

    fun removeItemAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(adapter: CollectionAdapter, item: CollectItem, position: Int) {
            itemView.title.text = item.title
            itemView.author.text = item.author
            itemView.date.text = item.niceDate
            itemView.btn_star.setImageResource(R.drawable.collect_success)
            itemView.btn_star.setOnClickListener { v ->
                if (AccountUtil.hasLogin()) {
//                    UncollectOutsideInterator(item.id).execute()
                    UncollectInterator(item.id).execute()
                            .subscribe({ response ->
                                if (response.errorCode == 0) {
                                    adapter.removeItemAt(position)
                                } else {
                                    UniversalToast.makeText(itemView.context, "取消收藏失败"
                                            , UniversalToast.LENGTH_SHORT)
                                            .setGravity(Gravity.CENTER, 0, 0)
                                            .showError()
                                }
                            }, { t ->
                                UniversalToast.makeText(itemView.context, "取消收藏失败"
                                        , UniversalToast.LENGTH_SHORT)
                                        .setGravity(Gravity.CENTER, 0, 0)
                                        .showError()
                                LogUtil.e(TAG, "取消收藏失败", t)
                            })
                } else {
                    RxBus.get().post(LoginEvent())
                }
            }
            itemView.title.setOnClickListener { v ->
                WebUtil.show(item.link)
            }
        }

        companion object {
            val TAG = VH::class.java.simpleName
        }
    }
}