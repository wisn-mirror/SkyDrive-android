package com.we.player.view

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.blankj.utilcode.util.LogUtils
import com.library.base.BaseApp
import com.we.player.controller.BaseViewController
import com.we.player.player.*
import com.we.player.render.IRenderView
import com.we.player.utils.PlayerUtils

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午8:33
 */
class VideoView : FrameLayout, MediaPlayerController, PlayerEventListener {
    var TAG: String = "VideoView"
    var isLoop: Boolean = false
    var isFull: Boolean = false
    var refreshPregressTime: Long = 1000
    var decodeView: ViewGroup? = null
    var activity: Activity? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {}

    protected var mVideoSize = intArrayOf(0, 0)

    var currentState: Int? = PlayStatus.STATE_IDLE
    var mediaPlayer: PlayerFactory<APlayer>? = null
    var mIRenderView: IRenderView? = null

    var iViewController: BaseViewController? = null
        set(value) {
            field = value
            mPlayerContainer?.removeView(value)
            mPlayerContainer?.addView(field, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT))
            iViewController?.mediaPlayerController = this
        }

    var isMute: Boolean = false
        set(value) {
            field = value
            mAPlayer?.setVolume(0f, 0f)
        }

    var mCurrentScreenScaleType: Int? = ScreenConfig.SCREEN_SCALE_DEFAULT
        set(value) {
            field = value
            mIRenderView?.setScreenScaleType(mCurrentScreenScaleType!!)
        }


    var mAPlayer: APlayer? = null
    val mPlayerContainer: FrameLayout? by lazy {
        var mPlayerContainer = FrameLayout(getContext())
        VideoView@ this.addView(mPlayerContainer, LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
        mPlayerContainer
    }


    fun addDisplay() {
        if (mIRenderView == null || mAPlayer == null) {
            return
        }
        mPlayerContainer?.removeView(mIRenderView!!.getRenderView())
        mIRenderView!!.attachToPlayer(mAPlayer!!)
        mPlayerContainer?.addView(mIRenderView!!.getRenderView(), 0, LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER))
    }

    fun startPrepare(reset: Boolean) {
        if (reset) {
            mAPlayer?.reset()
            //重新设置参数
        }
        mUrl?.let {
            mAPlayer?.setDataSource(mUrl, mHeaders)
        }
        mAPlayer?.prepareAsync()
        setPlayStatus(PlayStatus.STATE_PREPARING)
    }

