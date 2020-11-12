package com.we.player.render

import android.content.Context
import android.graphics.Bitmap
import android.view.TextureView
import android.view.View
import com.we.player.APlayer
import com.we.player.IRenderView

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:53
 */
class TextureRenderView(context: Context) : TextureView(context), IRenderView {
    override fun attachToPlayer(player: APlayer) {
        TODO("Not yet implemented")
    }

    override fun setVideoSize(width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun setVideoRotation(degree: Int) {
        TODO("Not yet implemented")
    }

    override fun getRenderView(): View {
        TODO("Not yet implemented")
    }

    override fun getScreenShot(): Bitmap {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }
}