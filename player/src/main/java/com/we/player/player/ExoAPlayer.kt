package com.we.player.player

import android.app.Application
import android.os.Handler
import android.view.Surface
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSource.MediaPeriodId
import com.google.android.exoplayer2.source.MediaSourceEventListener
import com.google.android.exoplayer2.video.VideoListener
import com.we.player.APlayer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:52
 */
class ExoAPlayer(var app: Application) : APlayer(), Player.EventListener, VideoListener {
    var mMediaSource: MediaSource? = null
    var simpleExoPlayer: SimpleExoPlayer? = null
    val newInstance = ExoSourceHelper.newInstance(app)

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

    override fun prepareAsync() {
        if (mMediaSource == null) {
            return
        }
        simpleExoPlayer?.setMediaSource(mMediaSource!!)
        mMediaSource!!.addEventListener(Handler(),mMediaSourceEventListener)
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
        simpleExoPlayer?.setPlaybackParameters(PlaybackParameters(speed))
    }

    override fun setVolume(v1: Float, v2: Float) {
        simpleExoPlayer?.volume = (v1 + v2) / 2;
    }

    override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {
        super.onVideoSizeChanged(width, height, unappliedRotationDegrees, pixelWidthHeightRatio)
    }

    override fun onSurfaceSizeChanged(width: Int, height: Int) {
        super.onSurfaceSizeChanged(width, height)
    }

    override fun onRenderedFirstFrame() {
        super.onRenderedFirstFrame()
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
    }

    override fun onPlaybackStateChanged(state: Int) {
        super.onPlaybackStateChanged(state)
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
    }
}