//////////////////播放器相关动作/////////////////////////////

    override fun start() {
        if (PlayStatus.isPlayingStatus(currentState)) {
            mAPlayer?.start()
            setPlayStatus(PlayStatus.STATE_PLAYING)
        } else {
            //todo 检查网络，是否提示
            mAPlayer = mediaPlayer?.createPlayer(BaseApp.app)
            mAPlayer?.mPlayerEventListener = this
            mAPlayer?.initPlayer()
            addDisplay()
            startPrepare(false)
            mAPlayer?.setLooping(isLoop)
        }
    }

    override fun pause() {
        mAPlayer?.pause()
        setPlayStatus(PlayStatus.STATE_PAUSED)

    }

    override fun getDuration(): Long {
        return mAPlayer?.getDuration()!!
    }

    override fun getCurrentPosition(): Long {
        return mAPlayer?.getCurrentPosition()!!
    }

    override fun getRefreshTime(): Long {
        return refreshPregressTime;
    }

    override fun seekTo(pos: Long) {
        mAPlayer?.seekTo(pos)
    }

    override fun isPlaying(): Boolean {
        return mAPlayer?.isPlaying()!!
    }

    override fun getBufferedPercentage(): Int {
        return mAPlayer?.getBufferedPercentage()!!
    }

    override fun setSpeed(speed: Float) {
        refreshPregressTime = (1000 / speed).toLong()
        mAPlayer?.setSpeed(speed)
    }

    override fun getSpeed(): Float {
        return mAPlayer?.geSpeed()!!
    }

    override fun setLooping(looping: Boolean) {
        isLoop = looping
        mAPlayer?.setLooping(looping)
    }


    override fun getTcpSpeed(): Long {
        return 0
    }

    override fun togglePlay() {
        val playing = mAPlayer?.isPlaying()
        if (playing == null || !playing) {
            mAPlayer?.start()
        } else {
            mAPlayer?.pause()
        }
    }

    override fun replay(resetPosition: Boolean) {

    }

    /**
     * 只能支持TextureRenderView,不支持surfaceview
     */
    override fun setMirrorRotation(enable: Boolean) {
        mIRenderView?.getRenderView()?.scaleX = if (enable) -1f else 1f;
    }

    /**
     * 截屏
     */
    override fun doScreenShot(): Bitmap? {
        return mIRenderView?.getScreenShot()
    }

    /**
     * 获取视频尺寸
     */
    override fun getVideoSize(): IntArray? {
        return mVideoSize
    }

    /**
     * 设置旋转角度
     */
    override fun setVideoRotation(rotation: Int) {
        mIRenderView?.setVideoRotation(rotation)
        this.rotation = rotation.toFloat()
    }


    override fun startFullScreen() {
        setPlayStatus(PlayStatus.PLAYER_FULL_SCREEN)
        activity = PlayerUtils.scanForActivity(context)
        decodeView = PlayerUtils.getDecorViewByActivity(activity)
        decodeView?.let {
            this.removeView(mPlayerContainer)
            decodeView?.addView(mPlayerContainer, LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT))
            activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            isFull = true
//            activity?.window?.setFlags(
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun stopFullScreen() {
        setPlayStatus(PlayStatus.PLAYER_NORMAL)
        decodeView?.let {
            decodeView?.removeView(mPlayerContainer)
            this.addView(mPlayerContainer)
            activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            isFull = false
        }

    }

    override fun isFullScreen(): Boolean {
        return isFull
    }

    override fun startTinyScreen() {
        setPlayStatus(PlayStatus.PLAYER_TINY_SCREEN)
    }

    override fun stopTinyScreen() {
        setPlayStatus(PlayStatus.PLAYER_NORMAL)

    }

    override fun isTinyScreen(): Boolean {
        return false
    }

    /////////////////播放器的回调//////////////////////////
    fun setPlayStatus(status: Int) {
        var msg = PlayStatusStr.getStatusStr(status);
        LogUtils.d(TAG, "状态 ：$msg")
        currentState = status
        iViewController?.setPlayStatus(status)
    }


    override fun onPlayerEventError() {
        setPlayStatus(PlayStatus.STATE_ERROR)
    }

    override fun onPlayerException(message: String?) {
        LogUtils.d(TAG, "onPlayerException ：$message")
    }

    override fun onPlayerEventCompletion() {
        setPlayStatus(PlayStatus.STATE_PLAYBACK_COMPLETED)
    }

    override fun onPlayerEventInfo(what: Int, extra: Int) {
        when (what) {
            PlayStatus.MEDIA_INFO_VIDEO_RENDERING_START -> {
                /**
                 * 开始渲染视频画面
                 */
                setPlayStatus(PlayStatus.STATE_PLAYING)
            }
            PlayStatus.MEDIA_INFO_BUFFERING_START -> {
                /**
                 * 缓冲开始
                 */
                setPlayStatus(PlayStatus.STATE_BUFFERING)
            }
            PlayStatus.MEDIA_INFO_BUFFERING_END -> {
                /**
                 * 缓冲结束
                 */
                setPlayStatus(PlayStatus.STATE_BUFFERED)
            }
            PlayStatus.MEDIA_INFO_VIDEO_ROTATION_CHANGED -> {
                /**
                 * 视频旋转信息
                 */
                mIRenderView?.setVideoRotation(extra)
            }
        }
    }

    override fun onPlayerEventPrepared() {
        setPlayStatus(PlayStatus.STATE_PREPARED)
    }


    override fun onPlayerEventVideoSizeChanged(videoWidth: Int, videoHeight: Int) {
        mVideoSize[0] = videoWidth
        mVideoSize[1] = videoHeight
        mIRenderView?.setScreenScaleType(mCurrentScreenScaleType!!)
        mIRenderView?.setVideoSize(videoWidth, videoHeight)
    }

//////////////////设置播放地址////////////////////////////////

    protected var mUrl //当前播放视频的地址
            : String? = null
    protected var mHeaders //当前视频地址的请求头
            : Map<String, String>? = null
    protected var mAssetFileDescriptor //assets文件
            : AssetFileDescriptor? = null

    /**
     * 设置视频地址
     */
    fun setUrl(url: String) {
        setUrl(url, null)
    }

    /**
     * 设置包含请求头信息的视频地址
     *
     * @param url     视频地址
     * @param headers 请求头
     */
    open fun setUrl(url: String, headers: Map<String, String>?) {
        mAssetFileDescriptor = null
        mUrl = url
        mHeaders = headers
    }


}