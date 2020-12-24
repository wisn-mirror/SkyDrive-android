package com.wisn.qm.ui.album

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.library.base.base.BaseViewModel
import com.wisn.qm.mode.UserDirListDataSource
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.bean.PageKey
import com.wisn.qm.mode.db.AppDataBase
import com.wisn.qm.mode.db.beans.UserDirBean
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.mode.db.beans.UploadBean
import com.wisn.qm.mode.net.ApiNetWork
import com.wisn.qm.task.UploadTaskUitls
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlbumViewModel : BaseViewModel() {
    val userdir = MutableLiveData<UserDirBean>()
    var result = ArrayList<String>()
    var dirlistLD = MutableLiveData<MutableList<UserDirBean>>()
    var selectData = MutableLiveData<MutableList<UserDirBean>>()
    var userDirListDataSource: UserDirListDataSource? = null


    fun selectData(): MutableLiveData<MutableList<UserDirBean>> {
        if (selectData.value == null) {
            selectData.value = ArrayList<UserDirBean>()
        }
        return selectData
    }

    fun editUserDirBean(isinit: Boolean, isAdd: Boolean, userDirBean: UserDirBean?) {
        if (isinit) {
            selectData().value?.clear();
            result.clear()
        }
        userDirBean?.let {
            if (isAdd) {
                selectData().value?.add(userDirBean)
                result.add(userDirBean.sha1!!)
            } else {
                selectData().value?.remove(userDirBean)
                result.remove(userDirBean.sha1!!)
            }
            selectData().value = selectData().value
        }

    }

    /**
     * 获取数据
     * prefetchDistance = 10 预加载10条
     */
    fun getDirListAll(): ArrayList<UserDirBean> {

        arrayListOf(1,2,3)
        if (userDirListDataSource == null) {
            return ArrayList()
        }
        return userDirListDataSource!!.mutableList!!
    }

    fun getUserDirListDataSource(pid: Long) = Pager(PagingConfig(pageSize = 1, prefetchDistance = 40), PageKey(pid)) {
        userDirListDataSource = UserDirListDataSource()
        return@Pager userDirListDataSource!!
    }.flow


    fun addUserDir(filename: String): MutableLiveData<UserDirBean> {
        launchGo({
            val dirlist = ApiNetWork.newInstance().addUserDir(-1, filename)
            if (dirlist.isSuccess()) {
                userdir.value = dirlist.data
            }
            dirlist
        })
        return userdir
    }

    fun getUserDirlist(pid: Long): MutableLiveData<MutableList<UserDirBean>> {
        launchGo({
            val dirlist = ApiNetWork.newInstance().getUserDirlist(pid)
            if (dirlist.isSuccess()) {
                dirlistLD.value = dirlist.data.list
            }
            dirlist
        })
        return dirlistLD
    }

    fun deletefiles(pid: Long) {
        launchGo({
            var sb = StringBuilder();
            result.forEachIndexed { index, s ->
                if (index == (result.size - 1)) {
                    sb.append(s)
                } else {
                    sb.append(s + ";")
                }
            }
            val dirlist = ApiNetWork.newInstance().deletefiles(pid, sb.toString())
            if (dirlist.isSuccess()) {
                getUserDirlist(pid);
            }
            dirlist
        })
    }


    fun saveMedianInfo(selectData: ArrayList<MediaInfo>, get: UserDirBean) {
        LogUtils.d("saveMedianInfo", Thread.currentThread().name)
        GlobalScope.launch {

            LogUtils.d("saveMedianInfo", Thread.currentThread().name)
            //子线程
            var uploadlist = ArrayList<UploadBean>()
            for (mediainfo in selectData) {
                mediainfo.pid = get.id
                mediainfo.uploadStatus = FileType.UPloadStatus_Noupload
                uploadlist.add(UploadTaskUitls.buidUploadBean(mediainfo))
            }
            LogUtils.d("uploadlist size", uploadlist.size)
            AppDataBase.getInstanse().uploadBeanDao?.insertUploadBeanList(uploadlist)
            UploadTaskUitls.exeRequest(Utils.getApp(), UploadTaskUitls.buildUploadRequest())
        }
    }

}