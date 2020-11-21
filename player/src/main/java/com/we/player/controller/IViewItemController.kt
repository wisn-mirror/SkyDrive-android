package com.we.player.controller

import android.view.View
import android.view.animation.Animation
import com.we.player.view.MediaPlayerController

/**
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:47
 *
 */
interface IViewItemController {
    fun attach( mediaPlayerController: MediaPlayerController?,  iViewController: IViewController)

    fun getView(): View

    fun onVisibilityChanged(isVisible: Boolean, anim: Animation?)

    fun onPlayStateChanged(playState: Int)

    fun onPlayerStateChanged(playerState: Int)

    fun setProgress(duration: Long?, position: Long?)

    fun onLockStateChanged(isLocked: Boolean)
}