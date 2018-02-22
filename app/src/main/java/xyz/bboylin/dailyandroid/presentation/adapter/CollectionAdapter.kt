package xyz.bboylin.dailyandroid.presentation.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.home_item.view.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.data.entity.CollectItem
import xyz.bboylin.dailyandroid.domain.interator.UncollectOutsideInterator
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.LoginEvent
import xyz.bboylin.dailyandroid.helper.util.AccountUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.universialtoast.UniversalToast

/**
 * Created by lin on 2018/2/22.
 */
class CollectionAdapter(context: Context?, items: ArrayList<Any>) : BaseAdapter<CollectionAdapter.VH>(context, items) {

    override fun onBindViewHolder(holder: VH?, position: Int) {
        if (items[position].equals(footerElem)) {
            return
        }
        holder?.bindItem(this, items[position] as CollectItem, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        if (viewType == TYPE_NORMAL) {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_item, parent, false)
            return CollectionAdapter.VH(view)
        }
        if (viewType == TYPE_FOOTER) {
            footerView.findViewById<View>(R.id.load_more_loading_view).visibility = View.VISIBLE
            footerView.findViewById<View>(R.id.load_more_failed_view).visibility = View.GONE
            footerView.findViewById<View>(R.id.load_more_end_view).visibility = View.GONE
        }
        return VH(footerView)
    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(adapter: CollectionAdapter, item: CollectItem, position: Int) {
            itemView.title.text = item.title
            itemView.author.text = item.author
            itemView.date.text = item.niceDate
            itemView.btn_star.setImageResource(R.drawable.collect_success)
            itemView.btn_star.setOnClickListener { v ->
                if (AccountUtil.hasLogin()) {
                    UncollectOutsideInterator(item.id).execute()
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
                FinestWebView.Builder(itemView.context).show(item.link)
            }
        }

        companion object {
            val TAG = VH::class.java.simpleName
        }
    }
}