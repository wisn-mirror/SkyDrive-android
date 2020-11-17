package com.we.player.controller.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.*
import com.we.player.R
import com.we.player.controller.IViewItemController
import com.we.player.controller.WrapController
import com.we.player.player.PlayStatus
import com.we.player.utils.TimeStrUtils
import java.util.*

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午10:57
 */
class PlayControlView : FrameLayout, IViewItemController {
    var controlWrapper: WrapController?=null
    var bottom_container:LinearLayout?= null
    var fullscreen:ImageView?= null
    var iv_play:ImageView?= null
    var curr_time:TextView?= null
    var seekBar:SeekBar?= null
    var total_time:TextView?= null
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_controller_play, this, true)
         bottom_container= this.findViewById<LinearLayout>(R.id.bottom_container)
         fullscreen= this.findViewById<ImageView>(R.id.fullscreen)
         iv_play= this.findViewById<ImageView>(R.id.iv_play)
        curr_time= this.findViewById<TextView>(R.id.curr_time)
         seekBar= this.findViewById<SeekBar>(R.id.seekBar)
         total_time= this.findViewById<TextView>(R.id.total_time)
    }

    override fun attach(controlWrapper: WrapController) {
        this.controlWrapper=controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {


    }

    override fun onPlayStateChanged(playState: Int) {
        when(playState){
            PlayStatus.STATE_PLAYING->{
                controlWrapper?.iViewController?.startProgress()
            }
            PlayStatus.STATE_PAUSED->{
                controlWrapper?.iViewController?.stopProgress()
            }
        }

    }

    override fun onPlayerStateChanged(playerState: Int) {

    }

    override fun setProgress(duration: Long?, position: Long?) {
        duration?.let {
            curr_time?.setText(TimeStrUtils.stringForTime(duration))
        }
    }

    override fun onLockStateChanged(isLocked: Boolean) {

    }


}