package com.we.player.render

import android.graphics.Bitmap
import android.view.View
import com.we.player.player.APlayer

/**
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:47
 */
interface IRenderView {

    /**
     * 设置视频比例
     */
    fun setScreenScaleType(screenScaleType: Int)

    /**
     * 关联最终的播放核心
     */
    fun attachToPlayer(player: APlayer)

    /**
     * 设置视频宽高
     */
    fun setVideoSize(width: Int, height: Int)

    /**
     * 设置视频旋转角度
     */
    fun setVideoRotation(degree: Int)

    /**
     * 获取播放View
     */
    fun getRenderView(): View

    /**
     * 视频截图
     */
    fun getScreenShot(): Bitmap?

    /**
     * 释放资源
     */
    fun release()


}