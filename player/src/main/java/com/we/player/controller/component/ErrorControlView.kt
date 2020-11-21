package com.we.player.controller.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.we.player.R
import com.we.player.controller.IViewController
import com.we.player.controller.IViewItemController
import com.we.player.player.PlayStatus
import com.we.player.view.MediaPlayerController

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午10:57
 */
class ErrorControlView : FrameLayout, IViewItemController, View.OnClickListener {

    var TAG: String? = "ErrorControlView"
    var mediaPlayerController: MediaPlayerController? = null
    var iViewController: IViewController? = null
    var message: TextView? = null
    var status_btn: TextView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_controller_error, this, true)
        message = findViewById<TextView>(R.id.message)
        status_btn = findViewById<TextView>(R.id.status_btn)
        status_btn?.setOnClickListener(this)
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
        when (playState) {
            PlayStatus.STATE_ERROR -> {
                visibility = View.VISIBLE
            }
            PlayStatus.STATE_IDLE,
            PlayStatus.STATE_PLAYBACK_COMPLETED,
            PlayStatus.STATE_START_ABORT,
            PlayStatus.STATE_PREPARED,
            PlayStatus.STATE_PREPARING,
            PlayStatus.STATE_PLAYING,
            PlayStatus.STATE_PAUSED,
            PlayStatus.STATE_BUFFERED, PlayStatus.STATE_BUFFERING -> {
                visibility = View.GONE
            }
        }
    }


    override fun setProgress(duration: Long?, position: Long?) {


    }

    override fun onLockStateChanged(isLocked: Boolean) {}

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.status_btn -> {
                mediaPlayerController?.replay(false)
            }
        }
    }

}