package com.wisn.qm.ui.netpreview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wisn.qm.R
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.UserDirBean

class NetPreviewAdapter(var data: MutableList<UserDirBean>, var previewCallback: PreviewCallback) : RecyclerView.Adapter<BaseNetPreviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseNetPreviewHolder {
        if (viewType == FileType.ImageViewItem) {
            return NetPreviewImageViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.rv_item_preview_netimage, parent, false), previewCallback)
        } else if (viewType == FileType.VideoViewItem) {
            return NetPreviewVideoViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.rv_item_preview_netvideo, parent, false), previewCallback)
        } else {
            return NetPreviewImageViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.rv_item_preview_netvideo, parent, false), previewCallback)
        }
    }

    override fun onBindViewHolder(holder: BaseNetPreviewHolder, position: Int) {
        if (getItemViewType(position) == FileType.ImageViewItem) {
            holder.loadImage(position,data.get(position))
        } else {
            holder.loadVideo(position,data.get(position))
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseNetPreviewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is NetPreviewVideoViewHolder) {
            holder.releaseVideo(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data.get(position).itemType
    }


}