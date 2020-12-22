package com.wisn.qm.mode.bean

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/22 上午11:56
 */
data class PageKey(
        val pid: Long,
        val lastId: Long?=-1,
        val pageNo: Long?=1,
        val pageSize: Long?=20)