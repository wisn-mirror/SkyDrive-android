package com.we.player

/**
 *
 * @Description: 播放器最终实现
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:51
 */
abstract class APlayer {

    /**
     * 初始化播放器
     */
    abstract fun initPlayer()

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