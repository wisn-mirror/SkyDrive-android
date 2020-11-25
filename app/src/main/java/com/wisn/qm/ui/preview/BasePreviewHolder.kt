package com.wisn.qm.ui.preview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wisn.qm.mode.db.beans.MediaInfo

open class BasePreviewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun loadImage(mediainfo: MediaInfo) {

    }

    open fun loadVideo(mediainfo: MediaInfo) {

    }

    open fun releaseVideo() {

    }


}