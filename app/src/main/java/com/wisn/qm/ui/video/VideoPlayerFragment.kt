package com.wisn.qm.ui.video

import android.view.LayoutInflater
import android.view.View
import com.qmuiteam.qmui.arch.QMUIFragment
import com.we.player.controller.StandardController
import com.we.player.controller.component.PlayControlView
import com.we.player.player.exo.AndroidMediaPlayerFactory
import com.we.player.render.SurfaceRenderView
import com.we.player.view.VideoView
import com.wisn.qm.R

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午3:15
 */
class VideoPlayerFragment() : QMUIFragment() {
    override fun onCreateView(): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_videoplayer, null)
    }

    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)
        var videoview  =rootView.findViewById<VideoView>(R.id.videoview)
        videoview?.setUrl("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4")
//        videoview.mIRenderView=TextureRenderView(requireContext())
        videoview.mIRenderView=SurfaceRenderView(requireContext())
//        videoview.mediaPlayer= ExoPlayerFactory()
        videoview.mediaPlayer= AndroidMediaPlayerFactory()
       var standardController= StandardController(requireContext());
        standardController.addIViewItemController(PlayControlView(requireContext()))
        videoview.setIViewControllerView(standardController)
        videoview.start()


    }

    override fun translucentFull(): Boolean {
        return true
    }
}