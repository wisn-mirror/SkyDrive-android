package com.we.player.render

import android.view.View
import com.we.player.VideoViewConfig

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/13 下午4:11
 */
class MeasureHelper {
    private var mVideoWidth = 0

    private var mVideoHeight = 0

    private var mCurrentScreenScale = 0

    private var mVideoRotationDegree = 0

    fun setVideoRotation(videoRotationDegree: Int) {
        mVideoRotationDegree = videoRotationDegree
    }

    fun setVideoSize(width: Int, height: Int) {
        mVideoWidth = width
        mVideoHeight = height
    }

    fun setScreenScale(screenScale: Int) {
        mCurrentScreenScale = screenScale
    }

    /**
     * 注意：VideoViewConfig的宽高一定要定死，否者以下算法不成立
     */
    fun doMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int): IntArray? {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        if (mVideoRotationDegree == 90 || mVideoRotationDegree == 270) { // 软解码时处理旋转信息，交换宽高
            widthMeasureSpec = widthMeasureSpec + heightMeasureSpec
            heightMeasureSpec = widthMeasureSpec - heightMeasureSpec
            widthMeasureSpec = widthMeasureSpec - heightMeasureSpec
        }
        var width = View.MeasureSpec.getSize(widthMeasureSpec)
        var height = View.MeasureSpec.getSize(heightMeasureSpec)
        if (mVideoHeight == 0 || mVideoWidth == 0) {
            return intArrayOf(width, height)
        }
        when (mCurrentScreenScale) {
            VideoViewConfig.SCREEN_SCALE_DEFAULT -> if (mVideoWidth * height < width * mVideoHeight) {
                width = height * mVideoWidth / mVideoHeight
            } else if (mVideoWidth * height > width * mVideoHeight) {
                height = width * mVideoHeight / mVideoWidth
            }
            VideoViewConfig.SCREEN_SCALE_ORIGINAL -> {
                width = mVideoWidth
                height = mVideoHeight
            }
            VideoViewConfig.SCREEN_SCALE_16_9 -> if (height > width / 16 * 9) {
                height = width / 16 * 9
            } else {
                width = height / 9 * 16
            }
            VideoViewConfig.SCREEN_SCALE_4_3 -> if (height > width / 4 * 3) {
                height = width / 4 * 3
            } else {
                width = height / 3 * 4
            }
            VideoViewConfig.SCREEN_SCALE_MATCH_PARENT -> {
                width = widthMeasureSpec
                height = heightMeasureSpec
            }
            VideoViewConfig.SCREEN_SCALE_CENTER_CROP -> if (mVideoWidth * height > width * mVideoHeight) {
                width = height * mVideoWidth / mVideoHeight
            } else {
                height = width * mVideoHeight / mVideoWidth
            }
            else -> if (mVideoWidth * height < width * mVideoHeight) {
                width = height * mVideoWidth / mVideoHeight
            } else if (mVideoWidth * height > width * mVideoHeight) {
                height = width * mVideoHeight / mVideoWidth
            }
        }
        return intArrayOf(width, height)
    }

    companion object {
        private var exoSourceHelper: MeasureHelper? = null
        fun newInstance() = exoSourceHelper ?: synchronized(this) {
            exoSourceHelper ?: MeasureHelper().also { exoSourceHelper = it }
        }
    }


}