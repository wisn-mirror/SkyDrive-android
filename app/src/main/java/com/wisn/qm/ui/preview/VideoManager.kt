package com.wisn.qm.ui.preview

import android.view.ViewGroup
import com.library.base.BaseApp
import com.we.player.controller.controller.ListVideoController
import com.we.player.player.exo.ExoPlayerFactory
import com.we.player.render.impl.TextureRenderViewFactory
import com.we.player.view.VideoView


/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/26 下午3:41
 */
object VideoManager {
  /*  var currentPlay: Int? = -1
    var videoMap: HashMap<String, VideoView> = HashMap()

    fun getVideoView(tag: String): VideoView {
        var videoview = videoMap.get(tag)
        if (videoview == null) {
            videoview = VideoView(BaseApp.app)
            videoview.renderViewFactory = TextureRenderViewFactory()
            videoview?.mediaPlayer = ExoPlayerFactory()
            videoview?.iViewController = ListVideoController(BaseApp.app)
            videoview.setLooping(true)
            videoMap.put(tag, videoview)
        }
        return videoview
    }

    fun recyclewByTag(tag: String) {
        var videoview = videoMap.get(tag)
        if (videoview != null) {
            videoview.parent?.let {
                it as ViewGroup
                it.removeView(videoview)
            }
            videoview.release()
        }
    }*/
}