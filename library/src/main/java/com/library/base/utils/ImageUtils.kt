package com.library.base.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.text.TextUtils
import android.util.Log
import java.io.IOException

object ImageUtils {
    const val TAG="ImageUtils"
    @JvmStatic
    fun getImageInfo() {

    }

    fun getBitmapDegree(path: String?): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            degree = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }
    fun rotateBitmapByDegree(bm: Bitmap, degree: Int): Bitmap? {
        var returnBm: Bitmap? = null
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }
        if (returnBm == null) {
            returnBm = bm
        }
        if (bm != returnBm) {
            bm.recycle()
        }
        return returnBm
    }

    fun getOrientation(imagePath: String?): Int {
        val degree = 0
        try {
            val exifInterface = ExifInterface(imagePath)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            return when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }


    fun getWidthHeight(imagePath: String): IntArray {
        if (imagePath.isEmpty()) {
            return intArrayOf(0, 0)
        }
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            val originBitmap = BitmapFactory.decodeFile(imagePath, options)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        // 使用第一种方式获取原始图片的宽高
        var srcWidth = options.outWidth
        var srcHeight = options.outHeight

        // 使用第二种方式获取原始图片的宽高
        if (srcHeight == -1 || srcWidth == -1) {
            try {
                val exifInterface = ExifInterface(imagePath)
                srcHeight = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL)
                srcWidth = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        // 使用第三种方式获取原始图片的宽高
        if (srcWidth <= 0 || srcHeight <= 0) {
            val bitmap2 = BitmapFactory.decodeFile(imagePath)
            if (bitmap2 != null) {
                srcWidth = bitmap2.width
                srcHeight = bitmap2.height
                try {
                    if (!bitmap2.isRecycled) {
                        bitmap2.recycle()
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        val orient: Int = getOrientation(imagePath)
        return if (orient == 90 || orient == 270) {
            intArrayOf(srcHeight, srcWidth)
        } else intArrayOf(srcWidth, srcHeight)
    }


    fun isLongImage(context: Context, imagePath: String): Boolean {
        val wh: IntArray = getWidthHeight(imagePath)!!
        val w = wh[0].toFloat()
        val h = wh[1].toFloat()
        val imageRatio = h / w
        val phoneRatio: Float = PhoneUtils.getPhoneRatio(context.applicationContext) + 0.1f
        val isLongImage = w > 0 && h > 0 && h > w && imageRatio >= phoneRatio
//        Print.d(TAG, "isLongImage = $isLongImage")
        return isLongImage
    }

    fun isWideImage(context: Context?, imagePath: String): Boolean {
        val wh: IntArray = getWidthHeight(imagePath)
        val w = wh[0].toFloat()
        val h = wh[1].toFloat()
        val imageRatio = w / h
        //float phoneRatio = PhoneUtil.getPhoneRatio(context.getApplicationContext()) + 0.1F;
        val isWideImage = w > 0 && h > 0 && w > h && imageRatio >= 2
        Log.d(TAG, "isWideImage = $isWideImage")
        return isWideImage
    }

    fun isSmallImage(context: Context, imagePath: String): Boolean {
        val wh: IntArray = getWidthHeight(imagePath)
        val isSmallImage: Boolean = wh[0] < PhoneUtils.getPhoneWid(context.applicationContext)
        Log.d(TAG, "isSmallImage = $isSmallImage")
        return isSmallImage
    }

    fun getLongImageMinScale(context: Context, imagePath: String): Float {
        val wh: IntArray = getWidthHeight(imagePath)
        val imageWid = wh[0].toFloat()
        val phoneWid: Float = PhoneUtils.getPhoneWid(context.applicationContext).toFloat()
        return phoneWid / imageWid
    }

    fun getLongImageMaxScale(context: Context, imagePath: String): Float {
        return getLongImageMinScale(context, imagePath) * 2
    }

    fun getWideImageDoubleScale(context: Context, imagePath: String): Float {
        val wh: IntArray = getWidthHeight(imagePath)
        val imageHei = wh[1].toFloat()
        val phoneHei: Float = PhoneUtils.getPhoneHei(context.applicationContext).toFloat()
        return phoneHei / imageHei
    }

    fun getSmallImageMinScale(context: Context, imagePath: String): Float {
        val wh: IntArray = getWidthHeight(imagePath)
        val imageWid = wh[0].toFloat()
        val phoneWid: Float = PhoneUtils.getPhoneWid(context.applicationContext).toFloat()
        return phoneWid / imageWid
    }

    fun getSmallImageMaxScale(context: Context, imagePath: String): Float {
        val wh: IntArray = getWidthHeight(imagePath)
        val imageWid = wh[0].toFloat()
        val phoneWid: Float = PhoneUtils.getPhoneWid(context.applicationContext).toFloat()
        return phoneWid * 2 / imageWid
    }

    fun getImageBitmap(srcPath: String?, degree: Int): Bitmap? {
        var degree = degree
        var isOOM = false
        val newOpts = BitmapFactory.Options()
        newOpts.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
        newOpts.inJustDecodeBounds = false
        val be = 1f
        newOpts.inSampleSize = be.toInt()
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565
        newOpts.inDither = false
        newOpts.inPurgeable = true
        newOpts.inInputShareable = true
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
        try {
            bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
        } catch (e: OutOfMemoryError) {
            isOOM = true
            if (bitmap != null && !bitmap.isRecycled) {
                bitmap.recycle()
            }
            Runtime.getRuntime().gc()
        } catch (e: java.lang.Exception) {
            isOOM = true
            Runtime.getRuntime().gc()
        }
        if (isOOM) {
            try {
                bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
            } catch (e: java.lang.Exception) {
                newOpts.inPreferredConfig = Bitmap.Config.RGB_565
                bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
            }
        }
        if (bitmap != null) {
            if (degree == 90) {
                degree += 180
            }
            bitmap = rotateBitmapByDegree(bitmap, degree)
            val ttHeight = 1080 * bitmap.height / bitmap.width
            if (bitmap.width >= 1080) {
                bitmap = zoomBitmap(bitmap, 1080, ttHeight)
            }
        }
        return bitmap
    }

    private fun zoomBitmap(bitmap: Bitmap?, width: Int, height: Int): Bitmap? {
        val w = bitmap!!.width
        val h = bitmap.height
        val matrix = Matrix()
        val scaleWidth = width.toFloat() / w
        val scaleHeight = height.toFloat() / h
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true)
    }

    fun getImageTypeWithMime(path: String?): String? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        var type = options.outMimeType
        Log.d(TAG, "getImageTypeWithMime: type1 = $type")
        // ”image/png”、”image/jpeg”、”image/gif”
        type = if (TextUtils.isEmpty(type)) {
            ""
        } else {
            type.substring(6)
        }
        Log.d(TAG, "getImageTypeWithMime: type2 = $type")
        return type
    }

    fun isPngImageWithMime(url: String, path: String?): Boolean {
        return "png".equals(getImageTypeWithMime(path), ignoreCase = true) || url.toLowerCase().endsWith("png")
    }

    fun isJpegImageWithMime(url: String, path: String?): Boolean {
        return ("jpeg".equals(getImageTypeWithMime(path), ignoreCase = true) || "jpg".equals(getImageTypeWithMime(path), ignoreCase = true)
                || url.toLowerCase().endsWith("jpeg") || url.toLowerCase().endsWith("jpg"))
    }

    fun isBmpImageWithMime(url: String, path: String?): Boolean {
        return "bmp".equals(getImageTypeWithMime(path), ignoreCase = true) || url.toLowerCase().endsWith("bmp")
    }

    fun isGifImageWithMime(url: String, path: String?): Boolean {
        return "gif".equals(getImageTypeWithMime(path), ignoreCase = true) || url.toLowerCase().endsWith("gif")
    }

    fun isWebpImageWithMime(url: String, path: String?): Boolean {
        return "webp".equals(getImageTypeWithMime(path), ignoreCase = true) || url.toLowerCase().endsWith("webp")
    }

    fun isStandardImage(url: String, path: String?): Boolean {
        return isJpegImageWithMime(url, path) || isPngImageWithMime(url, path) || isBmpImageWithMime(url, path)
    }
}