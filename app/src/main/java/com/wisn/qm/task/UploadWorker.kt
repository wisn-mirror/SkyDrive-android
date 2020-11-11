package com.wisn.qm.task

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.library.base.utils.SHAMD5Utils
import com.wisn.qm.mode.ConstantKey
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.AppDataBase
import com.wisn.qm.mode.db.beans.UploadBean
import com.wisn.qm.mode.net.ApiNetWork
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class UploadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        println("0000doWork, + ${Thread.currentThread().name}")

        val job = GlobalScope.launch {
            try {// launch a new coroutine and keep a reference to its Job
//                val newInstance = AppDataBaseHelper.newInstance(Utils.getApp())
                val uploadDataList = AppDataBase.getInstanse().uploadBeanDao?.getUploadBeanListPreUpload(FileType.UPloadStatus_Noupload)
                if (uploadDataList != null) {
                    for (uploadbean in uploadDataList) {
                        LogUtils.d("0000doWork" + uploadbean.toString())
                        //如果sha1为null 先生成sha1
                        if (uploadbean.sha1.isNullOrEmpty()) {
                            uploadbean.sha1 = uploadbean.filePath?.let {
                                SHAMD5Utils.getSHA1(uploadbean.filePath)
                            }
                        }
                        //先尝试秒传
                        val uploadFileHitpass = uploadbean.sha1?.let {
                             ApiNetWork.newInstance().uploadFileHitpass(uploadbean.pid, uploadbean.sha1!!)
                        }
                        if (uploadFileHitpass != null) {
                            if (uploadFileHitpass.isUploadSuccess()) {
                                //修改上传成功状态
                                AppDataBase.getInstanse().uploadBeanDao?.updateUploadBeanStatus(uploadbean.id, FileType.UPloadStatus_uploadSuccess,System.currentTimeMillis())
                                AppDataBase.getInstanse().mediaInfoDao?.updateMediaInfoStatusBySha1(uploadbean.sha1!!, FileType.MediainfoStatus_uploadSuccess)
                            } else {
                                //秒传失败，要重新上传文件
                                uploadFile(uploadbean)
                            }
                        } else {
                            uploadFile(uploadbean)
                        }
                    }
                    LogUtils.d("0000doWork  LiveEventBus")
                    LiveEventBus
                            .get(ConstantKey.updatePhotoList)
                            .postDelay(1, 1000);
                    LiveEventBus
                            .get(ConstantKey.updateAlbum)
                            .post(1);
                }
            } catch (e: Exception) {
            }

        }
        return Result.success()
    }

    private suspend fun uploadFile(uploadbean: UploadBean) {
        var mimetype = uploadbean.mimeType ?: "multipart/form-data"
        var requestFile = RequestBody.create(MediaType.parse(mimetype), File(uploadbean.filePath!!))
        val body = MultipartBody.Part.createFormData("file", uploadbean.fileName, requestFile)

        val uploadFile =  ApiNetWork.newInstance().uploadFile(uploadbean.sha1!!, uploadbean.pid, uploadbean.isVideo!!, uploadbean.mimeType!!, uploadbean.duration!!, body)
        if (uploadFile.isUploadSuccess()) {
            AppDataBase.getInstanse().uploadBeanDao?.updateUploadBeanStatus(uploadbean.id, FileType.UPloadStatus_uploadSuccess,System.currentTimeMillis())
            LogUtils.d("0000doWork   " + uploadFile.data())

        }
    }
}