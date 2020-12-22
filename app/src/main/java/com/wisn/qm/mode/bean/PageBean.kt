package com.wisn.qm.mode.bean

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/21 上午10:43
 */
data class PageBean<T>(
        val nextpageid: Long?,
        val pageNo: Long?,
        val pageSize: Long?,
        val total: Long?,
        val list: T
)