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
        TODO("Not yet implemented")
    }

    override fun showController() {
        TODO("Not yet implemented")
    }

    override fun setLocked(isLock: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isLocked(): Boolean {
        TODO("Not yet implemented")
    }

}