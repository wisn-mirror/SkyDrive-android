package com.wisn.qm.ui.selectpic

import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.qmuiteam.qmui.kotlin.onClick
import com.wisn.qm.R
import com.wisn.qm.databinding.RvItemPictureImageBinding
import com.wisn.qm.databinding.RvItemPictureTitleBinding
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.ui.home.BaseDataBindlingViewHolder
import java.io.File

/**
 * Created by Wisn on 2020/6/6 下午6:14.
 */

class SelectPictureAdapter(selectPictureCallBack: SelectPictureCallBack?) : BaseMultiItemQuickAdapter<MediaInfo, BaseDataBindlingViewHolder>() {
    protected var selectPictureCallBack: SelectPictureCallBack

    init {
        addItemType(FileType.TimeTitle, R.layout.rv_item_picture_title)
        addItemType(FileType.ImageViewItem, R.layout.rv_item_picture_image)
        this.selectPictureCallBack = selectPictureCallBack!!
    }

    fun updateSelect(isSelectModel: Boolean?) {
        isSelectModel?.let {
            selectPictureCallBack.changeSelectData(false, null);
        }
//        selectPosition = -1
        notifyDataSetChanged()
    }

    /**
     * （可选重写）当 item 的 ViewHolder创建完毕后，执行此方法。
     * 可在此对 ViewHolder 进行处理，例如进行 DataBinding 绑定 view
     *
     * @param viewHolder VH
     * @param viewType Int
     */
    override fun onItemViewHolderCreated(viewHolder: BaseDataBindlingViewHolder, viewType: Int) {
        super.onItemViewHolderCreated(viewHolder, viewType)
        if (viewType == FileType.TimeTitle) {
            viewHolder.setDataBinding<RvItemPictureImageBinding>(viewHolder.itemView)
//            viewHolder.dataBinding = DataBindingUtil.bind<RvItemPictureImageBinding>(viewHolder.itemView)
        } else if (viewType == FileType.ImageViewItem) {
            viewHolder.setDataBinding<RvItemPictureTitleBinding>(viewHolder.itemView)
//            viewHolder.dataBinding = DataBindingUtil.bind<RvItemPictureTitleBinding>(viewHolder.itemView)
        }
    }


    override fun convert(holder: BaseDataBindlingViewHolder, item: MediaInfo) {
        val adapterPosition = holder.adapterPosition;
        if (item.itemType == FileType.TimeTitle) {
//         val  binding  =holder.dataBinding as? RvItemPictureImageBinding
            val dataBinding = holder.getDataBinding<RvItemPictureTitleBinding>()
//            dataBinding?.tvTitle?.text = item.na.toString()

        } else {
            val dataBinding = holder.getDataBinding<RvItemPictureImageBinding>()
            LogUtils.d(item.filePath)
            dataBinding?.image?.let {
                Glide.with(context).load(File(item.filePath!!))
                        .apply(RequestOptions())
                        .into(it)
                dataBinding.image.onClick {
                    item.isSelect = !item.isSelect
                    notifyItemChanged(adapterPosition)
                    selectPictureCallBack.changeSelectData(item.isSelect, item);

                }
                dataBinding.ivSelect.visibility = View.VISIBLE
                if (item.isSelect) {
                    dataBinding.ivSelect.setBackgroundResource(R.mipmap.ic_image_selected)
                } else {
                    dataBinding.ivSelect.setBackgroundResource(R.mipmap.ic_image_unselected)
                }

            }
            if (item.isVideo!!) {
                dataBinding?.videoTime?.visibility = View.VISIBLE
                dataBinding?.videoTime?.setText(item.timestr)
            } else {
                dataBinding?.videoTime?.visibility = View.GONE
            }

        }
    }


}

