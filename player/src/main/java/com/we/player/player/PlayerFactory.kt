package com.we.player.player

import android.app.Application

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 上午8:55
 */
abstract class PlayerFactory<out APlayer>  {

    abstract fun createPlayer(app: Application):APlayer
}