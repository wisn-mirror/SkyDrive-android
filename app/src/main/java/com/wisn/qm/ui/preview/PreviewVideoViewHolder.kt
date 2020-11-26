package com.wisn.qm.ui.preview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blankj.utilcode.util.LogUtils
import com.library.base.BaseApp
import com.library.base.utils.GlideUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.we.player.controller.component.PreviewControlView
import com.we.player.controller.controller.ListVideoController
import com.we.player.view.VideoView
import com.wisn.qm.R
import com.wisn.qm.mode.db.beans.MediaInfo

class PreviewVideoViewHolder(var context: Context, view: View, var previewCallback: PreviewCallback) : BasePreviewHolder(view) {
    val TAG: String = "PreviewVideoViewHolder"
    var videoview: VideoView? = null

    var content: FrameLayout = view.findViewById(R.id.content)
    var preview: PreviewControlView = view.findViewById(R.id.preview)

    init {
        content?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
//                LogUtils.d(TAG, " addOnAttachStateChangeListener!!!! onViewAttachedToWindow $v")
//                videoview?.start()

            }

            override fun onViewDetachedFromWindow(v: View?) {
//                LogUtils.d(TAG, " addOnAttachStateChangeListener!!!! onViewDetachedFromWindow $v")
                VideoManager.recyclewByTag("TAG")
            }
        })
    }

    override fun loadVideo(position: Int, mediainfo: MediaInfo) {

        preview?.let {
            GlideUtils.load(mediainfo.filePath!!, preview.thumb!!)
        }
        preview?.playClick = object : PreviewControlView.PlayClick {
            override fun click(): Boolean {
                if (VideoManager.currentPlay != position) {
                    VideoManager.currentPlay = position
                    videoview = VideoManager.getVideoView("TAG")
                    var standardController = videoview?.iViewController as ListVideoController
                    standardController.addIViewItemControllerOne(preview, true)
                    videoview?.parent?.let {
                        it as ViewGroup
                        it.removeView(videoview)
                    }
                    content.addView(videoview, 0)
                    videoview?.setUrl(mediainfo.filePath!!)
                    videoview?.start()
                    LogUtils.d(TAG, " addOnAttachStateChangeListener!!!! loadVideo!!!!!!!!!!!!!! ")
                    return true
                }
                return false
            }

        }

    }


    override fun releaseVideo(position: Int) {

    }


}