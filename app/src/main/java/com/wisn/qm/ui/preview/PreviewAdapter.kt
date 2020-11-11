package com.wisn.qm.ui.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wisn.qm.R
import com.wisn.qm.mode.bean.FileType
import com.wisn.qm.mode.db.beans.MediaInfo

class PreviewAdapter(var data: MutableList<MediaInfo>,var previewCallback:PreviewCallback ) : RecyclerView.Adapter<PreviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewViewHolder {
        if (viewType == FileType.ImageViewItem) {
            return PreviewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_preview_image, parent, false),previewCallback)
        } else {
            return PreviewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_item_preview_video, parent, false),previewCallback)
        }
    }

    override fun onBindViewHolder(holder: PreviewViewHolder, position: Int) {
        if (getItemViewType(position) == FileType.ImageViewItem) {
            holder.loadImage(data.get(position))
        } else {
            holder.loadVideo(data.get(position))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data.get(position).itemType
    }

}