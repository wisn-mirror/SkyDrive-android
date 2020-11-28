package com.wisn.qm.ui.netpreview

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.library.base.config.Constant
import com.library.base.utils.GlideUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.wisn.qm.R
import com.wisn.qm.mode.db.beans.UserDirBean
import com.wisn.qm.ui.preview.view.PreviewLocalControlView

class NetPreviewVideoViewHolder(var context: Context, var view: View, var previewCallback: PreviewCallback) : BaseNetPreviewHolder(view) {
    val TAG: String = "PreviewVideoViewHolder"

    var content: FrameLayout = view.findViewById(R.id.content)
    var preview: PreviewLocalControlView = view.findViewById(R.id.preview)
    var pos: Int = -1

    override fun loadVideo(position: Int, mediainfo: UserDirBean) {
        mediainfo.sha1?.let {
            GlideUtils.loadUrl(Constant.getImageUrl(it)!!, preview.thumb!!)
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