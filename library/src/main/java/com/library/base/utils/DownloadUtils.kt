package com.library.base.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/2 下午2:17
 */
object DownloadUtils {

    open fun addDownload(context: Context, downloadUrl: String, title: String, description: String): Long {
        val downloadManager = context.applicationContext.getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(downloadUrl))
        request.setDestinationInExternalFilesDir(context.applicationContext, Environment.DIRECTORY_DOWNLOADS,
                "${System.currentTimeMillis()}.apk")
        request.setTitle(title)
        request.setDescription(description)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        return downloadManager.enqueue(request)
    }
}