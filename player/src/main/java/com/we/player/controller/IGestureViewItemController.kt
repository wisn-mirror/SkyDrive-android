package com.we.player.controller

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/19 下午8:28
 */
interface IGestureViewItemController : IViewItemController {
    /**
     * 开始滑动
     */
    fun onGestureStartSlide()

    /**
     * 结束滑动
     */
    fun onGestureStopSlide()

    /**
     * 滑动调整进度
     * @param slidePosition 滑动进度
     * @param currentPosition 当前播放进度
     * @param duration 视频总长度
     */
    fun onGesturePositionChange(slidePosition: Int, currentPosition: Long, duration: Long)

    /**
     * 滑动调整亮度
     * @param percent 亮度百分比
     */
    fun onGestureBrightnessChange(percent: Int)

    /**
     * 滑动调整音量
     * @param percent 音量百分比
     */
    fun onGestureVolumeChange(percent: Int)

}