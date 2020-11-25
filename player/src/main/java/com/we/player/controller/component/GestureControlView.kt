package com.we.player.controller.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.we.player.R
import com.we.player.controller.IGestureViewItemController
import com.we.player.controller.IViewController
import com.we.player.utils.TimeStrUtils
import com.we.player.view.MediaPlayerController

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午10:57
 */
class GestureControlView : FrameLayout, IGestureViewItemController, View.OnClickListener {

    var TAG: String? = "GestureControlView"

    var mediaPlayerController: MediaPlayerController? = null
    var iViewController: IViewController? = null
    var center_container: LinearLayout? = null
    var iv_icon: ImageView? = null
    var tv_percent: TextView? = null
    var pro_percent: ProgressBar? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_controller_gesture, this, true)
        center_container = findViewById(R.id.center_container)
        iv_icon = findViewById(R.id.iv_icon)
        tv_percent = findViewById(R.id.tv_percent)
        pro_percent = findViewById(R.id.pro_percent)
        visibility=View.GONE
    }

    /**
     * 开始滑动
     */
    override fun onGestureStartSlide() {
        visibility=View.VISIBLE
        center_container?.visibility = View.VISIBLE
    }

    /**
     * 结束滑动
     */
    override fun onGestureStopSlide() {
        visibility=View.GONE
        center_container?.visibility = View.GONE

    }

    /**
     * 滑动调整进度
     * @param slidePosition 滑动进度
     * @param currentPosition 当前播放进度
     * @param duration 视频总长度
     */
    override fun onGesturePositionChange(slidePosition: Int, currentPosition: Long, duration: Long) {
        if (slidePosition > currentPosition) {
            iv_icon?.setImageResource(R.drawable.ic_action_fast_forward)
        } else {
            iv_icon?.setImageResource(R.drawable.ic_action_fast_rewind)
        }
        tv_percent?.setText(String.format("%s/%s", TimeStrUtils.stringForTime(slidePosition.toLong()), TimeStrUtils.stringForTime(duration)))
        pro_percent?.visibility = GONE
    }

    /**
     * 滑动调整亮度
     * @param percent 亮度百分比
     */
    override fun onGestureBrightnessChange(percent: Int) {
        iv_icon?.setImageResource(R.drawable.ic_action_brightness)
        tv_percent?.setText("$percent%")
        pro_percent?.progress=percent
        pro_percent?.visibility = View.VISIBLE
    }

    /**
     * 滑动调整音量
     * @param percent 音量百分比
     */
    override fun onGestureVolumeChange(percent: Int) {
        if (percent <= 0) {
            iv_icon?.setImageResource(R.drawable.ic_action_volume_off)
        } else {
            iv_icon?.setImageResource(R.drawable.ic_action_volume_up)
        }
        tv_percent?.setText("$percent%")
        pro_percent?.progress=percent
        pro_percent?.visibility = View.VISIBLE
    }


    override fun attach(mediaPlayerController: MediaPlayerController?, iViewController: IViewController) {
        this.mediaPlayerController=mediaPlayerController
        this.iViewController=iViewController
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {


    }

    override fun onPlayStateChanged(playState: Int) {

    }


    override fun setProgress(duration: Long?, position: Long?) {


    }

    override fun onLockStateChanged(isLocked: Boolean) {}

    override fun onClick(p0: View?) {

    }

}