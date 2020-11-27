package com.wisn.qm.ui.home.picture

import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.library.base.utils.GlideUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.wisn.qm.R
import com.wisn.qm.databinding.RvItemPictureImageBinding
import com.wisn.qm.databinding.RvItemPictureTitleBinding
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.ui.home.BaseDataBindlingViewHolder
import com.wisn.qm.ui.preview.PreviewFragment

/**
 * Created by Wisn on 2020/6/6 下午6:14.
 */

class PictureAdapterV2(pictureController: PictureCallBack?) : BaseMultiItemQuickAdapter<MediaInfo, BaseDataBindlingViewHolder>() {
    protected var pictureController: PictureCallBack
    open var isSelectModel: Boolean = false
    protected var map: HashMap<Long, Boolean> = HashMap()

    init {
        addItemType(FileType.TimeTitle, R.layout.rv_item_picture_title)
        addItemType(FileType.ImageViewItem, R.layout.rv_item_picture_image)
        addItemType(FileType.VideoViewItem, R.layout.rv_item_picture_image)
        this.pictureController = pictureController!!
    }

    fun updateSelect(isSelectModel: Boolean?) {
        isSelectModel?.let {
            this.isSelectModel = isSelectModel
            pictureController.changeSelectData(false, isSelectModel, false, null);
        }
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
        } else if (viewType == FileType.ImageViewItem||viewType == FileType.VideoViewItem) {
            viewHolder.setDataBinding<RvItemPictureTitleBinding>(viewHolder.itemView)
//            viewHolder.dataBinding = DataBindingUtil.bind<RvItemPictureTitleBinding>(viewHolder.itemView)
        }
    }


    override fun convert(holder: BaseDataBindlingViewHolder, item: MediaInfo) {
        val adapterPosition = holder.adapterPosition
        if (item.itemType == FileType.TimeTitle) {
//         val  binding  =holder.dataBinding as? RvItemPictureImageBinding
            val dataBinding = holder.getDataBinding<RvItemPictureTitleBinding>()
//            dataBinding?.tvTitle?.text = item.na.toString()

        } else {
            val dataBinding = holder.getDataBinding<RvItemPictureImageBinding>()
//            LogUtils.d(item.filePath)
            dataBinding?.image?.let {
                GlideUtils.load(item.filePath!!,it)
//                Glide.with(context).load(File(item.filePath!!))
//                        .apply(RequestOptions())
//                        .into(it)
                dataBinding.image.setOnLongClickListener(View.OnLongClickListener {
                    if (!isSelectModel) {
                        map.clear()
                        pictureController.showPictureControl(true)
//                        item.isSelect = true
                        map.put(item.id!!, true)
                        notifyItemChanged(adapterPosition)
//                        select(holder.adapterPosition)
                        pictureController.changeSelectData(true, isSelectModel, true, item)
                    }
                    return@OnLongClickListener false
                })
                dataBinding.image.onClick {
                    if (isSelectModel) {
                        var isSelect = map.get(item.id!!)
                        if (isSelect == null) {
                            isSelect = true;
                        } else {
                            isSelect = !isSelect
                        }
                        map.put(item.id!!, isSelect)
                        notifyItemChanged(adapterPosition)
//                        select(holder.adapterPosition)
                        pictureController.changeSelectData(false, isSelectModel, isSelect, item)
                    } else {
                        //查看大图
                        val previewFragment = PreviewFragment(data, adapterPosition)
                        pictureController.getHomeFragment().startFragment(previewFragment)
                    }
                }
                if (isSelectModel) {
                    dataBinding.ivSelect.visibility = View.VISIBLE
                    var isSelect = map.get(item.id!!)
                    if (isSelect == null || !isSelect!!) {
//                        dataBinding.ivSelect.setBackgroundColor(context.resources.getColor(R.color.qmui_config_color_white))
                        dataBinding.ivSelect.setBackgroundResource(R.mipmap.ic_image_unselected)
                    } else {
//                        dataBinding.ivSelect.setBackgroundColor(context.resources.getColor(R.color.qmui_config_color_red))
                        dataBinding.ivSelect.setBackgroundResource(R.mipmap.ic_image_selected)
                    }
                } else {
                    dataBinding.ivSelect.visibility = View.GONE
                }

            }
            if (item.uploadStatus == FileType.MediainfoStatus_uploadSuccess) {
                dataBinding?.ivIsexist?.visibility = View.VISIBLE
            } else {
                dataBinding?.ivIsexist?.visibility = View.GONE
            }
            if (item.isVideo!!) {
                dataBinding?.videoTime?.visibility = View.VISIBLE
                dataBinding?.videoTime?.setText(item.timestr)
            } else {
                dataBinding?.videoTime?.visibility = View.GONE
            }
//            dataBinding?.showpath?.text = " " + item.id

        }
    }


}