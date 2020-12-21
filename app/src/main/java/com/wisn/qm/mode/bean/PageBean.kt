package com.wisn.qm.mode.bean

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/21 上午10:43
 */
data class PageBean<T>(
        val nextpageid: Int?,
        val pageNo: Int?,
        val pageSize: Int?,
        val total: Int?,
        val list: T
)