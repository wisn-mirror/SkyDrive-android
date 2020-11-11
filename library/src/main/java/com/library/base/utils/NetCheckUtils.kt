package com.library.base.utils

import com.blankj.utilcode.util.LogUtils
import com.library.base.config.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object NetCheckUtils {
    const val check = "user/checknet"
    suspend fun isConnect(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            var result: Boolean = false
            try {
                LogUtils.d("NetCheckUtils", url)
                // 统一资源
                val url = URL(url)
                // 连接类的父类，抽象类
                val urlConnection = url.openConnection()
                // http的连接类
                val httpURLConnection = urlConnection as HttpURLConnection
                // 设定请求的方法，默认是GET
                httpURLConnection.requestMethod = "GET"
                httpURLConnection.connectTimeout = 1000
                httpURLConnection.readTimeout = 1000
                // 设置字符编码
                httpURLConnection.setRequestProperty("Charset", "UTF-8")
                // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
                httpURLConnection.connect()
                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    result = true
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            result
        }
    }

    /**
     * 检查当前选中的ip 是否可用
     */
    suspend fun isConnectByIp(ip: String): Boolean {
        return isConnect(Constant.getBaseTemp(ip) + check)
    }

    /**
     * 初始化检查上一个ip 是否可用
     */
    suspend fun isConnectCheckInit(): Boolean {
        return isConnect(Constant.BASE_URL + check)
    }

}