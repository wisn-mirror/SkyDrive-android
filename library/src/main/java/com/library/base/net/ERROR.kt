package com.library.base.net

enum class ERROR(private val code: Int, private val err: String) {

    UNKNOW(1000, "未知错误"),
    PARSE_ERROR(1001, "解析错误"),
    NETWORD_ERROR(1002, "网络错误"),
    API_ERROR(1003, "发起请求异常"),
    HTTP_ERROR(1004, "接口异常"),
    HTTP_TimeERROR(1005, "接口连接超时"),
    HTTP_ConnectERROR(1006, "网路连接错误"),
    HTTP_Unauthorized(1007, "认证过期");

    fun getValue(): String {
        return err
    }

    fun getCode(): Int {
        return code
    }


}