package com.library.base.net

import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.library.base.BaseApp
import com.library.base.config.GlobalUser
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownServiceException
import java.text.ParseException

object ExceptionHandle {

    fun handleException(e: Throwable): ResponseThrowable {
        val ex: ResponseThrowable
        if (e is ResponseThrowable) {
            ex = e
        } else if (e is JsonParseException || e is ParseException || e is MalformedJsonException) {
            ex = ResponseThrowable(ERROR.PARSE_ERROR, e)
        } else if (e is UnknownServiceException ) {
            ex = ResponseThrowable(ERROR.NETWORD_ERROR, e)
        } else if (e is java.lang.IllegalArgumentException ) {
            ex = ResponseThrowable(ERROR.API_ERROR, e)
        } else if (e is SocketTimeoutException) {
            ex = ResponseThrowable(ERROR.HTTP_TimeERROR, e)
        } else if (e is ConnectException) {
            ex = ResponseThrowable(ERROR.HTTP_ConnectERROR, e)
        } else if (e is HttpException ) {
            LogUtils.d("code :"+e.code()+e.message())
            if (e.code() == 401 || e.code() == 403) {
                LogUtils.d("HTTP_UNAUTHORIZED222")
                // 登录页面
                GlobalUser.clearToken()
                BaseApp.app.loginEvent()
                ex = ResponseThrowable(ERROR.HTTP_Unauthorized, e)

            } else {
                ex = ResponseThrowable(ERROR.HTTP_ERROR, e)

            }
        } else {
            ex = ResponseThrowable(ERROR.UNKNOW, e)
        }
        e.printStackTrace()
        return ex
    }
}