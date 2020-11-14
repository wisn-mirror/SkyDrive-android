package com.we.player

import android.view.Surface
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
    abstract fun setSurface(surface: Surface?)

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
    abstract fun setVolume(v1: Float, v2: Float);


}