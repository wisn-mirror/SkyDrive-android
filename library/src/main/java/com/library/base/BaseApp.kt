package com.library.base
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.Utils
import com.library.base.config.Constant
import com.library.base.config.GlobalUser
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager

open class BaseApp : MultiDexApplication() {

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