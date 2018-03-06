package xyz.bboylin.dailyandroid.presentation.task

import android.os.AsyncTask
import org.jsoup.Jsoup
import xyz.bboylin.dailyandroid.helper.Constants
import xyz.bboylin.dailyandroid.helper.RxBus
import xyz.bboylin.dailyandroid.helper.rxevent.UpdateEvent
import xyz.bboylin.dailyandroid.helper.util.AppUtil
import xyz.bboylin.dailyandroid.helper.util.LogUtil

/**
 * Created by lin on 2018/3/6.
 */
class CheckUpdateTask : AsyncTask<Void, Int, Boolean>() {
    private lateinit var versionName: String
    private lateinit var url: String
    private lateinit var content: String
    override fun doInBackground(vararg params: Void?): Boolean {
        try {
            val doc = Jsoup.connect(Constants.UPDATE_RELEASE_URL).get()
            val elem = doc.getElementsByClass("release-body commit open float-left")[0]
            versionName = elem.getElementsByClass("release-title text-normal")[0]
                    .getElementsByTag("a")[0]
                    .text()
                    .replace("v", "")
                    .replace("V", "")
            elem.getElementsByClass("d-block py-2").forEach {
                val link = it.getElementsByTag("a")[0].attr("href")
                if (link.contains("apk")) {
                    url = Constants.GITHUB_HOST + link
                    return@forEach
                }
            }
            content = elem.getElementsByClass("markdown-body")[0].getElementsByTag("p")[0].text()
        } catch (e: Exception) {
            e.printStackTrace()
            AppUtil.setUpdateFlag(false)
            return false
        }
        return true
    }

    override fun onPostExecute(result: Boolean) {
        if (result) {
            LogUtil.d("====", versionName)
            LogUtil.d("====", url)
            LogUtil.d("====", content)
            val hasUpdate = AppUtil.checkUpdate(versionName)
            AppUtil.setUpdateFlag(hasUpdate)
            if (hasUpdate) {
                RxBus.get().post(UpdateEvent(versionName, url, content))
            }
        }
    }
}