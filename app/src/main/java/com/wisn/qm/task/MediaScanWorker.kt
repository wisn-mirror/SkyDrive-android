package com.wisn.qm.task

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.blankj.utilcode.util.LogUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.library.base.net.ExceptionHandle
import com.wisn.qm.mode.ConstantKey
import com.wisn.qm.mode.DataRepository
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.AppDataBase
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.mode.net.ApiNetWork
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class MediaScanWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        GlobalScope.launch {
            try {
                var s = System.currentTimeMillis();

                LogUtils.d("CCCCCCCMediaScanWorker2 开始")

                val maxId = AppDataBase.getInstanse().mediaInfoDao?.getMediaInfoMaxId();

                LogUtils.d("CCCCCCCMediaScanWorker2  开始!!!")

                if (maxId != null && maxId > 0) {

                    LogUtils.d("CCCCCCCMediaScanWorker2  差量aa " + maxId + " ???")
                    var start = System.currentTimeMillis();
                    // //todo 再次扫描更新
                    val mediaImageListNew = DataRepository.getInstance().getMediaImageAndVideoList(maxId.toString())
                    var start11 = System.currentTimeMillis();
                    LogUtils.d("CCCCCCCMediaScanWorker2 aa " + (start11 - start))

                    val mediaInfoListAll = AppDataBase.getInstanse().mediaInfoDao?.getMediaInfoListAllNotDelete()
                    var start22 = System.currentTimeMillis();
                    LogUtils.d("CCCCCCCMediaScanWorker2 aaa " + (start22 - start11))

                    mediaImageListNew?.let {
                        if (mediaInfoListAll != null && mediaInfoListAll.isNotEmpty()) {
                            mediaInfoListAll.addAll(mediaImageListNew)
                        }
                    }
                    var start33 = System.currentTimeMillis();

                    LogUtils.d("CCCCCCCMediaScanWorker2 aaaa " + (start33 - start22))


                    mediaInfoListAll?.sortByDescending {
                        it.createTime
                    }
                    LogUtils.d("CCCCCCCMediaScanWorker2 aaaa " + ( System.currentTimeMillis() - start33))

                    var start1 = System.currentTimeMillis();

                    // 先通知显示
                    LiveEventBus
                            .get(ConstantKey.updateHomeMedialist)
                            .post(mediaInfoListAll);
                    mediaImageListNew?.let {
                        AppDataBase.getInstanse().mediaInfoDao?.insertMediaInfo(mediaImageListNew)
                    }
                    LogUtils.d("CCCCCCCMediaScanWorker2 aaaaa" + (System.currentTimeMillis() - start1))
                    LogUtils.d("CCCCCCCMediaScanWorker2 bbb" + (System.currentTimeMillis() - s))

                    dealUploadStatus(mediaInfoListAll)
                    LogUtils.d("CCCCCCCMediaScanWorker2 bbbb" + (System.currentTimeMillis() - s))


                } else {
                    LogUtils.d("CCCCCCCMediaScanWorker2  全量")
                    //第一次加载
                    var start = System.currentTimeMillis();
                    LogUtils.d("CCCCCCCMediaScanWorker2 start" + start)
                    //首次 先扫描，后通知显示
                    val mediaImageList = DataRepository.getInstance().getMediaImageAndVideoList("-1")
                    mediaImageList?.let {
                        mediaImageList?.sortByDescending {
                            it.createTime
                        }
                        var end2 = System.currentTimeMillis();
                        LogUtils.d("CCCCCCCMediaScanWorker2 end2 耗时" + (end2 - end2))
                        AppDataBase.getInstanse().mediaInfoDao?.insertMediaInfo(mediaImageList)
                    }
                    LiveEventBus
                            .get(ConstantKey.updateHomeMedialist)
                            .post(mediaImageList);
                    dealUploadStatus(mediaImageList)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return Result.success()
    }

    private suspend fun dealUploadStatus(mediaImageList: MutableList<MediaInfo>?) {
        try {
            var allSha1sByUser = ApiNetWork.newInstance().getAllSha1sByUser();
            val data = allSha1sByUser?.data
            val iterator = mediaImageList?.iterator() ?: return
            var isUpdate = false;
            while (iterator.hasNext()) {
                val mediainfo = iterator.next() as MediaInfo
                val contains = data?.contains(mediainfo.sha1)
                contains?.let {
                    if (contains) {
                        isUpdate = true
                        mediainfo.uploadStatus = FileType.MediainfoStatus_uploadSuccess
                        mediainfo.id?.let {
                            AppDataBase.getInstanse().mediaInfoDao?.updateMediaInfoStatusById(mediainfo.id!!, FileType.MediainfoStatus_uploadSuccess)
                        }
                    }
                }
                var file = File(mediainfo.filePath)
                if (!file.exists()) {
                    AppDataBase.getInstanse().mediaInfoDao?.updateMediaInfoStatusById(mediainfo.id!!, FileType.MediainfoStatus_Deleted)
                    iterator.remove()
                }
            }
            if (isUpdate) {
                LiveEventBus
                        .get(ConstantKey.updateHomeMedialist)
                        .post(mediaImageList);
            }
        } catch (e: Throwable) {
            ExceptionHandle.handleException(e)
        }
    }
}