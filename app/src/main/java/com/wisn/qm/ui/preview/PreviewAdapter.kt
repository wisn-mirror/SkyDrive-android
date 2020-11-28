package com.wisn.qm.ui.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wisn.qm.R
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.MediaInfo

class PreviewAdapter(var data: MutableList<MediaInfo>, var previewCallback: PreviewCallback) : RecyclerView.Adapter<BasePreviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasePreviewHolder {
        if (viewType == FileType.ImageViewItem) {
            return PreviewImageViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.rv_item_preview_localimage, parent, false), previewCallback)
        } else if (viewType == FileType.VideoViewItem) {
            return PreviewVideoViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.rv_item_preview_localvideo, parent, false), previewCallback)
        } else {
            return PreviewImageViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.rv_item_preview_localvideo, parent, false), previewCallback)
        }
    }

    override fun onBindViewHolder(holder: BasePreviewHolder, position: Int) {
        if (getItemViewType(position) == FileType.ImageViewItem) {
            holder.loadImage(position,data.get(position))
        } else {
            holder.loadVideo(position,data.get(position))
        }
    }

    override fun onViewDetachedFromWindow(holder: BasePreviewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is PreviewVideoViewHolder) {
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