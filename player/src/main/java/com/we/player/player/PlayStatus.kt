package com.we.player.player

/**
 *
 * @Description: 播放器的各种状态
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午11:05
 */
object PlayStatus {


    fun isPlayingStatus(currentState:Int?): Boolean {
        when(currentState){
            STATE_ERROR,STATE_IDLE,STATE_START_ABORT,STATE_PREPARING,STATE_PLAYBACK_COMPLETED->{
                return false
            }
        }
        return true
    }


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
     *  开始播放中止,4G环境流量提醒，中止播放
     */
    const val STATE_START_ABORT = 8

    /**
     * 普通播放器
     */
    const val PLAYER_NORMAL = 10

    /**
     * 全屏播放器
     */
    const val PLAYER_FULL_SCREEN = 11

    /**
     * 小屏播放器
     */
    const val PLAYER_TINY_SCREEN = 12

/////////////////////////////////////////////////////////

    /**
     * 开始渲染视频画面
     */
    const val MEDIA_INFO_VIDEO_RENDERING_START = 3

    /**
     * 缓冲开始
     */
    const val MEDIA_INFO_BUFFERING_START = 701

    /**
     * 缓冲结束
     */
    const val MEDIA_INFO_BUFFERING_END = 702

    /**
     * 视频旋转信息
     */
    const val MEDIA_INFO_VIDEO_ROTATION_CHANGED = 10001


}