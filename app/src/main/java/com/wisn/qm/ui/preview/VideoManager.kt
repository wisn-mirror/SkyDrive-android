package com.wisn.qm.ui.preview


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