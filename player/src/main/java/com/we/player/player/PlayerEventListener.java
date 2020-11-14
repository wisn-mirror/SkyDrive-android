package com.we.player.player;

/**
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 上午9:06
 */
public interface PlayerEventListener {
    void onPlayerEventError();

    void onPlayerEventCompletion();

    void onPlayerEventInfo(int what, int extra);

    void onPlayerEventPrepared();

    void onPlayerEventVideoSizeChanged(int width, int height);
}
