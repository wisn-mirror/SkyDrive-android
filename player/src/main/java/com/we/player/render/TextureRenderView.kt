package com.we.player.render

import android.content.Context
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView
import android.view.View
import com.we.player.APlayer
import com.we.player.IRenderView

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/12 下午7:53
 */
class TextureRenderView(context: Context) : TextureView(context), IRenderView, TextureView.SurfaceTextureListener {
    val mMeasureHelper = MeasureHelper.newInstance()
    lateinit var player: APlayer
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mSurface: Surface? = null

    init {
        surfaceTextureListener = this
    }

    override fun attachToPlayer(player: APlayer) {
        this.player = player
    }

    override fun setVideoSize(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            mMeasureHelper.setVideoSize(width, height)
            requestLayout()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        try {
            val doMeasure = mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec)
            if (doMeasure != null) {
                setMeasuredDimension(doMeasure.get(0), doMeasure.get(1))
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        } catch (e: Exception) {
        }
    }

    override fun setVideoRotation(degree: Int) {
        mMeasureHelper.setVideoRotation(degree)
    }

    override fun getRenderView(): View {
        return this
    }

    override fun getScreenShot(): Bitmap? {
        return getBitmap()
    }

    override fun release() {
        mSurface?.release()
        mSurfaceTexture?.release()
    }

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
        if (mSurfaceTexture == null) {
            mSurfaceTexture = p0
            mSurface = Surface(mSurfaceTexture)
            player?.setSurface(mSurface)
        } else {
            setSurfaceTexture(mSurfaceTexture!!)
        }
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
        return false
    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
    }
}