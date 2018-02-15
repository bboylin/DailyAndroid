package xyz.bboylin.dailyandroid.data.entity

/**
 *
"apkLink": "",
"author": "鸿洋",
"chapterId": 298,
"chapterName": "我的博客",
"collect": false,
"courseId": 13,
"desc": "",
"envelopePic": "",
"id": 2323,
"link": "http://www.wanandroid.com/blog/show/2038",
"niceDate": "6小时前",
"origin": "",
"projectLink": "",
"publishTime": 1517969831000,
"title": "从一道面试题开始说起 枚举、动态代理的原理",
"visible": 1,
"zan": 0
 */

data class WanHomeItem(val publishTime: Long = 0,
                       val visible: Int = 0,
                       val niceDate: String = "",
                       val projectLink: String = "",
                       val author: String = "",
                       val zan: Int = 0,
                       val origin: String = "",
                       val chapterName: String = "",
                       val link: String = "",
                       val title: String = "",
                       val apkLink: String = "",
                       val envelopePic: String = "",
                       val chapterId: Int = 0,
                       val id: Int = 0,
                       var collect: Boolean = false,
                       val courseId: Int = 0,
                       val desc: String = "")