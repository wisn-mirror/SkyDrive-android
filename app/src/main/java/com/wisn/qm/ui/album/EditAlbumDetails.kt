package com.wisn.qm.ui.album

import com.wisn.qm.mode.db.beans.UserDirBean

interface EditAlbumDetails {
    fun isShowEdit(isShow: Boolean)
    fun changeSelectData(isinit: Boolean, isAdd: Boolean, userDirBean: UserDirBean?)

}