package com.wisn.qm.ui.video

import android.view.LayoutInflater
import android.view.View
import com.library.base.utils.GlideUtils
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.we.player.controller.component.TitleControlView
import com.we.player.controller.controller.StandardController
import com.we.player.player.exo.ExoPlayerFactory
import com.we.player.render.TextureRenderView
import com.we.player.view.VideoView
import com.wisn.qm.R

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午3:15
 */
class VideoPlayerFragment(var videourl: String, var thumbUrl: String,var title: String, var isplayAuto: Boolean) : QMUIFragment(), View.OnClickListener {
    val TAG: String = "VideoPlayerFragment"
    var videoview: VideoView? = null
    override fun onCreateView(): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_videoplayer, null)
    }

    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)
        QMUIStatusBarHelper.setStatusBarDarkMode(requireActivity())
        videoview = rootView.findViewById<VideoView>(R.id.videoview)
        videoview?.setUrl(videourl)
        videoview?.mIRenderView = TextureRenderView(requireContext())
//        videoview?.mIRenderView = SurfaceRenderView(requireContext())
        videoview?.mediaPlayer = ExoPlayerFactory()
//        videoview?.mediaPlayer = AndroidMediaPlayerFactory()
        var standardController = StandardController(requireContext());
//        standardController.addIViewItemControllerOne(PlayControlView(requireContext()))
        val preViewImage = standardController.previewControlView?.thumb
        videoview?.iViewController = standardController
        videoview?.setLooping(true)
        preViewImage?.let {
            GlideUtils.loadUrl(thumbUrl, it)
            it.visibility=View.VISIBLE
        }
        if (isplayAuto) {
            videoview?.start()
        }
        standardController.titleControlView?.title?.text = title
        standardController.titleControlView?.backListener = object : TitleControlView.BackListener {
            override fun back() {
                super.back()
                popBackStack()
            }
        }
    }

    override fun translucentFull(): Boolean {
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start -> {
            }
        }
    }

    override fun onBackPressed() {
        if (videoview?.onBackPressed() == true) {
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        QMUIStatusBarHelper.setStatusBarLightMode(activity)
    }
}