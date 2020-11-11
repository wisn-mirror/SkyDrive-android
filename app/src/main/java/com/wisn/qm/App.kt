package com.wisn.qm

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.library.base.BaseApp
import com.wisn.qm.task.UploadTaskUitls
import com.wisn.qm.ui.SplashActivity
import io.github.skyhacker2.sqliteonweb.SQLiteOnWeb

open class App:BaseApp(){
    override fun onCreate() {
        super.onCreate()
        SQLiteOnWeb.init(this).start(); //webSqlite
    }

   override fun loginEvent(){
         ActivityUtils.finishAllActivities()
       LogUtils.d("App","loginEvent")
//       val intentOf = QMUIFragmentActivity.intentOf(this, MainActivity::class.java, LoginFragment::class.java)
       val intentOf = Intent(this, SplashActivity::class.java)
       intentOf.flags=FLAG_ACTIVITY_NEW_TASK
       startActivity(intentOf)
   }
}