package com.we.player.controller

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import com.we.player.view.MediaPlayerController

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:55
 */
abstract class BaseViewController : FrameLayout, IViewController {
    var iviewItemController: MutableList<IViewItemController> = arrayListOf()
    var fadeout: Runnable = object : Runnable {
        override fun run() {
            hideController()
        }
    }

    private val mShowAnim: Animation by lazy {
        var show = AlphaAnimation(0f, 1f)
        show.duration = 300
        show
    }

    private val mHideAnim: Animation by lazy {
        var show = AlphaAnimation(1f, 0f)
        show.duration = 300
        show
    }
    var wrapController: WrapController? = null
    var mediaPlayerController: MediaPlayerController? = null
        set(value) {
            field = value
            if (value != null) {
                wrapController = WrapController(value, this)
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true)
    }

    override fun setPlayStatus(status: Int) {
        this.iviewItemController.forEach {
             it.onPlayStateChanged(status )
        }
    }

    override fun hideController() {

    }

    override fun showController() {

    }

    override fun setLocked(isLock: Boolean) {
    }

    override fun isLocked(): Boolean {
        return false
    }


    override fun startProgress() {

    }

    override fun stopProgress() {

    }

    override fun startTimeFade() {
        stopTimeFade()
        postDelayed(fadeout, 3800)
    }

    override fun stopTimeFade() {
        removeCallbacks(fadeout)
    }

    override fun hasCutout(): Boolean {
        return false
    }

    override fun getCutoutHeight(): Int {
        return 0
    }

    fun addIViewItemControllerList(isRemoveAll: Boolean, itemControllerlist: MutableList<IViewItemController>) {
        if (isRemoveAll) {
            this.iviewItemController.forEach {
                removeView(it.getView())
            }
            this.iviewItemController.clear()
        }
        this.iviewItemController.addAll(itemControllerlist)
        this.iviewItemController.forEach {
            addView(it.getView())
        }
    }

    fun addIViewItemControllerOne(iviewItemController: IViewItemController) {
        this.iviewItemController.add(iviewItemController)
        removeView(iviewItemController.getView())
        addView(iviewItemController.getView())
    }

}