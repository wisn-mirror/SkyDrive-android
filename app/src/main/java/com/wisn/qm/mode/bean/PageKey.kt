package com.wisn.qm.mode.bean

import android.util.Log

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/22 上午11:56
 */
data class PageKey(
        val pid: Long,
        val lastId: Long?,
        val pageNo: Long?=1,
        val pageSize: Long?=20)