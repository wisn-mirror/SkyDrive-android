package com.we.player.controller

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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
    val TAG:String="BaseViewController"
    var iviewItemController: MutableList<IViewItemController> = arrayListOf()
    var fadeout: Runnable = object : Runnable {
        override fun run() {
            hideController()
        }
    }
    var runProgress: Runnable = object : Runnable {
        override fun run() {
            iviewItemController.forEach {
                if (mediaPlayerController != null) {
                    it.setProgress(mediaPlayerController!!.getDuration(), mediaPlayerController!!.getCurrentPosition())
                    if (mediaPlayerController!!.isPlaying()) {
                        postDelayed(this, mediaPlayerController!!.getRefreshTime())
                    }
                }
            }
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
    var islock: Boolean = false
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
            it.onPlayStateChanged(status)
        }
    }

    override fun hideController() {
        this.iviewItemController.forEach {
            it.onVisibilityChanged(false, mHideAnim)
        }
    }

    override fun showController() {
        this.iviewItemController.forEach {
            it.onVisibilityChanged(true, mShowAnim)
        }
    }

    override fun setLocked(isLock: Boolean) {
        this.islock = islock
        this.iviewItemController.forEach {
            it.onLockStateChanged(isLock)
        }
    }

    override fun isLocked(): Boolean {
        return islock
    }


    override fun startProgress() {
        Log.d(TAG,"startProgress")
        post(runProgress)
    }

    override fun stopProgress() {
        removeCallbacks(runProgress)
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