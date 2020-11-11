package com.wisn.qm.ui.selectpic

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.library.base.base.BaseViewModel
import com.wisn.qm.mode.DataRepository
import com.wisn.qm.mode.db.AppDataBase
import com.wisn.qm.mode.db.beans.MediaInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

class SelectPictureViewModel : BaseViewModel() {
    var listdata = MutableLiveData<MutableList<MediaInfo>>()
    var selectData = MutableLiveData<ArrayList<MediaInfo>>()

    fun getMediaImageList(): MutableLiveData<MutableList<MediaInfo>> {

        LogUtils.d("getMediaImageList3AA ", Thread.currentThread().name)
        launchUI {
            launchFlow {
                LogUtils.d("getMediaImageList3 ", Thread.currentThread().name)
                val maxId = AppDataBase.getInstanse().mediaInfoDao?.getMediaInfoMaxId();
                if (maxId != null && maxId > 0) {
                    val mediaImageListNew = DataRepository.getInstance().getMediaImageAndVideoList(maxId.toString())
                    val mediaInfoListAll = AppDataBase.getInstanse().mediaInfoDao?.getMediaInfoListAllNotDelete()
                    mediaImageListNew?.let {
                        if (mediaInfoListAll != null && mediaInfoListAll.isNotEmpty()) {
                            mediaInfoListAll.addAll(mediaImageListNew)
                        }
                    }
                    mediaInfoListAll?.sortByDescending {
                        it.createTime
                    }
                    mediaImageListNew?.let {
                        AppDataBase.getInstanse().mediaInfoDao?.insertMediaInfo(mediaImageListNew)
                    }
                    mediaInfoListAll;
                } else {
                    val mediaImageList = DataRepository.getInstance().getMediaImageAndVideoList("-1")
                    mediaImageList?.let {
                        mediaImageList?.sortByDescending {
                            it.createTime
                        }
                        AppDataBase.getInstanse().mediaInfoDao?.insertMediaInfo(mediaImageList)
                    }
                    mediaImageList;
                }
            }.flowOn(Dispatchers.IO).collect {
                LogUtils.d("getMediaImageList3 BBB", Thread.currentThread().name)
                listdata.value = it
            }
        }
        return listdata
    }

    fun selectData(): MutableLiveData<ArrayList<MediaInfo>> {
        if (selectData.value == null) {
            selectData.value = ArrayList<MediaInfo>()
        }
        return selectData
    }
}