package com.we.player.helper

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import com.we.player.view.VideoView
import java.lang.ref.WeakReference

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午11:16
 */
class AudioFocusHelper : AudioManager.OnAudioFocusChangeListener{
    private val mHandler = Handler(Looper.getMainLooper())

//    private var mWeakVideoView: WeakReference<VideoView>? = null

    private var mAudioManager: AudioManager? = null

    private var mStartRequested = false
    private var mPausedForLoss = false
    private var mCurrentFocus = 0

  /*  fun AudioFocusHelper(videoView: VideoView) {
        mWeakVideoView = WeakReference<VideoView>(videoView)
        mAudioManager = videoView.getContext().getApplicationContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager?
    }
*/
    override fun onAudioFocusChange(focusChange: Int) {
        if (mCurrentFocus == focusChange) {
            return
        }

        //由于onAudioFocusChange有可能在子线程调用，
        //故通过此方式切换到主线程去执行
        mHandler.post { handleAudioFocusChange(focusChange) }
        mCurrentFocus = focusChange
    }

    private fun handleAudioFocusChange(focusChange: Int) {
       /* val videoView: VideoView = mWeakVideoView!!.get() ?: return
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> {
                if (mStartRequested || mPausedForLoss) {
                    videoView.start()
                    mStartRequested = false
                    mPausedForLoss = false
                }
                if (!videoView.isMute()) //恢复音量
                    videoView.setVolume(1.0f, 1.0f)
            }
            AudioManager.AUDIOFOCUS_LOSS, AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> if (videoView.isPlaying()) {
                mPausedForLoss = true
                videoView.pause()
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> if (videoView.isPlaying() && !videoView.isMute()) {
                videoView.setVolume(0.1f, 0.1f)
            }
        }*/
    }

    /**
     * Requests to obtain the audio focus
     */
    fun requestFocus() {
        if (mCurrentFocus == AudioManager.AUDIOFOCUS_GAIN) {
            return
        }
        if (mAudioManager == null) {
            return
        }
        val status = mAudioManager!!.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == status) {
            mCurrentFocus = AudioManager.AUDIOFOCUS_GAIN
            return
        }
        mStartRequested = true
    }

    /**
     * Requests the system to drop the audio focus
     */
    fun abandonFocus() {
        if (mAudioManager == null) {
            return
        }
        mStartRequested = false
        mAudioManager!!.abandonAudioFocus(this)
    }
}