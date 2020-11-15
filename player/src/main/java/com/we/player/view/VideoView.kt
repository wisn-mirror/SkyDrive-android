package com.we.player.view

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.library.base.BaseApp
import com.we.player.*
import com.we.player.controller.BaseViewController
import com.we.player.controller.IViewItemController
import com.we.player.player.PlayerEventListener

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午8:33
 */
class VideoView : FrameLayout, MediaPlayerController, PlayerEventListener {

    constructor(context: Context):this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0)
    constructor(context: Context,attributeSet: AttributeSet?,defStyleAttr:Int): super(context,attributeSet,defStyleAttr){
    }


    protected var mVideoSize = intArrayOf(0, 0)


    var mediaPlayer: PlayerFactory<APlayer>? = null
    var mIRenderView: IRenderView? = null
    var iViewController: BaseViewController? = null


    var mAPlayer: APlayer? = null
    val mPlayerContainer: FrameLayout? by lazy {
        var mPlayerContainer = FrameLayout(getContext())
        VideoView@this.addView(mPlayerContainer, LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
        mPlayerContainer
    }

    fun setIViewControllerView(viewcontroller: BaseViewController){
        iViewController?.let {
            mPlayerContainer?.removeView(iViewController)
        }
        this.iViewController=viewcontroller
        mPlayerContainer?.addView(iViewController,  LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun addDisplay() {
        if (mIRenderView == null || mAPlayer == null) {
            return
        }
        mPlayerContainer?.removeView(mIRenderView!!.getRenderView())
        mIRenderView!!.attachToPlayer(mAPlayer!!)
        mPlayerContainer?.addView(mIRenderView!!.getRenderView(), 0, LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
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
    }
//////////////////播放器相关动作/////////////////////////////

    override fun start() {
        mAPlayer = mediaPlayer?.createPlayer(BaseApp.app)
        mAPlayer?.mPlayerEventListener = this
        mAPlayer?.initPlayer()
        mAPlayer?.start()
        addDisplay()
        startPrepare(false)
    }

    override fun pause() {
        mAPlayer?.pause()
    }

    override fun getDuration(): Long {
        return 0
    }

    override fun getCurrentPosition(): Long {
//       mAPlayer?.cu
        return 0
    }

    override fun seekTo(pos: Long) {
        mAPlayer?.seekTo(pos)
    }

    override fun isPlaying(): Boolean {
//        return mAPlayer?.is
        return true
    }

    override fun getBufferedPercentage(): Int {
        return 0
    }

    override fun startFullScreen() {
    }

    override fun stopFullScreen() {
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun setMute(isMute: Boolean) {
    }

    override fun isMute(): Boolean {
        return false
    }

    override fun setScreenScaleType(screenScaleType: Int) {
    }

    override fun setSpeed(speed: Float) {
    }

    override fun getSpeed(): Float {
        return 0f
    }

    override fun getTcpSpeed(): Long {
        return 0
    }

    override fun replay(resetPosition: Boolean) {
    }

    override fun setMirrorRotation(enable: Boolean) {

    }

    override fun doScreenShot(): Bitmap? {
        return null
    }

    override fun getVideoSize(): IntArray? {
        return mVideoSize
    }


/////////////////播放器的回调//////////////////////////

    override fun onPlayerEventError() {
    }

    override fun onPlayerEventCompletion() {
    }

    override fun onPlayerEventInfo(what: Int, extra: Int) {
    }

    override fun onPlayerEventPrepared() {
    }

    override fun onPlayerEventVideoSizeChanged(videoWidth: Int, videoHeight: Int) {
        mVideoSize[0] = videoWidth
        mVideoSize[1] = videoHeight

//        mIRenderView?.setScaleType(mCurrentScreenScaleType)
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