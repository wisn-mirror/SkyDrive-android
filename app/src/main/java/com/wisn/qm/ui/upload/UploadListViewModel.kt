package com.wisn.qm.ui.upload

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.library.base.base.BaseViewModel
import com.wisn.qm.mode.db.AppDataBase
import com.wisn.qm.mode.db.beans.UploadBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

class UploadListViewModel : BaseViewModel() {
    var listdata = MutableLiveData<MutableList<UploadBean>>()

    fun getUploadList(): MutableLiveData<MutableList<UploadBean>> {
        LogUtils.d("UploadListViewModel ", Thread.currentThread().name)
        launchUI {
            launchFlow {
                LogUtils.d("UploadListViewModel ", Thread.currentThread().name)
                AppDataBase.getInstanse().uploadBeanDao?.getUploadBeanListAll();

            }.flowOn(Dispatchers.IO).collect {
                LogUtils.d("getMediaImageList3 BBB", Thread.currentThread().name)
                listdata.value = it
            }
        }
        return listdata
    }

}