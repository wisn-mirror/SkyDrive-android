package com.library.base.base

import com.library.base.net.ResponseThrowable

abstract class BaseModel {
    suspend fun <T> cachNetCall(
            remote: suspend () -> IBaseResponse<T>,
            local: suspend () -> T?,
            save: suspend (T) -> Unit,
            isUseCache: (T?) -> Boolean = { true }
    ): T {
        val localData = local.invoke()
        if (isUseCache(localData)) return localData!!
        else {
            val net = remote()
            if (net.isSuccess()) {
                return net.data()!!.also { save(it) }
            }
            throw ResponseThrowable(net)
        }

    }

    fun onCleared() {
    }

}