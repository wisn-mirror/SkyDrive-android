package com.wisn.qm.mode.net

import com.library.base.config.UserBean
import com.library.base.net.RetrofitClient
import com.wisn.qm.mode.bean.BaseResult
import com.wisn.qm.mode.bean.MultiPartInfo
import com.wisn.qm.mode.db.beans.UserDirBean
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.Query

class ApiNetWork {

     var mService: Api? =null
//     var getServie() by lazy { RetrofitClient.getInstance().create(Api::class.java) }

    suspend fun login(username: String, password: String): BaseResult<String> {
        return getServie().login(username, password)
    }

    fun getServie(): Api {
        if (mService == null) {
            synchronized(this) {
                if (mService == null) {
                    mService = RetrofitClient.getInstance().create(Api::class.java)
                }
            }
        }
        return mService!!
    }

    fun updateBaseUrl(ip: String) {
        val instance = RetrofitClient.getInstance();
        instance.updateBaseUrl(ip)
        mService = instance.create(Api::class.java)
    }


    suspend fun register(username: String, password: String, password1: String): BaseResult<UserBean> {
        return getServie().register(username, password, password1)
    }

    /**
     * 用户信息
     */
    suspend fun getUserInfo(): BaseResult<UserBean> {
        return getServie().getUserInfo()
    }

    /**
     * 用户退出
     */
    suspend fun singout(): BaseResult<String> {
        return getServie().singout()
    }

    /**
     * 单个文件信息
     */
    suspend fun getFileInfo(sha1: Long): BaseResult<UserDirBean> {
        return getServie().getFileInfo(sha1)
    }


    /**
     * 删除单个文件
     */
    suspend fun deleteFile(sha1: String): BaseResult<String> {
        return getServie().deleteFile(sha1)
    }

    /**
     * 所有文件sha1
     */
    suspend fun getAllSha1sByUser(): BaseResult<MutableList<String>> {
        return getServie().getAllSha1sByUser()
    }

    /**
     * 当前用户所有文件夹
     */
    suspend fun getUserFileAlllist(): BaseResult<List<UserDirBean>> {
        return getServie().getUserFileAlllist()
    }

    /**
     * 单文件上传
     */
    suspend fun uploadFile(sha1: String, pid: Long, isVideo: Boolean, minetype: String, videoduration: Long, file: MultipartBody.Part): BaseResult<UserDirBean> {
        return getServie().uploadFile(sha1, pid, isVideo, minetype, videoduration, file)
    }


    suspend fun deletefiles(pid: Long, sha1s: String): BaseResult<Any> {
        return getServie().deletefiles(pid, sha1s)
    }

    /**
     *更改用户头像
     */
    suspend fun updateUserPhoto(file: MultipartBody.Part): BaseResult<UserDirBean> {
        return getServie().updateUserPhoto(file)
    }

    /**
     * 修改用户名
     */
    suspend fun updateUserName(filename: String): BaseResult<Boolean> {
        return getServie().updateUserName(filename)
    }


    /**
     * 单文件上传，秒传
     */
    suspend fun uploadFileHitpass(pid: Long, sha1: String): BaseResult<UserDirBean> {
        return getServie().uploadFileHitpass(pid, sha1)
    }


    /**
     * 每个目录的文件夹列表
     */
    suspend fun getUserDirlist(@Query("pid") pid: Long): BaseResult<MutableList<UserDirBean>> {
        return getServie().getUserDirlist(pid)
    }


    /**
     * 添加文件夹
     */
    suspend fun addUserDir(@Field("pid") pid: Long, @Field("filename") filename: String): BaseResult<UserDirBean> {
        return getServie().addUserDir(pid, filename)
    }

    /**
     * 初始化分块上传
     */
    suspend fun initMultipartInfo(@Query("pid") pid: Long, @Query("filename") filename: String,
                                  @Query("filesize") filesize: Long, @Query("sha1") sha1: String): BaseResult<MultiPartInfo> {
        return getServie().initMultipartInfo(pid, filename, filesize, sha1)
    }

    /**
     * 通知分块上传完成
     */
    suspend fun finishMultipartInfo(@Query("uploadId") uploadId: String, @Query("sha1") sha1: String): BaseResult<MultiPartInfo> {
        return getServie().finishMultipartInfo(uploadId, sha1)
    }

    /**
     * 分块上传
     */
    suspend fun uploadMultipartInfo(@Query("pid") pid: Long, @Query("uploadId") uploadId: String, @Query("chunkindex") chunkindex: Int): BaseResult<MultiPartInfo> {
        return getServie().uploadMultipartInfo(pid, uploadId, chunkindex)
    }

    /**
     * 删除文件夹
     */
    suspend fun deleteDirs(@Query("ids") ids: String): BaseResult<Boolean> {
        return getServie().deleteDirs(ids)
    }

    companion object {
        private var netWork: ApiNetWork? = null
        fun newInstance() = netWork ?: synchronized(this) {
            netWork ?: ApiNetWork().also { netWork = it }
        }
    }
}