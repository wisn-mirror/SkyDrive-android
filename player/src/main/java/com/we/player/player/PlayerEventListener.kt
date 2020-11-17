package com.we.player.player;

/**
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 上午9:06
 */
interface PlayerEventListener {
    fun onPlayerEventError();

    fun onPlayerException(message: String?);

    fun onPlayerEventCompletion();

    fun onPlayerEventInfo(what: Int, extra: Int);

    fun onPlayerEventPrepared();

    fun onPlayerEventVideoSizeChanged(width: Int, height: Int);
}
