package com.wisn.qm.ui.video

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.qmuiteam.qmui.arch.QMUIFragment
import com.we.player.controller.controller.StandardController
import com.we.player.controller.component.PlayControlView
import com.we.player.player.ScreenConfig
import com.we.player.player.exo.AndroidMediaPlayerFactory
import com.we.player.render.TextureRenderView
import com.we.player.view.VideoView
import com.wisn.qm.R

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午3:15
 */
class VideoPlayerFragment : QMUIFragment(), View.OnClickListener {
    var videoview: VideoView? = null
    var speedsBt: Button? = null
    override fun onCreateView(): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_videoplayer, null)
    }

    override fun onViewCreated(rootView: View) {
        super.onViewCreated(rootView)
        videoview = rootView.findViewById<VideoView>(R.id.videoview)
        videoview?.setUrl("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4")
        videoview?.mIRenderView = TextureRenderView(requireContext())
//        videoview?.mIRenderView = SurfaceRenderView(requireContext())
//        videoview?.mediaPlayer= ExoPlayerFactory()
        videoview?.mediaPlayer = AndroidMediaPlayerFactory()
        var standardController = StandardController(requireContext());
//        standardController.addIViewItemControllerOne(PlayControlView(requireContext()))
        videoview?.iViewController = standardController
        videoview?.setLooping(true)
        videoview?.isMute = true

        rootView.findViewById<View>(R.id.start).setOnClickListener(this)
        rootView.findViewById<View>(R.id.scaletype).setOnClickListener(this)
        rootView.findViewById<View>(R.id.mirrorRotion).setOnClickListener(this)
        rootView.findViewById<View>(R.id.Rotation).setOnClickListener(this)
        speedsBt = rootView.findViewById(R.id.Speeds)
        speedsBt?.setOnClickListener(this)


    }

    override fun translucentFull(): Boolean {
        return true
    }

    var isMirror: Boolean = false
    var degree: Int = 0
    var speeds: Float = 1f
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.start -> {
                videoview?.start()
            }
            R.id.scaletype -> {
                when (videoview?.mCurrentScreenScaleType) {
                    ScreenConfig.SCREEN_SCALE_DEFAULT -> {
                        videoview?.mCurrentScreenScaleType = ScreenConfig.SCREEN_SCALE_16_9
                    }
                    ScreenConfig.SCREEN_SCALE_16_9 -> {
                        videoview?.mCurrentScreenScaleType = ScreenConfig.SCREEN_SCALE_4_3
                    }
                    ScreenConfig.SCREEN_SCALE_4_3 -> {
                        videoview?.mCurrentScreenScaleType = ScreenConfig.SCREEN_SCALE_MATCH_PARENT
                    }
                    ScreenConfig.SCREEN_SCALE_MATCH_PARENT -> {
                        videoview?.mCurrentScreenScaleType = ScreenConfig.SCREEN_SCALE_ORIGINAL
                    }
                    ScreenConfig.SCREEN_SCALE_ORIGINAL -> {
                        videoview?.mCurrentScreenScaleType = ScreenConfig.SCREEN_SCALE_CENTER_CROP
                    }
                    ScreenConfig.SCREEN_SCALE_CENTER_CROP -> {
                        videoview?.mCurrentScreenScaleType = ScreenConfig.SCREEN_SCALE_DEFAULT
                    }
                }
            }
            R.id.mirrorRotion -> {
                isMirror = !isMirror
                videoview?.setMirrorRotation(isMirror)
            }
            R.id.Rotation -> {
                degree + 90
                videoview?.setVideoRotation(degree)

            }
            R.id.Speeds -> {
                speeds = speeds + 0.5f
                if (speeds > 10) {
                    speeds = 1f
                }
                videoview?.setSpeed(speeds)
                speedsBt?.setText("speed$speeds")
            }
        }
    }

    override fun onBackPressed() {
        if(videoview?.onBackPressed() == true){
            return
        }
        super.onBackPressed()
    }
}