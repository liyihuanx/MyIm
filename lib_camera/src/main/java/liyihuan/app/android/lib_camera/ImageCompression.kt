package liyihuan.app.android.lib_camera

import android.content.Context
import android.graphics.*
import android.media.ExifInterface
import android.os.AsyncTask
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * @ClassName: ImageCompression
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:41
 */
class ImageCompression(context: Context) : AsyncTask<String, Void, String>() {

    private var context: WeakReference<Context> = WeakReference(context)

    private var mCompressCallback: PickCallback? = null
    private var mOutputFilePath: String? = null

    // Create the storage directory if it does not exist
    private val filename: String
        get() {
            if (mOutputFilePath != null) {
                return mOutputFilePath!!
            }
            val mediaStorageDir = File(
                Environment.getExternalStorageDirectory().toString()
                    + "/Android/data/"
                    + context.get()?.applicationContext?.packageName
                    + "/Files/Compressed")
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs()
            }

            val mImageName = "IMG_" + System.currentTimeMillis().toString() + ".jpg"

            return mediaStorageDir.absolutePath + "/" + mImageName
        }

    fun setCompressCallback(compressCallback: PickCallback?): ImageCompression {
        mCompressCallback = compressCallback
        return this
    }

    fun setOutputFilePath(outputFilePath: String): ImageCompression {
        mOutputFilePath = outputFilePath
        return this
    }

    override fun doInBackground(vararg strings: String): String? {
        try {
            return compressImage(strings[0])
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(imagePath: String?) {
        // imagePath is path of new compressed image.
        if (mCompressCallback != null&&imagePath!==null) {
            mCompressCallback!!.onSuccess(mutableListOf(imagePath))
        }
    }


    private fun compressImage(imagePath: String): String {
        var scaledBitmap: Bitmap? = null

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(imagePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio = maxWidth / maxHeight

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()

        }

        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
//        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bmp, middleX - bmp.width / 2, middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

        bmp.recycle()

        val exif: ExifInterface
        try {
            exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var out: FileOutputStream? = null
        val filepath = filename
        try {
            out = FileOutputStream(filepath)

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return filepath
    }

    companion object {
        private val maxHeight = 720.0f
        private val maxWidth = 720.0f

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
                val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
                inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
            }
            val totalPixels = (width * height).toFloat()
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++
            }

            return inSampleSize
        }
    }

}