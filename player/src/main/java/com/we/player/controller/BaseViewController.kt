package com.we.player.controller

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import com.blankj.utilcode.util.LogUtils
import com.we.player.helper.OrientationEventListenerHelper
import com.we.player.helper.OrientationEventListenerM
import com.we.player.utils.PlayerUtils
import com.we.player.view.MediaPlayerController

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:55
 */
abstract class BaseViewController : FrameLayout, IViewController, OrientationEventListenerM {
    val TAG: String = "BaseViewController"
    var activity: Activity? = null
    var mOrientation: Int = 0

    var orientationEventListener: OrientationEventListenerHelper? = null
    var iviewItemControllers: MutableList<IViewItemController> = arrayListOf()
    var IGestureViewItemControllers: MutableList<IGestureViewItemController> = arrayListOf()
    var fadeout: Runnable = object : Runnable {
        override fun run() {
            hideController()
        }
    }
    var runProgress: Runnable = object : Runnable {
        override fun run() {
            LogUtils.d(TAG, "runProgress!!! ${iviewItemControllers.size} ")

            iviewItemControllers.forEach {
                if (mediaPlayerController != null) {
//                    LogUtils.d(TAG, "runProgress ${mediaPlayerController!!.getDuration()}, ${mediaPlayerController!!.getCurrentPosition()}")
                    it.setProgress(mediaPlayerController!!.getDuration(), mediaPlayerController!!.getCurrentPosition())
                }
            }
            if (mediaPlayerController!!.isPlaying()) {
                postDelayed(this, mediaPlayerController!!.getRefreshTime())
            }
        }
    }

    private val mShowAnim: Animation by lazy {
        var show = AlphaAnimation(0f, 1f)
        show.duration = 300
        show
    }

    private val mHideAnim: Animation by lazy {
        var hide = AlphaAnimation(1f, 0f)
        hide.duration = 300
        hide
    }
    var islock: Boolean = false
    var isRunProgress: Boolean = false
    var isShowControl: Boolean = true
    var mediaPlayerController: MediaPlayerController? = null
        set(value) {
            field = value
            if (value != null) {
                this.iviewItemControllers.forEach {
                    it.attach(value, this)
                }
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true)
        orientationEventListener = OrientationEventListenerHelper(getContext().applicationContext,
                SensorManager.SENSOR_DELAY_NORMAL)
        orientationEventListener?.orientationEventListener = this
        orientationEventListener?.enable()
        activity = PlayerUtils.scanForActivity(context)

    }

    override fun onOrientationChanged(orientation: Int) {
//        LogUtils.d(TAG, "onOrientationChanged $orientation")
        if (activity == null) {
            return
        }
        if (activity?.isFinishing()!!) {
            return
        }
        if (!mediaPlayerController?.isFullScreen()!! && !mediaPlayerController?.isPlaying()!!) {
            return
        }

        //记录用户手机上一次放置的位置
        val lastOrientation: Int = mOrientation

        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            //手机平放时，检测不到有效的角度
            //重置为原始位置 -1
            mOrientation = -1
            return
        }

