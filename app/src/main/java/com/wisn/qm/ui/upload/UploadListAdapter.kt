package com.wisn.qm.ui.upload

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.wisn.qm.R
import com.wisn.qm.databinding.RvItemUploadlistBinding
import com.wisn.qm.mode.db.beans.UploadBean
import java.io.File

/**
 * Created by Wisn on 2020/6/6 下午6:14.
 */

class UploadListAdapter : BaseQuickAdapter<UploadBean, BaseDataBindingHolder<RvItemUploadlistBinding>>(R.layout.rv_item_uploadlist) {

    override fun convert(holder: BaseDataBindingHolder<RvItemUploadlistBinding>, item: UploadBean) {
        holder.dataBinding?.itemData = item
        holder.dataBinding?.executePendingBindings()
        Glide.with(context).load(File(item.filePath!!))
                .apply(RequestOptions())
                .into(holder.dataBinding!!.ivHeader)
    }

}