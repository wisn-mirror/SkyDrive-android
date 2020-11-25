package com.wisn.qm.ui.preview

import android.content.Context
import android.view.View
import com.library.base.utils.GlideUtils
import com.we.player.controller.component.PlayControlView
import com.we.player.controller.controller.PreviewVideoController
import com.we.player.player.exo.ExoPlayerFactory
import com.we.player.render.TextureRenderView
import com.we.player.view.VideoView
import com.wisn.qm.R
import com.wisn.qm.mode.db.beans.MediaInfo

class PreviewVideoViewHolder(var context: Context, view: View, var previewCallback: PreviewCallback) : BasePreviewHolder(view) {
    var videoview: VideoView = view.findViewById(R.id.videoview)
    var standardController = PreviewVideoController(context)
    val preViewImage = standardController.previewControlView?.thumb

    init {
        videoview.mIRenderView = TextureRenderView(context)
        videoview.mediaPlayer = ExoPlayerFactory()
        standardController.addIViewItemControllerOne(PlayControlView(context))
        videoview.iViewController = standardController
        videoview.setLooping(true)
    }

    override fun loadVideo(mediainfo: MediaInfo) {
        videoview.pause()
        preViewImage?.let {
            GlideUtils.load(mediainfo.filePath!!,preViewImage)

        }
        videoview.setUrl(mediainfo.filePath!!)
        videoview.start()
    }

    override fun releaseVideo() {
        super.releaseVideo()
        videoview.stop()
    }


}