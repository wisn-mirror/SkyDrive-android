package com.wisn.qm.ui.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.library.base.utils.ImageAdapter
import com.wisn.qm.R
import com.wisn.qm.databinding.RvItemAlbumBinding
import com.wisn.qm.mode.db.beans.UserDirBean

/**
 * Created by Wisn on 2020/6/6 下午6:14.
 */

class AlbumAdapter : BaseQuickAdapter<UserDirBean, BaseDataBindingHolder<RvItemAlbumBinding>>(R.layout.rv_item_album) {

    override fun convert(holder: BaseDataBindingHolder<RvItemAlbumBinding>, item: UserDirBean) {
        holder.dataBinding?.itemData = item
        holder.dataBinding?.executePendingBindings()
        ImageAdapter.setImageSha1(holder.dataBinding!!.ivHeader,item.sha1_pre)

    }

}