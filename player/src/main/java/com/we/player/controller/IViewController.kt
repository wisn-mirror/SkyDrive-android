package com.we.player.controller


/**
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:47
 */
interface IViewController {
    /**
     * 同步设置状态
     */
    fun setPlayStatus(status: Int)

    /**
     * 隐藏控制栏
     */
    fun hideController()

    /**
     * 显示控制栏
     */
    fun showController()

    /**
     * 设置是否锁定控制view
     */
    fun setLocked(isLock: Boolean)

    /**
     * 是否锁定状态
     */
    fun isLocked(): Boolean

    fun getLayoutId(): Int

    /**
     * 开始刷新进度
     */
    fun startProgress()

    /**
     * 停止刷新进度
     */
    fun stopProgress()

    /**
     * 开始自动隐藏状态栏
     */
    fun startTimeFade()

    /**
     * 取消自动隐藏状态栏
     */
    fun stopTimeFade()

    /**
     * 是否需要适配刘海
     */
    fun hasCutout(): Boolean

    /**
     * 获取刘海的高度
     */
    fun getCutoutHeight(): Int


}