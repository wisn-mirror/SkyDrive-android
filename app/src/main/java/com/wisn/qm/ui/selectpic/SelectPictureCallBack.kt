package com.wisn.qm.ui.selectpic

import com.wisn.qm.mode.db.beans.MediaInfo
 interface SelectPictureCallBack {
    fun changeSelectData(isAdd:Boolean,item: MediaInfo?);
}