package com.we.player.player

/**
 *
 * @Description: 播放器的各种状态
 * @Author: Wisn
 * @CreateDate: 2020/11/14 下午11:05
 */
object PlayStatusStr {

    fun getStatusStr(status:Int):String{
        when(status){
            PlayStatus.STATE_ERROR->{
               return STATE_ERROR
            }
            PlayStatus.STATE_IDLE->{
                return STATE_IDLE
            }
            PlayStatus.STATE_PREPARING->{
                return STATE_PREPARING
            }
            PlayStatus.STATE_PREPARED->{
                return STATE_PREPARED
            }
            PlayStatus.STATE_PLAYING->{
                return STATE_PLAYING
            }
            PlayStatus.STATE_PAUSED->{
                return STATE_PAUSED
            }
            PlayStatus.STATE_PLAYBACK_COMPLETED->{
                return STATE_PLAYBACK_COMPLETED
            }
            PlayStatus.STATE_BUFFERING->{
                return STATE_BUFFERING
            }
            PlayStatus.STATE_BUFFERED->{
                return STATE_BUFFERED
            }
            PlayStatus.STATE_START_ABORT->{
                return STATE_START_ABORT
            }
            PlayStatus.PLAYER_NORMAL->{
                return PLAYER_NORMAL
            }
            PlayStatus.PLAYER_FULL_SCREEN->{
                return PLAYER_FULL_SCREEN
            }
            PlayStatus.PLAYER_TINY_SCREEN->{
                return PLAYER_TINY_SCREEN
            }
        }
        return "未知"
    }

    /**
     *  播放错误
     */
    const val STATE_ERROR = "播放错误"

    /**
     *  闲置状态
     */
    const val STATE_IDLE = "闲置状态"

    /**
     *  准备中
     */
    const val STATE_PREPARING = "准备中"

    /**
     * 准备好
     */
    const val STATE_PREPARED = "准备好"

    /**
     *  播放中
     */
    const val STATE_PLAYING = "播放中"

    /**
     *  暂停
     */
    const val STATE_PAUSED = "暂停"

    /**
     *  播放完成
     */
    const val STATE_PLAYBACK_COMPLETED = "播放完成"

    /**
     *  缓冲中
     */
    const val STATE_BUFFERING = "缓冲中"

    /**
     *  缓冲完成
     */
    const val STATE_BUFFERED = "缓冲完成"

    /**
     *  开始播放中止
     */
    const val STATE_START_ABORT = "开始播放中止"

    /**
     * 普通播放器
     */
    const val PLAYER_NORMAL = "普通播放器"

    /**
     * 全屏播放器
     */
    const val PLAYER_FULL_SCREEN = "全屏播放器"

    /**
     * 小屏播放器
     */
    const val PLAYER_TINY_SCREEN = "小屏播放器"

/////////////////////////////////////////////////////////


}