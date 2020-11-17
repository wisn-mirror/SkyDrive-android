package com.we.player.player.exo

import android.app.Application
import android.os.Handler
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.video.VideoListener
import com.we.player.player.APlayer
import com.we.player.player.PlayStatus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:52
 */
class ExoAPlayer(var app: Application) : APlayer(), Player.EventListener, VideoListener {
    val TAG: String = "ExoAPlayer"
    var mMediaSource: MediaSource? = null
    var simpleExoPlayer: SimpleExoPlayer? = null
    val newInstance = ExoSourceHelper.newInstance(app)
    var parameters: PlaybackParameters? = null
    private var mLastReportedPlaybackState = PlayStatus.STATE_IDLE
    private var mLastReportedPlayWhenReady = false
    private var mIsPreparing = false
    private var mIsBuffering = false

    private val mMediaSourceEventListener: MediaSourceEventListener = object : MediaSourceEventListener {
        fun onReadingStarted(windowIndex: Int, mediaPeriodId: MediaPeriodId?) {
            mPlayerEventListener?.onPlayerEventPrepared()
        }
    }

    override fun initPlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(
                app.applicationContext,
                DefaultRenderersFactory(app)
        ).build()

        simpleExoPlayer?.addListener(this)
        simpleExoPlayer?.addVideoListener(this)
    }

    override fun setDataSource(path: String?, headers: Map<String, String>?) {
        mMediaSource = newInstance.getMediaSource(path!!, headers)
    }

    override fun setSurface(surface: Surface?) {
        simpleExoPlayer?.setVideoSurface(surface)
    }

    override fun setDisplay(holder: SurfaceHolder) {
        super.setDisplay(holder)
        if (holder == null) {
            setSurface(null)
        } else {
            setSurface(holder!!.surface)
        }
    }

    override fun prepareAsync() {
        if (mMediaSource == null) {
            return
        }
        simpleExoPlayer?.setMediaSource(mMediaSource!!)
        mMediaSource!!.addEventListener(Handler(), mMediaSourceEventListener)
        simpleExoPlayer?.prepare()
    }

    override fun start() {
        simpleExoPlayer?.playWhenReady = true
    }

    override fun pause() {
        simpleExoPlayer?.playWhenReady = false
    }

    override fun stop() {
        simpleExoPlayer?.stop()
    }

    override fun reset() {
        simpleExoPlayer?.stop()
        simpleExoPlayer?.setVideoSurface(null)
    }

    override fun seekTo(seekto: Long) {
        simpleExoPlayer?.seekTo(seekto)
    }

    override fun release() {
        simpleExoPlayer?.let {
            GlobalScope.launch {
                simpleExoPlayer?.release()
            }
        }
    }

    override fun setSpeed(speed: Float) {
        this.parameters = PlaybackParameters(speed)
        simpleExoPlayer?.setPlaybackParameters(parameters)
    }

    override fun setVolume(v1: Float, v2: Float) {
        simpleExoPlayer?.volume = (v1 + v2) / 2;
    }

    override fun geSpeed(): Float {
        return if (this.parameters != null) this.parameters!!.speed else 1f
    }


    override fun setLooping(isLooping: Boolean) {
        simpleExoPlayer?.repeatMode = if (isLooping) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
    }

    override fun getBufferedPercentage(): Int {
        return simpleExoPlayer?.bufferedPercentage!!
    }

    override fun getDuration(): Long {
        return simpleExoPlayer?.duration!!
    }

    override fun getCurrentPosition(): Long {
        return simpleExoPlayer?.currentPosition!!
    }

    override fun isPlaying(): Boolean {
        if (simpleExoPlayer == null) {
            return false
        } else {
            return when (simpleExoPlayer!!.playbackState) {
                Player.STATE_BUFFERING, Player.STATE_READY -> simpleExoPlayer!!.playWhenReady
                Player.STATE_ENDED, Player.STATE_IDLE -> false
                else -> false
            }
        }
    }

    override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
        super.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio)
        mPlayerEventListener?.onPlayerEventVideoSizeChanged(width, height)
        if (unappliedRotationDegrees > 0) {
            mPlayerEventListener?.onPlayerEventInfo(PlayStatus.MEDIA_INFO_VIDEO_ROTATION_CHANGED, unappliedRotationDegrees)
        }
    }

    override fun onSurfaceSizeChanged(width: Int, height: Int) {
        super.onSurfaceSizeChanged(width, height)
        Log.d(TAG, "onSurfaceSizeChanged width$width height$height")
    }

    override fun onRenderedFirstFrame() {
        super.onRenderedFirstFrame()
        if (mPlayerEventListener != null && mIsPreparing) {
            mPlayerEventListener?.onPlayerEventInfo(PlayStatus.MEDIA_INFO_VIDEO_RENDERING_START, 0)
            mIsPreparing = false
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (mIsPreparing) return
        if (mLastReportedPlayWhenReady != playWhenReady || mLastReportedPlaybackState != playbackState) {
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    mPlayerEventListener?.onPlayerEventInfo(PlayStatus.MEDIA_INFO_BUFFERING_START, getBufferedPercentage())
                    mIsBuffering = true
                }
                Player.STATE_READY -> if (mIsBuffering) {
                    mPlayerEventListener?.onPlayerEventInfo(PlayStatus.MEDIA_INFO_BUFFERING_END, getBufferedPercentage())
                    mIsBuffering = false
                }
                Player.STATE_ENDED -> mPlayerEventListener?.onPlayerEventCompletion()
            }
            mLastReportedPlaybackState = playbackState
            mLastReportedPlayWhenReady = playWhenReady
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        mPlayerEventListener?.onPlayerEventError()

    }
}