package com.we.player.render

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.we.player.player.APlayer

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/15 下午8:43
 */
class SurfaceRenderView(context: Context) : SurfaceView(context), IRenderView, SurfaceHolder.Callback {
    val mMeasureHelper = MeasureHelper.newInstance()
    lateinit var player: APlayer
    init {
        val surfaceHolder = holder
        surfaceHolder.addCallback(this)
        surfaceHolder.setFormat(PixelFormat.RGBA_8888)
    }

    override fun setScreenScaleType(screenScaleType: Int) {
        mMeasureHelper.setScreenScale(screenScaleType)
        requestLayout()
    }

    override fun attachToPlayer(player: APlayer) {
        this.player = player
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        try {
            val doMeasure = mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec)
            if (doMeasure != null) {
                setMeasuredDimension(doMeasure.get(0), doMeasure.get(1))
            }/* else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }*/
        } catch (e: Exception) {
        }
    }

    override fun setVideoSize(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            mMeasureHelper.setVideoSize(width, height)
            requestLayout()
        }
    }

    override fun setVideoRotation(degree: Int) {
        mMeasureHelper.setVideoRotation(degree)
        rotation=degree.toFloat()
    }

    override fun getRenderView(): View {
        return this
    }

    override fun getScreenShot(): Bitmap? {
        return null
    }

    override fun release() {

    }

    override fun surfaceCreated(holder: SurfaceHolder) {

    }

    override fun surfaceChanged(p0: SurfaceHolder, format: Int, width: Int, height: Int) {
        player?.setDisplay(p0)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }
}