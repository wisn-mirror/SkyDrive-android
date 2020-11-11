package com.wisn.qm.ui.home.picture

import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.ui.home.HomeFragment
 interface PictureCallBack {
    fun showPictureControl(isShow: Boolean?)
    fun changeSelectData(isinit:Boolean ,isSelectModel:Boolean,isAdd:Boolean,item: MediaInfo?)
    fun getHomeFragment(): HomeFragment
}