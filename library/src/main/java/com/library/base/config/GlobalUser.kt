package com.library.base.config

import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils

object GlobalUser {
    var token: String? = null
    var userinfo: UserBean? = null

    fun initData() {
        token = SPUtils.getInstance().getString(Constant.TokenKey)
        val userstr = SPUtils.getInstance().getString(Constant.UserInfo)
        userinfo = GsonUtils.fromJson(userstr, UserBean::class.java)
    }

    fun saveToken(token: String) {
        if (token.isNotEmpty()) {
            SPUtils.getInstance().put(Constant.TokenKey, token)
            this.token = token
        }
    }

    fun saveUserInfo(userBean: UserBean) {
        val toJson = GsonUtils.toJson(userBean)
        SPUtils.getInstance().put(Constant.UserInfo, toJson)
        this.userinfo = userBean
    }

    fun updateUserName(username:String){
        this.userinfo?.user_name=username
        saveUserInfo(this.userinfo!!)
    }

    fun clearToken() {
        SPUtils.getInstance().put(Constant.TokenKey, "")
        SPUtils.getInstance().put(Constant.UserInfo, "")
        this.token = null
        this.userinfo = null
    }
}