package com.wisn.qm.mode.file

import android.provider.MediaStore
import com.blankj.utilcode.util.LogUtils
import com.library.base.BaseApp
import com.library.base.utils.SHAMD5Utils
import com.wisn.qm.mode.db.beans.MediaInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class MediaInfoScanHelper {


    suspend fun getMediaImageList(maxid: String): MutableList<MediaInfo> {
        return withContext(Dispatchers.IO) {
            val arrayOf = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.ORIENTATION,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.LATITUDE,
                    MediaStore.Images.Media.LONGITUDE)
            val query = BaseApp.app.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf,
                    MediaStore.Images.Media._ID + ">=?",
                    arrayOf(maxid),
                    null
//                MediaStore.Images.Media.DATE_ADDED + " desc"
            )
            var result = ArrayList<MediaInfo>()
//        var sha1list = ArrayList<String>()
            query?.let {
                try {
                    query.moveToFirst()
                    val filePathIndex = query.getColumnIndex(MediaStore.Images.Media.DATA)
                    val fileNameIndex1 = query.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val createTimeIndex2 = query.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    val idIndex3 = query.getColumnIndex(MediaStore.Images.Media._ID)
                    val fileSizeIndex4 = query.getColumnIndex(MediaStore.Images.Media.SIZE)
                    val latitudeIndex5 = query.getColumnIndex(MediaStore.Images.Media.LATITUDE)
                    val longitudeIndex6 = query.getColumnIndex(MediaStore.Images.Media.LONGITUDE)
                    val widthIndex7 = query.getColumnIndex(MediaStore.Images.Media.WIDTH)
                    val heightIndex8 = query.getColumnIndex(MediaStore.Images.Media.HEIGHT)
                    val mimeTypeIndex = query.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)

                    while (query.moveToNext()) {
                        // 获取图片的路径

                        val filePath: String = query.getString(filePathIndex)
                        //获取图片名称
                        val fileName: String = query.getString(fileNameIndex1)
                        //获取图片时间
                        val createTime: Long = query.getLong(createTimeIndex2)
                        val id: Long = query.getLong(idIndex3)
                        val fileSize: Long = query.getLong(fileSizeIndex4)
                        val latitude: Float = query.getFloat(latitudeIndex5)
                        val longitude: Float = query.getFloat(longitudeIndex6)
                        val width: Int = query.getInt(widthIndex7)
                        val height: Int = query.getInt(heightIndex8)
                        //获取图片类型
                        val mimeType: String = query.getString(
                                mimeTypeIndex)
                        if (filePath.isNullOrEmpty()) {
                            continue
                        }
                        val shA1 = filePath?.let {
                            SHAMD5Utils.getSHA1(filePath)
                        }

                        if ("downloading" != getExtensionName(filePath)) { //过滤未下载完成的文件
//                            LogUtils.d("width:", width, " height:", height)
                            val element = MediaInfo(id, fileName, filePath, fileSize, -1, mimeType, false, createTime, null, latitude, longitude, width, height)
                            element.sha1 = shA1
//                            LogUtils.d(element.toString())
                            result.add(element)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    query.close()
                }
                LogUtils.d("result.size:", result.size)
            }
            result
        }
    }

    suspend fun getMediaVideoList(maxid: String): MutableList<MediaInfo> {
        return withContext(Dispatchers.IO) {
            val arrayOf = arrayOf(
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.WIDTH,
                    MediaStore.Video.Media.HEIGHT,
//                            MediaStore.Video.Media.ORIENTATION,
                    MediaStore.Video.Media.DISPLAY_NAME,
                    MediaStore.Video.Media.DATE_ADDED,
                    MediaStore.Video.Media.SIZE,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Video.Media.MIME_TYPE,
                    MediaStore.Video.Media.LATITUDE,
                    MediaStore.Video.Media.LONGITUDE)
            val query = BaseApp.app.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    arrayOf,
                    MediaStore.Video.Media._ID + ">? and( "
                            + MediaStore.Video.Media.MIME_TYPE + "=? or "
                            + MediaStore.Video.Media.MIME_TYPE + "=? or "
                            + MediaStore.Video.Media.MIME_TYPE + "=? or "
                            + MediaStore.Video.Media.MIME_TYPE + "=? or "
                            + MediaStore.Video.Media.MIME_TYPE + "=? or "
                            + MediaStore.Video.Media.MIME_TYPE + "=? or "
                            + MediaStore.Video.Media.MIME_TYPE + "=? or "
                            + MediaStore.Video.Media.MIME_TYPE + "=? or "
                            + MediaStore.Video.Media.MIME_TYPE + "=?) ",

                    arrayOf(maxid, "video/mp4", "video/3gp", "video/aiv", "video/rmvb", "video/vob", "video/flv",
                            "video/mkv", "video/mov", "video/mpg"),
                    MediaStore.Video.Media.DATE_ADDED
//                MediaStore.Images.Media.DATE_ADDED + " desc"
            )
            var result = ArrayList<MediaInfo>()
//        var sha1list = ArrayList<String>()
            query?.let {
                try {
                    query.moveToFirst()
                    val idcolumnIndex = query.getColumnIndex(MediaStore.Video.Media._ID)
                    val filePathcolumnIndex1 = query.getColumnIndex(MediaStore.Video.Media.DATA)
                    val widthcolumnIndex2 = query.getColumnIndex(MediaStore.Video.Media.WIDTH)
                    val heightcolumnIndex3 = query.getColumnIndex(MediaStore.Video.Media.HEIGHT)
                    val fileNamecolumnIndex4 = query.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
                    val createTimecolumnIndex5 = query.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)
                    val fileSizecolumnIndex6 = query.getColumnIndex(MediaStore.Video.Media.SIZE)
                    val durationcolumnIndex7 = query.getColumnIndex(MediaStore.Video.Media.DURATION)
                    val mimeTypecolumnIndex8 = query.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                    val latitudecolumnIndex9 = query.getColumnIndex(MediaStore.Video.Media.LATITUDE)
                    val longitudecolumnIndex10 = query.getColumnIndex(MediaStore.Video.Media.LONGITUDE)

                    while (query.moveToNext()) {
                        val id: Long = query.getLong(idcolumnIndex)
                        // 获取图片的路径
                        val filePath: String = query.getString(filePathcolumnIndex1)
                        val width: Int = query.getInt(widthcolumnIndex2)
                        val height: Int = query.getInt(heightcolumnIndex3)
                        //获取图片名称
                        val fileName: String = query.getString(fileNamecolumnIndex4)
                        //获取图片时间
                        val createTime: Long = query.getLong(createTimecolumnIndex5)
                        val fileSize: Long = query.getLong(fileSizecolumnIndex6)
                        val duration: Long = query.getLong(durationcolumnIndex7)
                        //获取图片类型
                        val mimeType: String = query.getString(mimeTypecolumnIndex8)
                        val latitude: Float = query.getFloat(latitudecolumnIndex9)
                        val longitude: Float = query.getFloat(longitudecolumnIndex10)
                        if (fileSize < 1024 || filePath.isNullOrEmpty()) {
                            continue
                        }
                        val shA1 = filePath?.let {
                            SHAMD5Utils.getSHA1(filePath)
                        }
                        if ("downloading" != getExtensionName(filePath)) { //过滤未下载完成的文件
                            LogUtils.d("width:", width, " height:", height)
                            val element = MediaInfo(id, fileName, filePath, fileSize, duration, mimeType, true, createTime, null, latitude, longitude, width, height)
                            element.sha1 = shA1
//                            val format1 = format.format(Date(duration))
                            var createTimess = getTimestr(duration)
                            element.timestr = createTimess
//                            element.timestr = converted
//                            LogUtils.d("AAAAABBB${SystemClock.elapsedRealtime()}  createTime:" + createTime + " duration" + duration + "timestr" + element.timestr)
//                            LogUtils.d(element.toString())
                            result.add(element)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    query.close()
                }
                LogUtils.d("result.size:", result.size)
            }
            result
        }

    }


    fun getExtensionName(dotname: String): String {
        return dotname?.substring(dotname.lastIndexOf("."))
    }

    private val SECONDS_PER_MINUTE = 60
    private val SECONDS_PER_HOUR = 60 * 60
    private val SECONDS_PER_DAY = 24 * 60 * 60

    fun getTimestr(duration: Long): String {
//        val millis = duration % 1000

        var seconds = Math.floor(duration / 1000.toDouble()).toInt()
//        var days = 0
        var hours = 0
        var minutes = 0

//        /* if (seconds >= SECONDS_PER_DAY) {
//             days = seconds / SECONDS_PER_DAY
//             seconds -= days * SECONDS_PER_DAY
//         }*/
        if (seconds >= SECONDS_PER_HOUR) {
            hours = seconds / SECONDS_PER_HOUR
            seconds -= hours * SECONDS_PER_HOUR
        }
        if (seconds >= SECONDS_PER_MINUTE) {
            minutes = seconds / SECONDS_PER_MINUTE
            seconds -= minutes * SECONDS_PER_MINUTE
        }
        if (hours > 0) {
            return String.format("%2d:%02d:%02d", hours, minutes, seconds)
        } else {
            return String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun getTimeDurationStr(duration: Long): String? {
        try {
            val sb = StringBuffer()
            val l = duration / 1000 //计算奔视频有多少秒
            val hour = l / 3600 //计算有多少个小时
            val min = (l - hour * 3600) / 60 //计算有多少分钟
            val sec = l % 60 //计算有多少秒
            if (hour != 0L) {
                if (hour < 10) {
                    sb.append("0$hour:")
                } else {
                    sb.append("$hour:")
                }
            }
            if (min < 10) {
                sb.append("0$min:")
            } else {
                sb.append("$min:")
            }
            if (sec < 10) {
                sb.append("0$sec")
            } else {
                sb.append(sec)
            }
            return sb.toString()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        private var mediaInfo: MediaInfoScanHelper? = null
        fun newInstance() = mediaInfo ?: synchronized(this) {
            mediaInfo ?: MediaInfoScanHelper().also { mediaInfo = it }
        }
    }

}