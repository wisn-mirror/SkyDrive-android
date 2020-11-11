package com.library.base.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.library.base.BaseApp
import com.library.base.config.GlobalUser
import com.library.base.event.Message
import com.library.base.net.ExceptionHandle
import com.library.base.net.ResponseThrowable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class BaseViewModel : AndroidViewModel(Utils.getApp()), LifecycleObserver {
    val defUi: UIChange by lazy { UIChange() }
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }

    fun <T> launchGo(
            block: suspend CoroutineScope.() -> IBaseResponse<T>,
            error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
                defUi.toastEvent.postValue("${it.code}: ${it.errMsg}")
            },
            complete: suspend CoroutineScope.() -> Unit = {},
            isShowDialog: Boolean = true
    ) {
        if (isShowDialog) defUi.disDialog.call()
        launchUI {
            coroutineScope {
                try {
                    withContext(Dispatchers.IO) { block }().let {
                        if (it.isExpire()) {
                            GlobalUser.clearToken()
                            LogUtils.d("HTTP_UNAUTHORIZED2")
                            // 登录页面
                            BaseApp.app.loginEvent()
                        }
                    }
                } catch (e: Throwable) {
                    error(ExceptionHandle.handleException(e))
                } finally {
                    complete()
                }
            }
        }
    }

    fun launchGoLo(
            block: suspend CoroutineScope.() -> Unit,
            error: suspend CoroutineScope.(ResponseThrowable) -> Unit = {
                defUi.toastEvent.postValue("${it.code}: ${it.errMsg}")
            },
            complete: suspend CoroutineScope.() -> Unit = {},
            isShowDialog: Boolean = true
    ) {
        if (isShowDialog) defUi.disDialog.call()
        launchUI {
            coroutineScope {
                try {
                    withContext(Dispatchers.IO) { block }()
                } catch (e: Throwable) {
                    error(ExceptionHandle.handleException(e))
                } finally {
                    if (isShowDialog) defUi.disDialog.call()
                    complete()
                }
            }
        }
    }
/*
    fun <T> launchOnlyResult(
            block: suspend CoroutineScope.() -> IBaseResponse<T>,
            success: (T) -> Unit,
            error: (ResponseThrowable) -> Unit = {
                defUi.toastEvent.postValue("${it.code}: ${it.errMsg}")
            },
            complete: suspend CoroutineScope.() -> Unit = {},
            isShowDialog: Boolean = true

    ) {
        if (isShowDialog) defUi.disDialog.call()
        launchUI {
            handleException({
                withContext(Dispatchers.IO) {
                    block().let {
                        if (it.isSuccess()) {
                            it.data()
                        } else throw  ResponseThrowable(it.code(), it.msg())
                    }.also {
                        success(it)
                    }
                }
            }, {
                error(it)
            }, {
                defUi.disDialog.call()
                complete()
            })
        }

    }*/

    class UIChange {
        val showDialog by lazy { SingleLiveEvent<String>() }
        val disDialog by lazy { SingleLiveEvent<Void>() }
        val toastEvent by lazy { SingleLiveEvent<String>() }
        val msgEvent by lazy { SingleLiveEvent<Message>() }
    }

}