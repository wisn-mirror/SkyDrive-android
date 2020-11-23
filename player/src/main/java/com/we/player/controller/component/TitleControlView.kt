package com.we.player.controller.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.blankj.utilcode.util.LogUtils
import com.we.player.R
import com.we.player.controller.IViewController
import com.we.player.controller.IViewItemController
import com.we.player.player.PlayStatus
import com.we.player.player.PlayStatusStr
import com.we.player.view.MediaPlayerController

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午10:57
 */
class TitleControlView : FrameLayout, IViewItemController, View.OnClickListener {

    var TAG: String? = "TitleControlView"

    var mediaPlayerController: MediaPlayerController? = null
    var iViewController: IViewController? = null
    var back: ImageView? = null
    var title: TextView? = null
    var sys_time: TextView? = null
    var battery: TextView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_controller_title, this, true)
        back = findViewById(R.id.back)
        title = findViewById(R.id.title)
        sys_time = findViewById(R.id.sys_time)
        battery = findViewById(R.id.battery)
        back?.setOnClickListener(this)
        visibility = GONE
    }

    override fun attach(mediaPlayerController: MediaPlayerController?, iViewController: IViewController) {
        this.mediaPlayerController = mediaPlayerController
        this.iViewController = iViewController
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {
        if (isVisible) {
            val fullScreen = mediaPlayerController?.isFullScreen()!!
            val islocked = iViewController?.isLocked()!!
            if (fullScreen && !islocked) {
                visibility = VISIBLE
            } else {
                visibility = GONE
            }
        } else {
            visibility = GONE

        }
    }

    override fun onPlayStateChanged(playState: Int) {
        LogUtils.d(TAG, "onPlayStateChanged  $playState  ${PlayStatusStr.getStatusStr(playState)} ")
        when (playState) {
            PlayStatus.PLAYER_FULL_SCREEN -> {
                visibility = VISIBLE
            }
            PlayStatus.PLAYER_NORMAL -> {
                visibility = GONE
            }
        }
    }

    override fun setProgress(duration: Long?, position: Long?) {


    }

    override fun onLockStateChanged(isLocked: Boolean) {}

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.back -> {
                mediaPlayerController?.onBackPressed()
            }
        }
    }

}