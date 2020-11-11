package com.wisn.qm.ui.preview

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.github.chrisbanes.photoview.PhotoView
import com.qmuiteam.qmui.kotlin.onClick
import com.wisn.qm.R
import com.wisn.qm.mode.db.beans.MediaInfo

class PreviewViewHolder(view: View, var previewCallback: PreviewCallback) : RecyclerView.ViewHolder(view) {
    var iv_image: SubsamplingScaleImageView = view.findViewById(R.id.iv_image)
    var gif_view: PhotoView = view.findViewById(R.id.gif_view)

    init {
        iv_image.onClick {
            previewCallback.onContentClick(it)
        }
        gif_view.onClick {
            previewCallback.onContentClick(it)
        }
        iv_image.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE)
        iv_image.setDoubleTapZoomStyle(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
        iv_image.setDoubleTapZoomDuration(200)
        iv_image.setMinScale(1f)
        iv_image.setMaxScale(5f)
        iv_image.setDoubleTapZoomScale(3f)

        gif_view.setZoomTransitionDuration(200)
        gif_view.setMinimumScale(1f)
        gif_view.setMaximumScale(5f)
        gif_view.setScaleType(ImageView.ScaleType.FIT_CENTER)
    }

    fun loadImage(mediainfo: MediaInfo) {
        iv_image.setImage(ImageSource.uri(mediainfo.filePath!!))
    }

    fun loadVideo(mediainfo: MediaInfo) {

    }

}