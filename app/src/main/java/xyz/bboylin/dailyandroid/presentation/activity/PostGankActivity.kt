package xyz.bboylin.dailyandroid.presentation.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_post_gank.*
import xyz.bboylin.dailyandroid.R
import xyz.bboylin.dailyandroid.domain.interator.PostGankInterator
import xyz.bboylin.dailyandroid.helper.util.LogUtil
import xyz.bboylin.universialtoast.UniversalToast

/**
 * Created by lin on 2018/3/3.
 */
class PostGankActivity : BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_post_gank

    override fun initView() {
        setSupportActionBar(toolbar)
        setupToolbar(R.string.post_gank)
        commitTv.setOnClickListener { v ->
            val url = urlEditText.text.toString()
            val desc = descEditText.text.toString()
            val who = whoEditText.text.toString()
            if (TextUtils.isEmpty(url)
                    || TextUtils.isEmpty(desc)
                    || TextUtils.isEmpty(who)) {
                UniversalToast.makeText(this, "请补充完整信息"
                        , UniversalToast.LENGTH_SHORT, UniversalToast.EMPHASIZE)
                        .showWarning()
                return@setOnClickListener
            }
            PostGankInterator(url, desc, who, "Android", true).execute()
                    .subscribe({ response ->
                        if (!response.error) {
                            UniversalToast.makeText(this, "提交成功", UniversalToast.LENGTH_SHORT)
                                    .setGravity(Gravity.CENTER, 0, 0)
                                    .showSuccess()
                            finish()
                        } else {
                            UniversalToast.makeText(this, "不知道出什么问题了", UniversalToast.LENGTH_SHORT)
                                    .setGravity(Gravity.CENTER, 0, 0)
                                    .showError()
                        }
                    }, { t ->
                        LogUtil.e(TAG, "提交干货出错了", t)
                        UniversalToast.makeText(this, "出错了", UniversalToast.LENGTH_SHORT)
                                .setGravity(Gravity.CENTER, 0, 0)
                                .showError()
                    })
        }
    }

    override fun initData() {

    }

    companion object {
        private val TAG = PostGankActivity::class.java.simpleName
        fun startFrom(context: Context) {
            context.startActivity(Intent(context, PostGankActivity::class.java))
        }
    }
}