        if (orientation > 350 || orientation < 10) {
            var o: Int = activity?.requestedOrientation
                    ?: ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            //手动切换横竖屏
            if (o == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && lastOrientation == 0) return
            if (mOrientation == 0) return
            //0度，用户竖直拿着手机
            mOrientation = 0
            //竖屏
            LogUtils.d(TAG, "onOrientationChanged 竖屏竖屏竖屏竖屏竖屏$orientation")
            if (islock) {
                return
            }
            mediaPlayerController?.stopFullScreen()
        } else if (orientation > 80 && orientation < 100) {
            val o: Int = activity?.requestedOrientation
                    ?: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            //手动切换横竖屏
            if (o == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && lastOrientation == 90) return
            if (mOrientation == 90) return
            //90度，用户右侧横屏拿着手机
            mOrientation = 90
            //反向横屏
            LogUtils.d(TAG, "onOrientationChanged 反向横屏反向横屏反向横屏反向横屏$orientation")
            if (mediaPlayerController?.isFullScreen() == true) {
            } else {
                mediaPlayerController?.startFullScreen(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE)
            }

        } else if (orientation > 260 && orientation < 280) {
            val o: Int = activity?.requestedOrientation
                    ?: ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            //手动切换横竖屏
            if (o == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && lastOrientation == 270) return
            if (mOrientation == 270) return
            //270度，用户左侧横屏拿着手机
            mOrientation = 270
            //横屏
            LogUtils.d(TAG, "onOrientationChanged 横屏横屏横屏$orientation")
            if (mediaPlayerController?.isFullScreen() == true) {
            } else {
                mediaPlayerController?.startFullScreen(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            }
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LogUtils.d(TAG, " onOrientationChanged  onConfigurationChanged!!!! $newConfig")


    }

    override fun setPlayStatus(status: Int) {
        LogUtils.d(TAG, "setPlayStatus")
        this.iviewItemControllers.forEach {
            it.onPlayStateChanged(status)
        }
    }


    override fun setLocked(lock: Boolean) {
        this.islock = lock
        this.iviewItemControllers.forEach {
            it.onLockStateChanged(lock)
        }
        onLockStateChanged(lock)
        hideController()
    }

    open fun onLockStateChanged(isLocked: Boolean) {

    }


    override fun isLocked(): Boolean {
        return this.islock
    }


    override fun startProgress() {
        LogUtils.d(TAG, "startProgress")
        if (!isRunProgress) {
            isRunProgress = true
            post(runProgress)
        }
    }

    override fun stopProgress() {
        isRunProgress = false
        removeCallbacks(runProgress)
    }

    override fun startTimeFade() {
        stopTimeFade()
        postDelayed(fadeout, 3800)
    }

    override fun stopTimeFade() {
        removeCallbacks(fadeout)
    }

    override fun hideController() {
        if (isShowControl) {
            stopTimeFade()
            this.iviewItemControllers.forEach {
                it.onVisibilityChanged(false, mHideAnim)
            }
            onVisibilityChanged(false, mHideAnim)
            isShowControl = false

        }
    }

    override fun showController() {
        if (!isShowControl) {
            this.iviewItemControllers.forEach {
                it.onVisibilityChanged(true, mShowAnim)
            }
            onVisibilityChanged(true, mShowAnim)
            startTimeFade()
            isShowControl = true
        }
    }

    open fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {

    }

    override fun isShowController(): Boolean {
        return isShowControl
    }

    override fun toggleControllerView() {
        if (isShowControl) {
            hideController()
        } else {
            showController()
        }
    }

    override fun hasCutout(): Boolean {
        return false
    }

    override fun getCutoutHeight(): Int {
        return 0
    }

    fun addIViewItemControllerList(isRemoveAll: Boolean, itemControllerlist: MutableList<IViewItemController>) {
        if (isRemoveAll) {
            this.iviewItemControllers.forEach {
                removeView(it.getView())
            }
            this.iviewItemControllers.clear()
            this.IGestureViewItemControllers.clear()
        }
        this.iviewItemControllers.addAll(itemControllerlist)
        this.iviewItemControllers.forEach {
            addView(it.getView())
            it.attach(mediaPlayerController, this)
            if (it is IGestureViewItemController) {
                IGestureViewItemControllers.add(it)
            }
        }
    }

    fun addIViewItemControllerOne(iviewItemController: IViewItemController,isPrivate:Boolean) {
        this.iviewItemControllers.add(iviewItemController)
        removeView(iviewItemController.getView())
        if(!isPrivate){
            addView(iviewItemController.getView())
        }
        iviewItemController.attach(mediaPlayerController, this)
        if (iviewItemController is IGestureViewItemController) {
            IGestureViewItemControllers.add(iviewItemController)
        }
    }

    fun addIViewItemControllerOne(iviewItemController: IViewItemController) {
        addIViewItemControllerOne(iviewItemController,false)
    }

    override fun onBackPressed(): Boolean {
        return false
    }

}