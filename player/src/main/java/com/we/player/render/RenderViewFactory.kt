package com.we.player.render

import android.content.Context

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/26 下午1:55
 */
abstract class RenderViewFactory<out IRenderView> {
    abstract fun createIRenderView(context: Context): IRenderView

}