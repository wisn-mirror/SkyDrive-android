package com.we.player.controller.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.qmuiteam.qmui.kotlin.wrapContent
import com.we.player.R
import com.we.player.controller.IViewItemController
import com.we.player.controller.WrapController
import com.we.player.player.PlayStatus

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午10:57
 */
class PreviewControlView : FrameLayout, IViewItemController, View.OnClickListener {

    var TAG: String? = "PreviewControlView"
    var controlWrapper: WrapController? = null
    var thumb: ImageView? = null
    var start_play: ImageView? = null
    var loading: ProgressBar? = null
    var ll_net_tip: LinearLayout? = null
    var message: TextView? = null
    var status_btn: TextView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_controller_preview, this, true)
        thumb = findViewById(R.id.thumb)
        start_play = findViewById(R.id.start_play)
        loading = findViewById(R.id.loading)
        ll_net_tip = findViewById(R.id.ll_net_tip)
        message = findViewById(R.id.message)
        status_btn = findViewById(R.id.status_btn)
        start_play?.setOnClickListener(this)
        status_btn?.setOnClickListener(this)
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
        when (playState) {
            PlayStatus.STATE_START_ABORT -> {
                ll_net_tip?.visibility = View.VISIBLE
                start_play?.visibility = View.GONE
                loading?.visibility = View.GONE

            }
            PlayStatus.STATE_BUFFERED,
            PlayStatus.STATE_PREPARING -> {
                ll_net_tip?.visibility = View.GONE
                start_play?.visibility = View.GONE
                loading?.visibility = View.VISIBLE

            }

            PlayStatus.STATE_PAUSED -> {
                ll_net_tip?.visibility = View.GONE
                start_play?.isSelected = false
                start_play?.visibility = View.VISIBLE
                loading?.visibility = View.GONE

            }
            PlayStatus.STATE_PLAYING -> {
                ll_net_tip?.visibility = View.GONE
                start_play?.isSelected = true
                start_play?.visibility = View.GONE
                loading?.visibility = View.GONE
            }

            PlayStatus.STATE_ERROR,
            PlayStatus.STATE_PREPARED,
            PlayStatus.STATE_IDLE,
            PlayStatus.STATE_PLAYBACK_COMPLETED,
            PlayStatus.STATE_BUFFERING -> {
                ll_net_tip?.visibility = View.GONE
                start_play?.visibility = View.GONE
                loading?.visibility = View.GONE

            }
        }
    }

    override fun onPlayerStateChanged(playerState: Int) {

    }

    override fun setProgress(duration: Long?, position: Long?) {

    }

    override fun onLockStateChanged(isLocked: Boolean) {}

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.status_btn -> {
                //继续播放
                controlWrapper?.mediaPlayerController?.start()
            }
            R.id.start_play -> {
                //开始播放
                controlWrapper?.mediaPlayerController?.togglePlay()
            }
        }
    }

}