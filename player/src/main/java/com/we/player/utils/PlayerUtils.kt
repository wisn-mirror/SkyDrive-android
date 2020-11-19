package com.we.player.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.ViewGroup

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/19 上午10:47
 */
object PlayerUtils {

    /**
     * 获取Activity
     */
    fun scanForActivity(context: Context?): Activity? {
        if (context == null) return null
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return scanForActivity(context.baseContext)
        }
        return null
    }

    /**
     * 获取DecorView
     */
    fun getDecorView(context :Context?): ViewGroup? {
        return getDecorView(scanForActivity(context))
    }

    /**
     * 获取DecorView
     */
    fun getDecorViewByActivity(activity :Activity?): ViewGroup? {
        val activity: Activity = activity ?: return null
        return activity.window.decorView as ViewGroup
    }


}