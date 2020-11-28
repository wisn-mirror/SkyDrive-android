package com.wisn.qm.ui.netpreview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.mode.db.beans.UserDirBean

open class BaseNetPreviewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun loadImage(position:Int,mediainfo: UserDirBean) {

    }

    open fun loadVideo(position:Int,mediainfo: UserDirBean) {

    }

    open fun releaseVideo(position:Int) {

    }


}