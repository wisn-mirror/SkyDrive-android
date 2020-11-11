package com.library.base.net.inteceptor

import com.library.base.config.GlobalUser
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthInterceptor : Interceptor {

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
           return chain.proceed(chain.request().newBuilder().run {
                GlobalUser.token?.let {
                    addHeader("token", GlobalUser.token)
                }
                build()
            })
        } catch (e: Exception) {
            throw e
        }
    }

}