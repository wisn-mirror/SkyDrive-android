package com.we.player.player

import android.app.Application
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.upstream.cache.*
import com.google.android.exoplayer2.util.Util
import java.io.File

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午4:11
 */
class ExoSourceHelper {


    private var mUserAgent: String? = null
    private var mAppContext: Context? = null
    private var mHttpDataSourceFactory: HttpDataSource.Factory? = null
    private var mCache: Cache? = null

    constructor(app: Application){
        mAppContext = app
        mUserAgent = Util.getUserAgent(mAppContext!!, mAppContext!!.getApplicationInfo().name)
    }


    fun getMediaSource(uri: String): MediaSource? {
        return getMediaSource(uri, null, false)
    }

    fun getMediaSource(uri: String, headers: Map<String, String>?): MediaSource? {
        return getMediaSource(uri, headers, false)
    }

    fun getMediaSource(uri: String, isCache: Boolean): MediaSource? {
        return getMediaSource(uri, null, isCache)
    }

    fun getMediaSource(uri: String, headers: Map<String, String>?, isCache: Boolean): MediaSource? {
        val contentUri = Uri.parse(uri)
        if ("rtmp" == contentUri.scheme) {
            return ProgressiveMediaSource.Factory(RtmpDataSourceFactory(null))
                    .createMediaSource(contentUri)
        }
        val contentType = inferContentType(uri)
        val factory: DataSource.Factory
        factory = if (isCache) {
            getCacheDataSourceFactory()
        } else {
            getDataSourceFactory()
        }
        if (mHttpDataSourceFactory != null) {
            setHeaders(headers)
        }
        return when (contentType) {
            C.TYPE_DASH -> DashMediaSource.Factory(factory).createMediaSource(contentUri)
            C.TYPE_SS -> SsMediaSource.Factory(factory).createMediaSource(contentUri)
            C.TYPE_HLS -> HlsMediaSource.Factory(factory).createMediaSource(contentUri)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(factory).createMediaSource(contentUri)
            else -> ProgressiveMediaSource.Factory(factory).createMediaSource(contentUri)
        }
    }

    private fun inferContentType(fileName: String): Int {
        var fileName = fileName
        fileName = Util.toLowerInvariant(fileName)
        return if (fileName.contains(".mpd")) {
            C.TYPE_DASH
        } else if (fileName.contains(".m3u8")) {
            C.TYPE_HLS
        } else if (fileName.matches(Regex(""".*\\.ism(l)?(/manifest(\\(.+\\))?)?"""))) {
            C.TYPE_SS
        } else {
            C.TYPE_OTHER
        }
    }

    private fun getCacheDataSourceFactory(): DataSource.Factory {
        if (mCache == null) {
            mCache = newCache()
        }
        return CacheDataSourceFactory(
                mCache!!,
                getDataSourceFactory(),
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    private fun newCache(): Cache? {
        return SimpleCache(
                File(mAppContext!!.externalCacheDir, "exo-video-cache"),  //缓存目录
                LeastRecentlyUsedCacheEvictor(512 * 1024 * 1024),  //缓存大小，默认512M，使用LRU算法实现
                ExoDatabaseProvider(mAppContext!!))
    }

    /**
     * Returns a new DataSource factory.
     *
     * @return A new DataSource factory.
     */
    private fun getDataSourceFactory(): DataSource.Factory {
        return DefaultDataSourceFactory(mAppContext!!, getHttpDataSourceFactory()!!)
    }

    /**
     * Returns a new HttpDataSource factory.
     *
     * @return A new HttpDataSource factory.
     */
    private fun getHttpDataSourceFactory(): DataSource.Factory? {
        if (mHttpDataSourceFactory == null) {
            mHttpDataSourceFactory = DefaultHttpDataSourceFactory(
                    mUserAgent!!,
                    null,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,  //http->https重定向支持
                    true)
        }
        return mHttpDataSourceFactory
    }

    private fun setHeaders(headers: Map<String, String>?) {
        if (headers != null && headers.size > 0) {
            for ((key, value) in headers) {
                //如果发现用户通过header传递了UA，则强行将HttpDataSourceFactory里面的userAgent字段替换成用户的
                if (TextUtils.equals(key, "User-Agent")) {
                    if (!TextUtils.isEmpty(value)) {
                        try {
                            val userAgentField = mHttpDataSourceFactory?.javaClass?.getDeclaredField("userAgent")
                            userAgentField?.let {
                                userAgentField.isAccessible = true
                                userAgentField[mHttpDataSourceFactory] = value
                            }

                        } catch (e: Exception) {
                            //ignore
                        }
                    }
                } else {
                    mHttpDataSourceFactory!!.defaultRequestProperties[key!!] = value!!
                }
            }
        }
    }

    fun setCache(cache: Cache?) {
        mCache = cache
    }

    companion object {
        private var exoSourceHelper: ExoSourceHelper? = null
        fun newInstance(app: Application) = exoSourceHelper ?: synchronized(this) {
            exoSourceHelper ?: ExoSourceHelper(app).also { exoSourceHelper = it }
        }
    }


}