package xyz.bboylin.dailyandroid.presentation.widget

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import xyz.bboylin.dailyandroid.R

/**
 * Created by lin on 2018/3/6.
 */
class UpdateDialog(context: Context?) : Dialog(context, R.style.CustomDialog) {
    private lateinit var contentTv: TextView
    private lateinit var downloadBtn: TextView
    private lateinit var cancelBtn: TextView

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null)
        contentTv = view.findViewById(R.id.dialog_update_content)
        downloadBtn = view.findViewById(R.id.dialog_download)
        cancelBtn = view.findViewById(R.id.dialog_cancle)
        cancelBtn.setOnClickListener { v -> cancel() }
        super.setContentView(view)
    }

    fun setContent(content: String): UpdateDialog {
        contentTv.setText(content)
        return this
    }

    fun setUrl(url: String): UpdateDialog {
        downloadBtn.setOnClickListener { v ->
            downloadBySystem(context, url)
        }
        return this
    }

    /**
     * 通过浏览器下载APK包
     * @param context
     * @param url
     */
    fun downloadBySystem(context: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun setCallBack(listener: View.OnClickListener): UpdateDialog {
        downloadBtn.setOnClickListener(listener)
        return this
    }
}