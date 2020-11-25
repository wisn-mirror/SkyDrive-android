package com.we.player.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.os.Build
import android.util.TypedValue
import android.view.*
import com.library.base.BaseApp

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
    fun getDecorView(context: Context?): ViewGroup? {
        return getDecorView(scanForActivity(context))
    }

    /**
     * dp转为px
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics).toInt()
    }

    /**
     * sp转为px
     */
    fun sp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dpValue, context.resources.displayMetrics).toInt()
    }

    /**
     * 获取DecorView
     */
    fun getDecorViewByActivity(activity: Activity?): ViewGroup? {
        val activity: Activity = activity ?: return null
        return activity.window.decorView as ViewGroup
    }

    /**
     * 获取NavigationBar的高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        if (! hasNavigationBar(context)) {
            return 0
        }
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android")
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     */
    fun getWindowManager(context: Context): WindowManager {
        return context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    /**
     * 是否存在NavigationBar
     */
    fun hasNavigationBar(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val display: Display =  getWindowManager(context).getDefaultDisplay()
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            realSize.x != size.x || realSize.y != size.y
        } else {
            val menu = ViewConfiguration.get(context).hasPermanentMenuKey()
            val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            !(menu || back)
        }
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(): Int {
        val resourceId: Int = BaseApp.app.resources.getIdentifier("status_bar_height", "dimen", "android")
        return BaseApp.app.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context, isIncludeNav: Boolean): Int {
        return if (isIncludeNav) {
            context.resources.displayMetrics.widthPixels +  getNavigationBarHeight(context)
        } else {
            context.resources.displayMetrics.widthPixels
        }
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(context: Context, isIncludeNav: Boolean): Int {
        return if (isIncludeNav) {
            context.resources.displayMetrics.heightPixels +  getNavigationBarHeight(context)
        } else {
            context.resources.displayMetrics.heightPixels
        }
    }


    /**
     * 边缘检测
     */
    fun isEdge(context: Context, e: MotionEvent): Boolean {
        val edgeSize: Int =  dp2px(context, 40f)
        return e.rawX < edgeSize || e.rawX >getScreenWidth(context, true) - edgeSize || e.rawY < edgeSize || e.rawY >  getScreenHeight(context, true) - edgeSize
    }


}