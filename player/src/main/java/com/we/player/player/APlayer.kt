package com.we.player.player

import android.view.Surface
import android.view.SurfaceHolder
import com.we.player.player.PlayerEventListener

/**
 *
 * @Description: 播放器最终实现
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:51
 */
abstract class APlayer {


    var mPlayerEventListener: PlayerEventListener? = null

    /**
     * 初始化播放器
     */
    abstract fun initPlayer()

    /**
     * 设置播放地址
     *
     * @param path    播放地址
     * @param headers 播放地址请求头
     */
    abstract fun setDataSource(path: String?, headers: Map<String, String>?)

    /**
     * 设置渲染视频的View,主要用于TextureView
     */
    open fun setSurface(surface: Surface?) {}

    /**
     * 设置渲染视频的View,主要用于SurfaceView
     */
    open fun setDisplay(holder: SurfaceHolder) {}

    /**
     * 准备开始播放（异步）
     */
    abstract fun prepareAsync()

    /**
     * 开始播放
     */
    abstract fun start()

    /**
     * 暂停
     */
    abstract fun pause()

    /**
     * 停止
     */
    abstract fun stop()

    /**
     * 重置
     */
    abstract fun reset()

    /**
     * 指定进度
     */
    abstract fun seekTo(seekto: Long)

    /**
     * 是否循环播放
     */
    abstract fun setLooping(isLooping: Boolean)

    /**
     * 释放
     */
    abstract fun release()

    /**
     * 指定速度播放
     */
    abstract fun setSpeed(speed: Float)

    /**
     * 设置声音
     */
    abstract fun setVolume(v1: Float, v2: Float)


    /**
     * 当前播放速度
     */
    abstract fun geSpeed(): Float

    /**
     * 获取缓冲百分比
     */
    abstract fun getBufferedPercentage(): Int

    /**
     * 视频总时长
     */
    abstract fun getDuration(): Long

    /**
     * 获取当前播放位置
     */
    abstract fun getCurrentPosition(): Long

    /**
     * 当前是否播放状态
     */
    abstract fun isPlaying(): Boolean


}