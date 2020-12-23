package com.library.base.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.library.R
import com.library.base.BaseApp
import java.io.File

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/25 下午3:35
 */
object GlideUtils {
    val option= RequestOptions().placeholder(R.drawable.ic_no_media)
    fun load(path: String, imageView: ImageView) {
        Glide.with(BaseApp.app).load(File(path))
                .apply(option)
                .into(imageView)
    }

    fun loadUrl(url: String, imageView: ImageView,replaceid :Int?=R.drawable.ic_no_media) {
        replaceid?.let {
            option.placeholder(replaceid)
        }
        Glide.with(BaseApp.app).load(url)
                .apply(option)
                .into(imageView)
    }
}