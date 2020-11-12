package com.we.player


/**
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:47
 */
interface IViewController {

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
    fun setLocked(isLock:Boolean)

    /**
     * 是否锁定状态
     */
    fun isLocked():Boolean


}