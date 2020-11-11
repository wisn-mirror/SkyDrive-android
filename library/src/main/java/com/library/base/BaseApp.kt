package com.library.base

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.Utils
import com.library.base.config.Constant
import com.library.base.config.GlobalUser
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager

open class BaseApp : Application() {

    companion object {
         lateinit var app: BaseApp
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        app = this
        Utils.init(this)
        QMUISwipeBackActivityManager.init(this)
        GlobalUser.initData()
        Constant.initBaseUrl()
    }
    open fun loginEvent(){
    }
}