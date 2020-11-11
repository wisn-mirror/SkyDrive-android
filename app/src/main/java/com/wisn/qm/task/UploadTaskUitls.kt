package com.wisn.qm.task

import android.content.Context
import androidx.work.*
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.mode.db.beans.UploadBean
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

object UploadTaskUitls {
    val constraints: Constraints by lazy {
        Constraints.Builder()
//                .setRequiresCharging(true)
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .setRequiresStorageNotLow(true)
                .build()
    }

    fun buildUploadRequest(inputdata: Data?, clazz: KClass<out Worker>): WorkRequest {

        val constraints = OneTimeWorkRequest.Builder(clazz.java)
                //设置指数退避策略。假如Worker线程的执行出现了异常，比如服务器宕机，那么你可能希望过一段时间，重试该任务。那么你可以在Worker的doWork()方法中返回Result.retry()，系统会有默认的指数退避策略来帮你重试任务，你也可以通过setBackoffCriteria()方法，自定义指数退避策略。
                .setBackoffCriteria(BackoffPolicy.LINEAR, OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)//设置指数退避算法
                //设置延迟执行任务。假设你没有设置触发条件，或者当你设置的触发条件符合系统的执行要求，此时，系统有可能立刻执行该任务，但如果你希望能够延迟执行，那么可以通过setInitialDelay()方法，延后任务的执行。
//                .setInitialDelay(10,TimeUnit.SECONDS)//符合触发条件后，延迟10秒执行
                //为任务设置Tag标签。设置Tag后，你就可以通过该抱歉跟踪任务的状态WorkManager.getWorkInfosByTagLiveData(String tag)或者取消任务WorkManager.cancelAllWorkByTag(String tag)。
                .addTag("UploadTag")
                .setConstraints(constraints);
        inputdata?.let {
            constraints.setInputData(inputdata)
        }
        return constraints.build()
    }

    fun exeRequest(context: Context, workRequest: WorkRequest): Operation {
        return WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun buildUploadRequest(): WorkRequest {
        return buildUploadRequest(null, UploadWorker::class)
    }

    fun buildMediaScanWorkerRequest(): WorkRequest {
        return buildUploadRequest(null, MediaScanWorker::class)
    }




    fun buidUploadBean(mediainfo: MediaInfo): UploadBean {
        return UploadBean(mediainfo.id,mediainfo.fileName, mediainfo.filePath, mediainfo.fileSize, mediainfo.mimeType, mediainfo.createTime, mediainfo.pid, mediainfo.uploadStatus, mediainfo.sha1, mediainfo.isVideo, mediainfo.duration)
    }


}