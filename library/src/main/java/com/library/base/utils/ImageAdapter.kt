package com.library.base.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.library.base.config.Constant

object ImageAdapter {
    @BindingAdapter(value = ["sha1"], requireAll = true)
    @JvmStatic
    fun setImageSha1(imageView: ImageView, sha1: String?) {
        sha1?.let {
            Glide.with(imageView.context)
                    .load(Constant.getImageUrl(sha1))
                    .apply(RequestOptions())
                    .into(imageView)
        }
    }

}