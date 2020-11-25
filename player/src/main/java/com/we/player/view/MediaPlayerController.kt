package com.we.player.view

import android.graphics.Bitmap

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午11:03
 */
interface MediaPlayerController {
    fun start()

    fun pause()

    fun getDuration(): Long

    fun getCurrentPosition(): Long

    fun getRefreshTime(): Long

    fun seekTo(pos: Long)

    fun isPlaying(): Boolean

    fun getBufferedPercentage(): Int

    fun setSpeed(speed: Float)

    fun getSpeed(): Float

    fun setLooping(looping: Boolean)

    fun getTcpSpeed(): Long

    fun togglePlay()

    fun replay(resetPosition: Boolean)

    fun setMirrorRotation(enable: Boolean)

    fun doScreenShot(): Bitmap?

    fun getVideoSize(): IntArray?

    fun setVideoRotation(rotation: Int)


    fun startFullScreen(requestedOrientation: Int)

    fun stopFullScreen()

    fun isFullScreen(): Boolean


    fun startTinyScreen()

    fun stopTinyScreen()

    fun isTinyScreen(): Boolean

    fun onBackPressed(): Boolean


}