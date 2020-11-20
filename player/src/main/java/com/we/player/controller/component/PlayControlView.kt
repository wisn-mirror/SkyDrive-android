package com.we.player.controller.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.blankj.utilcode.util.LogUtils
import com.we.player.R
import com.we.player.controller.IViewItemController
import com.we.player.controller.WrapController
import com.we.player.player.PlayStatus
import com.we.player.utils.TimeStrUtils

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午10:57
 */
class PlayControlView : FrameLayout, IViewItemController, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    var TAG: String? = "PlayControlView"
    var controlWrapper: WrapController? = null
    var bottom_container: LinearLayout? = null
    var fullscreen: ImageView? = null
    var iv_play: ImageView? = null
    var curr_time: TextView? = null
    var seekBar: SeekBar? = null
    var total_time: TextView? = null
    var bottom_progress: ProgressBar? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_controller_play, this, true)
        bottom_container = this.findViewById<LinearLayout>(R.id.bottom_container)
        fullscreen = this.findViewById<ImageView>(R.id.fullscreen)
        iv_play = this.findViewById<ImageView>(R.id.iv_play)
        curr_time = this.findViewById<TextView>(R.id.curr_time)
        seekBar = this.findViewById<SeekBar>(R.id.seekBar)
        total_time = this.findViewById<TextView>(R.id.total_time)
        bottom_progress = this.findViewById<ProgressBar>(R.id.bottom_progress)
        iv_play?.setOnClickListener(this)
        fullscreen?.setOnClickListener(this)
        seekBar?.setOnSeekBarChangeListener(this)
    }

    override fun attach(controlWrapper: WrapController?) {
        this.controlWrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {


    }

    override fun onPlayStateChanged(playState: Int) {
        LogUtils.d(TAG, "onPlayStateChanged", playState)
        when (playState) {
            PlayStatus.STATE_IDLE,
            PlayStatus.STATE_PLAYBACK_COMPLETED -> {
                bottom_progress?.progress = 0
                seekBar?.progress = 0
            }
            PlayStatus.STATE_START_ABORT,
            PlayStatus.STATE_ERROR,
            PlayStatus.STATE_PREPARED,
            PlayStatus.STATE_PREPARING -> {

            }
            PlayStatus.STATE_PLAYING -> {
                controlWrapper?.iViewController?.startProgress()
                iv_play?.isSelected = true
            }
            PlayStatus.STATE_PAUSED -> {
                controlWrapper?.iViewController?.stopProgress()
                iv_play?.isSelected = false
            }
            PlayStatus.STATE_BUFFERED, PlayStatus.STATE_BUFFERING -> {
                val playing = controlWrapper?.mediaPlayerController?.isPlaying()
                playing?.let {
                    iv_play?.isSelected = it
                }
            }
        }
    }

    override fun onPlayerStateChanged(playerState: Int) {

    }

    override fun setProgress(duration: Long?, position: Long?) {
        duration?.let {
            total_time?.setText(TimeStrUtils.stringForTime(it))
            bottom_progress?.max = it.toInt()
            seekBar?.max = it.toInt()
        }
        position?.let {
            curr_time?.setText(TimeStrUtils.stringForTime(it))
            bottom_progress?.progress = it.toInt()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                seekBar?.setProgress(it.toInt(), true)
            } else {
                seekBar?.progress = it.toInt()
            }
        }

    }

    override fun onLockStateChanged(isLocked: Boolean) {}

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fullscreen -> {
                val isFull = controlWrapper?.mediaPlayerController?.isFullScreen()
                if (isFull == null || !isFull) {
                    controlWrapper?.mediaPlayerController?.startFullScreen()
                } else {
                    controlWrapper?.mediaPlayerController?.stopFullScreen()
                }
            }
            R.id.iv_play -> {
                controlWrapper?.mediaPlayerController?.togglePlay()
            }
        }
    }

    override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
        if (!fromUser) {
            return
        }
        var max = seekBar?.max
        if (max != null && max > 0) {
            val duration = controlWrapper?.mediaPlayerController?.getDuration();
            duration?.let {
                var target = duration * progress / max
                curr_time?.setText(TimeStrUtils.stringForTime(target))
            }
        }

    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {
        var max = seekBar?.max
        if (max != null && max > 0) {
            val duration = controlWrapper?.mediaPlayerController?.getDuration();
            var target = duration!! * p0?.progress!! / max
            controlWrapper?.mediaPlayerController?.seekTo(target)
        }
    }


}