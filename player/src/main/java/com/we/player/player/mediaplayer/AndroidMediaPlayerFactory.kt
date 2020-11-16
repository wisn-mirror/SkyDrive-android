package com.we.player.player.exo

import android.app.Application
import com.we.player.player.PlayerFactory
import com.we.player.player.mediaplayer.AndroidMediaPlayer

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/14 上午8:54
 */
open class AndroidMediaPlayerFactory : PlayerFactory<AndroidMediaPlayer>() {

    override fun createPlayer(app: Application): AndroidMediaPlayer {
       return AndroidMediaPlayer(app)
    }
}