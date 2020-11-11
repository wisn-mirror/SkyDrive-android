package com.wisn.qm.ui.home.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by Wisn on 2020/6/6 下午6:15.
 */
class Share(override var itemType: Int) : MultiItemEntity {

    companion object {
        const val ImageViewItem = 0
        const val VideoItem = 1
    }

}