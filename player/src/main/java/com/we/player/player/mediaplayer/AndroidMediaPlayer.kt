package com.we.player.player.mediaplayer

import android.app.Application
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.Surface
import android.view.SurfaceHolder
import com.we.player.player.APlayer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/15 下午10:19
 */
class AndroidMediaPlayer(var app: Application) : APlayer() {
    var mediaPlayer: MediaPlayer? = null
    private var mIsPreparing = false
    private var mBufferedPercent = 0

    override fun initPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer?.setOnErrorListener(onErrorListener)
        mediaPlayer?.setOnCompletionListener(onCompletionListener)
        mediaPlayer?.setOnInfoListener(onInfoListener)
        mediaPlayer?.setOnBufferingUpdateListener(onBufferingUpdateListener)
        mediaPlayer?.setOnPreparedListener(onPreparedListener)
        mediaPlayer?.setOnVideoSizeChangedListener(onVideoSizeChangedListener)
    }

    override fun setDataSource(path: String?, headers: Map<String, String>?) {
        try {
            mediaPlayer?.setDataSource(app, Uri.parse(path), headers)
        } catch (e: Exception) {
            mPlayerEventListener?.onPlayerEventError()
        }
    }

    override fun prepareAsync() {
        try {
            mIsPreparing = true
            mediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            mPlayerEventListener?.onPlayerEventError()
        }
    }

    override fun start() {
        try {
            mediaPlayer?.start()
        } catch (e: Exception) {
            mPlayerEventListener?.onPlayerEventError()
        }
    }

    override fun pause() {
        try {
            mediaPlayer?.pause()
        } catch (e: Exception) {
            mPlayerEventListener?.onPlayerEventError()
        }
    }

    override fun stop() {
        try {
            mediaPlayer?.stop()
        } catch (e: Exception) {
            mPlayerEventListener?.onPlayerEventError()
        }
    }

    override fun reset() {
        try {
            mediaPlayer?.reset()
            mediaPlayer?.setSurface(null)
            mediaPlayer?.setDisplay(null)
        } catch (e: Exception) {
            mPlayerEventListener?.onPlayerEventError()
        }
    }

    override fun seekTo(seekto: Long) {
        try {
            mediaPlayer?.seekTo(seekto.toInt())
        } catch (e: Exception) {
            mPlayerEventListener?.onPlayerEventError()
        }
    }

    override fun setLooping(isLooping: Boolean) {
        try {
            mediaPlayer?.isLooping = isLooping
        } catch (e: Exception) {
            mPlayerEventListener?.onPlayerEventError()
        }
    }

    override fun release() {
        mediaPlayer?.setOnErrorListener(null)
        mediaPlayer?.setOnCompletionListener(null)
        mediaPlayer?.setOnInfoListener(null)
        mediaPlayer?.setOnBufferingUpdateListener(null)
        mediaPlayer?.setOnPreparedListener(null)
        mediaPlayer?.setOnVideoSizeChangedListener(null)
        GlobalScope.launch {
            try {
                mediaPlayer?.release()
            } catch (e: Exception) {

            }
        }
    }

    override fun setDisplay(holder: SurfaceHolder) {
        super.setDisplay(holder)
        mediaPlayer?.setDisplay(holder)
    }

    override fun setSurface(surface: Surface?) {
        mediaPlayer?.setSurface(surface)
    }

    override fun setSpeed(speed: Float) {
        // only support above Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val speed1 = mediaPlayer?.playbackParams?.setSpeed(speed)
                speed1?.let {
                    mediaPlayer?.playbackParams
                }
            } catch (e: java.lang.Exception) {
                mPlayerEventListener?.onPlayerEventError()
            }
        }
    }

    override fun setVolume(v1: Float, v2: Float) {
        mediaPlayer?.setVolume(v1, v2)
    }

    override fun geSpeed(): Float {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return mediaPlayer?.playbackParams?.speed!!
            } catch (e: java.lang.Exception) {
                mPlayerEventListener?.onPlayerEventError()
            }
        }
        return 1f
    }

    override fun getBufferedPercentage(): Int {
        return mBufferedPercent
    }

    override fun getDuration(): Long {
        return mediaPlayer?.duration!!.toLong()
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition!!.toLong()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying!!
    }

//////////////////////////////////

    private val onErrorListener = MediaPlayer.OnErrorListener { mp, what, extra ->
        mPlayerEventListener?.onPlayerEventError()
        true
    }

    private val onCompletionListener = MediaPlayer.OnCompletionListener { mPlayerEventListener?.onPlayerEventCompletion() }

    private val onInfoListener = MediaPlayer.OnInfoListener { mp, what, extra -> //解决MEDIA_INFO_VIDEO_RENDERING_START多次回调问题
        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            if (mIsPreparing) {
                mPlayerEventListener?.onPlayerEventInfo(what, extra)
                mIsPreparing = false
            }
        } else {
            mPlayerEventListener?.onPlayerEventInfo(what, extra)
        }
        true
    }

    private val onBufferingUpdateListener = MediaPlayer.OnBufferingUpdateListener { mp, percent -> mBufferedPercent = percent }


    private val onPreparedListener = MediaPlayer.OnPreparedListener {
        mPlayerEventListener?.onPlayerEventPrepared()
        start()
    }

    private val onVideoSizeChangedListener = MediaPlayer.OnVideoSizeChangedListener { mp, width, height ->
        val videoWidth = mp.videoWidth
        val videoHeight = mp.videoHeight
        if (videoWidth != 0 && videoHeight != 0) {
            mPlayerEventListener?.onPlayerEventVideoSizeChanged(videoWidth, videoHeight)
        }
    }
}