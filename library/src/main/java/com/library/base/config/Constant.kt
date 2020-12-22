package com.library.base.config
import com.blankj.utilcode.util.SPUtils


object Constant {
    const val TAGBASE_URL = "BASE_URL"
    const val TokenKey = "Token"
    const val id = "Id"
    const val UserInfo = "user"

    //    const val BASE_URL = "http://10.0.2.2:9996/"
    var BASE_URL = "http://192.168.0.100:9996/"

    fun getImageUrl(sha1: String): String? {
        if (sha1.isNullOrEmpty()) {
            return null;
        }
        return "${BASE_URL}file/open?filesha1=${sha1}&token=${GlobalUser.token}"
    }

    fun getImageDownloadUrl(sha1: String): String {
        return "${BASE_URL}file/getdownload?filesha1=${sha1}&token=${GlobalUser.token}"
    }


    fun initBaseUrl() {
        BASE_URL = SPUtils.getInstance().getString(TAGBASE_URL)
    }


    fun setBaseUrl(ip: String): String {
        ip?.let {
            BASE_URL = getBaseTemp(ip)
            SPUtils.getInstance().put(TAGBASE_URL, BASE_URL)
        }
        return BASE_URL
    }

    fun getBaseTemp(ip: String): String {
        return "http://$ip:9996/";
    }
}