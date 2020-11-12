package com.wisn.qm.ui.album.details

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.library.base.config.Constant
import com.qmuiteam.qmui.kotlin.onClick
import com.wisn.qm.R
import com.wisn.qm.databinding.RvItemAlbumDetailMediaBinding
import com.wisn.qm.databinding.RvItemAlbumDetailTimetitleBinding
import com.wisn.qm.databinding.RvItemAlbumDetailTitleBinding
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.UserDirBean
import com.wisn.qm.ui.album.EditAlbumDetails
import com.wisn.qm.ui.home.BaseDataBindlingViewHolder

/**
 * Created by Wisn on 2020/6/6 下午6:14.
 */

class AlbumDetailsAdapter(var editAlbumDetails: EditAlbumDetails) : BaseMultiItemQuickAdapter<UserDirBean, BaseDataBindlingViewHolder>() {
    var isSelectModel: Boolean = false
    protected var map: HashMap<Long, Boolean> = HashMap()

    init {
        addItemType(FileType.UploadProgressItem, R.layout.rv_item_album_detail_progress)
        addItemType(FileType.TitleInfo, R.layout.rv_item_album_detail_title)
        addItemType(FileType.TimeTitle, R.layout.rv_item_album_detail_timetitle)
        addItemType(FileType.ImageViewItem, R.layout.rv_item_album_detail_media)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDataBindlingViewHolder {
        return super.onCreateViewHolder(parent, viewType)
    }

    fun updateSelect(isSelectModel: Boolean?) {
        isSelectModel?.let {
            this.isSelectModel = isSelectModel
            editAlbumDetails.isShowEdit(false)
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
            viewHolder.setDataBinding<RvItemAlbumDetailTimetitleBinding>(viewHolder.itemView)
        } else if (viewType == FileType.TitleInfo) {
            viewHolder.setDataBinding<RvItemAlbumDetailTitleBinding>(viewHolder.itemView)
        } else if (viewType == FileType.ImageViewItem) {
            viewHolder.setDataBinding<RvItemAlbumDetailMediaBinding>(viewHolder.itemView)
        } else if (viewType == FileType.UploadProgressItem) {
//            viewHolder.setDataBinding<RvItemAlbumDetailProgressBinding>(viewHolder.itemView)
        }
    }


    override fun convert(holder: BaseDataBindlingViewHolder, item: UserDirBean) {
        val adapterPosition = holder.adapterPosition
        if (item.itemType == FileType.TimeTitle) {
//            viewHolder.setDataBinding<RvItemAlbumDetailTimetitleBinding>(viewHolder.itemView)
        } else if (item.itemType == FileType.TitleInfo) {
//            viewHolder.setDataBinding<RvItemAlbumDetailTitleBinding>(viewHolder.itemView)
        } else if (item.itemType == FileType.ImageViewItem) {
//            viewHolder.setDataBinding<RvItemAlbumDetailMediaBinding>(viewHolder.itemView)
            val dataBinding = holder.getDataBinding<RvItemAlbumDetailMediaBinding>()
            dataBinding?.image?.let {
                Glide.with(context).load(Constant.getImageUrl(item.sha1!!))
                        .apply(RequestOptions())
                        .into(it)
                dataBinding?.image?.setOnLongClickListener(View.OnLongClickListener {
                    if (!isSelectModel) {
                        map.clear()
                        map.put(item.id!!, true)
                        isSelectModel = true;
                        notifyDataSetChanged()
                        editAlbumDetails.isShowEdit(true)
                        editAlbumDetails.changeSelectData(true,true,item)
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
                        editAlbumDetails.changeSelectData(false,isSelect,item)
                    } else {
                        //查看大图
//                        val previewFragment = PreviewFragment(data, adapterPosition)
//                        pictureController.getHomeFragment().startFragment(previewFragment)
                    }
                }
            }
            if (item.ftype == 1) {
                dataBinding?.videoTime?.visibility = View.VISIBLE
                dataBinding?.videoTime?.setText(item.video_duration)
            } else {
                dataBinding?.videoTime?.visibility = View.GONE
            }

            if (isSelectModel) {
                dataBinding?.ivSelect?.visibility = View.VISIBLE
                var isSelect = map.get(item.id!!)
                if (isSelect == null || !isSelect!!) {
                    dataBinding?.ivSelect?.setBackgroundResource(R.mipmap.ic_image_unselected)
                } else {
                    dataBinding?.ivSelect?.setBackgroundResource(R.mipmap.ic_image_selected)
                }
            } else {
                dataBinding?.ivSelect?.visibility = View.GONE
            }
        } else if (item.itemType == FileType.UploadProgressItem) {
//            viewHolder.setDataBinding<RvItemAlbumDetailProgressBinding>(viewHolder.itemView)
        }
    }


}