package com.we.player.controller

import android.content.Context
import android.widget.FrameLayout
import com.we.player.IViewController

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:55
 */
class BaseViewController(context: Context) : FrameLayout(context), IViewController {
    override fun hideController() {
    }

    override fun showController() {
    }

    override fun setLocked(isLock: Boolean) {
    }

    override fun isLocked(): Boolean {
        return false
    }

}