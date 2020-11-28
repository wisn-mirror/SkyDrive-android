package com.wisn.qm.ui.netpreview

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.github.chrisbanes.photoview.PhotoView
import com.library.base.config.Constant
import com.library.base.utils.GlideUtils
import com.library.base.utils.ImageUtils
import com.qmuiteam.qmui.kotlin.onClick
import com.wisn.qm.R
import com.wisn.qm.mode.db.beans.MediaInfo
import com.wisn.qm.mode.db.beans.UserDirBean

class NetPreviewImageViewHolder(var context: Context, view: View, var previewCallback: PreviewCallback) : BaseNetPreviewHolder(view) {
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

    override fun loadImage(position: Int, mediainfo: UserDirBean) {
        mediainfo.sha1?.let {
            GlideUtils.loadUrl(Constant.getImageUrl(it)!!, gif_view)
            iv_image.visibility = View.GONE
            gif_view.visibility = View.VISIBLE
        }
       /* if (ImageUtils.isGifImageWithMime(mediainfo.filePath!!, mediainfo.filePath!!)) {
            iv_image.visibility = View.GONE
            gif_view.visibility = View.VISIBLE
            GlideUtils.load(mediainfo.filePath!!, gif_view)
        } else {
            iv_image.visibility = View.VISIBLE
            gif_view.visibility = View.GONE
            iv_image.setImage(ImageSource.uri(mediainfo.filePath!!))
        }*/
    }

    override fun loadVideo(position: Int, mediainfo: UserDirBean) {

    }

}