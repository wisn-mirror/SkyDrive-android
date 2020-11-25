package com.library.base.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.library.base.BaseApp
import java.io.File

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/25 下午3:35
 */
object GlideUtils {

    fun load(path: String, imageView: ImageView) {
        Glide.with(BaseApp.app).load(File(path))
                .apply(RequestOptions())
                .into(imageView)
    }

    fun loadUrl(url: String, imageView: ImageView) {
        Glide.with(BaseApp.app).load(url)
                .apply(RequestOptions())
                .into(imageView)
    }
}