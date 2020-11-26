package com.we.player.render.impl

import android.content.Context
import com.we.player.render.RenderViewFactory
import com.we.player.render.SurfaceRenderView
import com.we.player.render.TextureRenderView

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/26 下午1:55
 */
class SurfaceRenderViewFactory : RenderViewFactory<SurfaceRenderView>() {
    override fun createIRenderView(context: Context): SurfaceRenderView {
        return SurfaceRenderView(context)
    }
}