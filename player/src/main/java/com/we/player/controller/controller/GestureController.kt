package com.we.player.controller.controller

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.we.player.controller.BaseViewController
import com.we.player.player.PlayStatus
import com.we.player.utils.PlayerUtils

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/19 下午5:02
 */
abstract class GestureController : BaseViewController, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener, View.OnTouchListener {
    var TAGA: String = "GestureController"
    private val mGestureDetector: GestureDetector by lazy {
        GestureDetector(context, this)
    }
    private val mAudioManager: AudioManager by lazy {
        getContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private val mIsGestureEnabled = true
    private var mStreamVolume = 0
    private var mBrightness = 0f
    private var mSeekPosition = 0
    private var mFirstTouch = false
    private var mChangePosition = false
    private var mChangeBrightness = false
    private var mChangeVolume = false

    private val mCanChangePosition = true

    private val mEnableInNormal = false

    private var mCanSlide = false

    private var mCurPlayState = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        setOnTouchListener(this)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        if (mGestureDetector == null) {
            return false
        }
        return mGestureDetector!!.onTouchEvent(p1)
    }

    override fun setPlayStatus(status: Int) {
        super.setPlayStatus(status)
        if (status == PlayStatus.PLAYER_NORMAL) {
            mCanSlide = mEnableInNormal
        } else if (status == PlayStatus.PLAYER_FULL_SCREEN) {
            mCanSlide = true
        }
        this.mCurPlayState = status
    }

    /**
     * 在屏幕上滑动
     */
    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        if (!PlayStatus.isPlayingStatus(mCurPlayState) //不处于播放状态
                || !mIsGestureEnabled //关闭了手势
                || !mCanSlide //关闭了滑动手势
                || isLocked() //锁住了屏幕
                || PlayerUtils.isEdge(context, e1)) {//处于屏幕边沿
            LogUtils.d(TAGA, "：p1：" + e1)
            return true
        }

        val deltaX = e1.x - e2.x
        val deltaY = e1.y - e2.y
        if (mFirstTouch) {
            mChangePosition = Math.abs(distanceX) >= Math.abs(distanceY)
            if (!mChangePosition) {
                //半屏宽度
                val halfScreen: Int = PlayerUtils.getScreenWidth(context, true) / 2
                if (e2.x > halfScreen) {
                    mChangeVolume = true
//                    mChangeBrightness = false

                } else {
//                    mChangeVolume = false
                    mChangeBrightness = true
                }
            }
            if (mChangePosition) {
                //根据用户设置是否可以滑动调节进度来决定最终是否可以滑动调节进度
                mChangePosition = mCanChangePosition
            }
            if (mChangePosition || mChangeBrightness || mChangeVolume) {
                IGestureViewItemControllers?.forEach {
                    it.onGestureStartSlide()
                }
            }
            mFirstTouch = false
        }
        if (mChangePosition) {
            slideToChangePosition(deltaX)
        } else if (mChangeBrightness) {
            slideToChangeBrightness(deltaY)
        } else if (mChangeVolume) {
            slideToChangeVolume(deltaY)
        }
        return true
    }

    protected open fun slideToChangePosition(deltaX: Float) {
        val duration = mediaPlayerController?.getDuration() as Long
        val currentPosition = mediaPlayerController?.getCurrentPosition() as Long
        var position = (-deltaX / measuredWidth * 120000 + currentPosition).toInt()
        if (position > duration) {
            position = duration.toInt()
        }
        if (position < 0) {
            position = 0
        }
        IGestureViewItemControllers?.forEach {
            it.onGesturePositionChange(position, currentPosition, duration)
        }
        mSeekPosition = position
    }

    protected open fun slideToChangeBrightness(deltaY: Float) {
        val activity: Activity = PlayerUtils.scanForActivity(context) ?: return
        val attributes = activity.window.attributes
        if (mBrightness == -1.0f) mBrightness = 0.5f
        var brightness: Float = deltaY * 2 / measuredHeight * 1.0f + mBrightness
        if (brightness < 0) {
            brightness = 0f
        }
        if (brightness > 1.0f) brightness = 1.0f
        val percent = (brightness * 100).toInt()
        attributes.screenBrightness = brightness
        activity.window.attributes = attributes
        IGestureViewItemControllers?.forEach {
            it.onGestureBrightnessChange(percent)
        }
    }

    protected open fun slideToChangeVolume(deltaY: Float) {
        val streamMaxVolume: Int = mAudioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val deltaV = deltaY * 2 / measuredHeight * streamMaxVolume
        var index: Float = mStreamVolume + deltaV
        if (index > streamMaxVolume) index = streamMaxVolume.toFloat()
        if (index < 0) index = 0f
        val percent = (index / streamMaxVolume * 100).toInt()
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index.toInt(), 0)
        IGestureViewItemControllers?.forEach {
            it.onGestureVolumeChange(percent)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //滑动结束时事件处理
        if (!mGestureDetector.onTouchEvent(event)) {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    stopSlide()
                    if (mSeekPosition > 0) {
                        mediaPlayerController?.seekTo(mSeekPosition.toLong())
                        mSeekPosition = 0
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    stopSlide()
                    mSeekPosition = 0
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun stopSlide() {
        IGestureViewItemControllers?.forEach {
            it.onGestureStopSlide()
        }
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        p0?.let {
            if (!PlayStatus.isPlayingStatus(mCurPlayState) //不处于播放状态
                    || !mIsGestureEnabled //关闭了手势
                    || PlayerUtils.isEdge(context, p0)) {
                //处于屏幕边沿
                LogUtils.d(TAGA, "：p0：" + p0)
                return true
            }

            mStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val activity = PlayerUtils.scanForActivity(context)
            mBrightness = activity?.window?.attributes?.screenBrightness ?: 0f
            mFirstTouch = true
            mChangePosition = false
            mChangeBrightness = false
            mChangeVolume = false
        }
        return true
    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        LogUtils.d(TAGA, "：p3333：" + p0)
        if (PlayStatus.isPlayingStatus(mCurPlayState)) {
            toggleControllerView()
        }
        return true
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        p0?.let {
            if (!mIsGestureEnabled //关闭了手势
                    || !mCanSlide //关闭了滑动手势
                    || isLocked() //锁住了屏幕
                    || PlayerUtils.isEdge(context, p0)//处于屏幕边沿
                    || !mediaPlayerController!!.isFullScreen()) {
                //todo 显示锁定

            } else {
               mediaPlayerController?.togglePlay()

            }
        }
        return true
    }


    override fun onShowPress(p0: MotionEvent?) {
    }


    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }


    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        return false
    }


}