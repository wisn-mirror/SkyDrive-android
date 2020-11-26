package com.wisn.qm.ui.preview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wisn.qm.mode.db.beans.MediaInfo

open class BasePreviewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun loadImage(position:Int,mediainfo: MediaInfo) {

    }

    open fun loadVideo(position:Int,mediainfo: MediaInfo) {

    }

    open fun releaseVideo(position:Int) {

    }


}