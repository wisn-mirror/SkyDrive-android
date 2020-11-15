package com.we.player.player

/**
 *
 * @Description: 播放器的各种状态
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午11:05
 */
object PlayStatus {

    /**
     *  播放错误
     */
    const val STATE_ERROR = -1

    /**
     *  闲置状态
     */
    const val STATE_IDLE = 0

    /**
     *  准备中
     */
    const val STATE_PREPARING = 1

    /**
     * 准备好
     */
    const val STATE_PREPARED = 2

    /**
     *  播放中
     */
    const val STATE_PLAYING = 3

    /**
     *  暂停
     */
    const val STATE_PAUSED = 4

    /**
     *  播放完成
     */
    const val STATE_PLAYBACK_COMPLETED = 5

    /**
     *  缓冲中
     */
    const val STATE_BUFFERING = 6

    /**
     *  缓冲完成
     */
    const val STATE_BUFFERED = 7

    /**
     *  开始播放中止
     */
    const val STATE_START_ABORT = 8

}