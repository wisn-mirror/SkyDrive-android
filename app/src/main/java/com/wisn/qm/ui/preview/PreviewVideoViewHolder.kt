package com.wisn.qm.ui.preview

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.library.base.utils.GlideUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.we.player.controller.component.PreviewControlView
import com.wisn.qm.R
import com.wisn.qm.mode.db.beans.MediaInfo

class PreviewVideoViewHolder(var context: Context, var view: View, var previewCallback: PreviewCallback) : BasePreviewHolder(view) {
    val TAG: String = "PreviewVideoViewHolder"

    var content: FrameLayout = view.findViewById(R.id.content)
    var preview: PreviewControlView = view.findViewById(R.id.preview)
    var pos: Int = -1

    override fun loadVideo(position: Int, mediainfo: MediaInfo) {
        preview.let {
            GlideUtils.load(mediainfo.filePath!!, preview.thumb!!)
        }
        this.pos = position
        view.tag = this

        content.onClick {
            previewCallback.onContentClick(it)
        }
    }

    override fun releaseVideo(position: Int) {

    }


}