package xyz.bboylin.dailyandroid.helper.util

import android.content.Context
import android.content.SharedPreferences
import xyz.bboylin.dailyandroid.helper.Constants

/**
 * Created by lin on 2018/2/19.
 */
object CollectionUtil {
    private lateinit var context: Context
    private lateinit var sp: SharedPreferences

    fun initContext(context: Context) {
        this.context = context
        sp = context.getSharedPreferences(Constants.SP_COLLECTION_KEY, Context.MODE_PRIVATE)
    }

    fun isEmpty() = getCollection() == null

    fun saveCollections(set: Set<String>) {
        sp.edit().putStringSet(Constants.SP_COLLECTION_KEY, set).apply()
    }

    fun saveSingleCollection(url: String) {
        val set = getCollection() ?: HashSet<String>()
        set.add(url)
        sp.edit().putStringSet(Constants.SP_COLLECTION_KEY, set).apply()
    }

    fun removeSingleCollection(url: String) {
        val set = getCollection() ?: return
        if (set.remove(url)) {
            sp.edit().putStringSet(Constants.SP_COLLECTION_KEY, set).apply()
        }
    }

    fun getCollection() = sp.getStringSet(Constants.SP_COLLECTION_KEY, null)
}