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

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午10:57
 */
class TitleControlView : FrameLayout, IViewItemController, View.OnClickListener {

    var TAG: String? = "TitleControlView"
    var controlWrapper: WrapController? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.item_controller_title, this, true)
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

    }

    override fun onPlayerStateChanged(playerState: Int) {

    }

    override fun setProgress(duration: Long?, position: Long?) {


    }

    override fun onLockStateChanged(isLocked: Boolean) {}

    override fun onClick(p0: View?) {

    }

}