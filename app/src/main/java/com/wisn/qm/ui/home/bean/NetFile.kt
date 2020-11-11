package com.wisn.qm.ui.home.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by Wisn on 2020/6/6 下午6:15.
 */
data class NetFile(val position:Int,override var itemType: Int=0) : MultiItemEntity {

    companion object {
        const val ImageViewItem = 0
        const val TimeTitle = 1
    }

}