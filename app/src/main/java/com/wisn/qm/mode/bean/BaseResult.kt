package com.wisn.qm.mode.bean

import com.library.base.base.IBaseResponse

data class BaseResult<T>(
        val code: Int,
        val message: String,
        val data: T

) : IBaseResponse<T?> {
    override fun code(): Int {
       return code
    }

    override fun msg(): String {
     return message
    }

    override fun data(): T{
       return data
    }

    override fun isSuccess(): Boolean {
       return code==100
    }

    override fun isExpire(): Boolean {
      return code== -99
    }

    fun isUploadSuccess(): Boolean {
        return code==100||code==101
    }
}