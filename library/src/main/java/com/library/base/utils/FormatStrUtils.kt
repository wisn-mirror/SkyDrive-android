package com.library.base.utils

import java.text.SimpleDateFormat
import java.util.*

object FormatStrUtils {

    /**
     * 日期格式：HH:mm:ss
     */
    val DF_HH_MM_SS = "HH:mm:ss"
    private val minute = 60 * 1000 // 1分钟
            .toLong()
    private val hour = 60 * minute // 1小时

    private val day = 24 * hour // 1天

    private val month = 31 * day // 月

    private val year = 12 * month // 年

    /**
     * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
     *
     * @param mDate
     * @return
     */
    fun getformatA(mDate: Long): String? {
        val date = Date(mDate) ?: return null
        val diff = System.currentTimeMillis() - date.time
        var r: Long = 0
        if (diff > year) {
            r = diff / year
            return r.toString() + "年前"
        }
        if (diff > month) {
            r = diff / month
            return r.toString() + "个月前"
        }
        if (diff > day) {
            r = diff / day
            return if (r == 1L) {
                "昨天" + SimpleDateFormat(DF_HH_MM_SS).format(Date(mDate))
            } else r.toString() + "天前"
        }
        if (diff > hour) {
            r = diff / hour
            return r.toString() + "小时前"
        }
        if (diff > minute) {
            r = diff / minute
            return r.toString() + "分钟前"
        }
        return "刚刚"
    }


}