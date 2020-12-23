package com.wisn.qm.ui.album.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.util.getItemView
import com.library.base.config.Constant
import com.library.base.utils.GlideUtils
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
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/12/21 下午6:45
 */
class AlbumDetailsPageingAdapter(var editAlbumDetails: EditAlbumDetails, var albumDetailsFragment: AlbumDetailsPageingFragment,val update: ((Int, UserDirBean?, AlbumDetailsPageingAdapter) -> Unit))
    : PagingDataAdapter<UserDirBean, BaseDataBindlingViewHolder>(object :
        DiffUtil.ItemCallback<UserDirBean>() {

    override fun areItemsTheSame(
            oldItem: UserDirBean,
            newItem: UserDirBean
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
            oldItem: UserDirBean,
            newItem: UserDirBean
    ): Boolean {
        return oldItem == newItem
    }
}) {

    var isSelectModel: Boolean = false
    protected var map: HashMap<Long, Boolean> = HashMap()
    override fun onBindViewHolder(holder: BaseDataBindlingViewHolder, position: Int) {
        var item=getItem(position)!!
        val adapterPosition = holder.adapterPosition
        if (item.itemType == FileType.TimeTitle) {
//            viewHolder.setDataBinding<RvItemAlbumDetailTimetitleBinding>(viewHolder.itemView)
        } else if (item.itemType == FileType.TitleInfo) {
//            viewHolder.setDataBinding<RvItemAlbumDetailTitleBinding>(viewHolder.itemView)
        } else if (item.itemType == FileType.ImageViewItem || item.itemType == FileType.VideoViewItem) {
//            viewHolder.setDataBinding<RvItemAlbumDetailMediaBinding>(viewHolder.itemView)
            val dataBinding = holder.getDataBinding<RvItemAlbumDetailMediaBinding>()
            dataBinding?.image?.let {
//                Glide.with(it.context).load(Constant.getImageUrl(item.sha1!!))
//                        .apply(RequestOptions())
//                        .into(it)
                GlideUtils.loadUrl(Constant.getImageUrl(item.sha1!!),it)
                dataBinding?.image?.setOnLongClickListener(View.OnLongClickListener {
                    if (!isSelectModel) {
                        map.clear()
                        map.put(item.id!!, true)
                        isSelectModel = true;
                        notifyDataSetChanged()
                        editAlbumDetails.isShowEdit(true)
                        editAlbumDetails.changeSelectData(true, true, item)
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
                        editAlbumDetails.changeSelectData(false, isSelect, item)
                    } else {
                        //查看大图
//                        val netPreviewFragment = NetPreviewFragment(this.da, adapterPosition)
//                        albumDetailsFragment.startFragment(netPreviewFragment)
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

    fun updateSelect(isSelectModel: Boolean?) {
        isSelectModel?.let {
            this.isSelectModel = isSelectModel
            editAlbumDetails.isShowEdit(false)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDataBindlingViewHolder {
        var viewHolder:BaseDataBindlingViewHolder =BaseDataBindlingViewHolder(LayoutInflater.from(parent.context).inflate( R.layout.rv_item_album_detail_media, parent, false))
        if (viewType == FileType.TimeTitle) {
            viewHolder =BaseDataBindlingViewHolder(LayoutInflater.from(parent.context).inflate( R.layout.rv_item_album_detail_title, parent, false))
            viewHolder.setDataBinding<RvItemAlbumDetailTimetitleBinding>(viewHolder.itemView)
        } else if (viewType == FileType.TitleInfo) {
            viewHolder =BaseDataBindlingViewHolder(LayoutInflater.from(parent.context).inflate( R.layout.rv_item_album_detail_title, parent, false))
            viewHolder.setDataBinding<RvItemAlbumDetailTitleBinding>(viewHolder.itemView)
        } else if (viewType == FileType.ImageViewItem || viewType == FileType.VideoViewItem) {
            viewHolder.setDataBinding<RvItemAlbumDetailMediaBinding>(viewHolder.itemView)
        } else if (viewType == FileType.UploadProgressItem) {
//            viewHolder.setDataBinding<RvItemAlbumDetailProgressBinding>(viewHolder.itemView)
            viewHolder =BaseDataBindlingViewHolder(LayoutInflater.from(parent.context).inflate( R.layout.rv_item_album_detail_progress, parent, false))
        }
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.itemType
    }

}