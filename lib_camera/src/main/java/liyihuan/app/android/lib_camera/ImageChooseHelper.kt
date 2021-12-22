package liyihuan.app.android.lib_camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

/**
 * @ClassName: ImageChooseHelper
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:40
 */
object ImageChooseHelper {

    var PROVIDER_KEY = "xxx.android7.fileProvider"

    val originPath: String
        get() = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                + File.separator + "origin_" + System.currentTimeMillis() + ".jpg")


    val cameraFilePath: String
        get() = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                + File.separator + System.currentTimeMillis() + ".jpg")

    val cropFilePath: String
        get() = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                + File.separator + "crop_" + System.currentTimeMillis() + ".jpg")

    val compressFilePath: String
        get() = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                + File.separator + "compress_" + System.currentTimeMillis() + ".jpg")

    internal fun init(context: Context) {
        PROVIDER_KEY = context.packageName + ".android7.fileProvider"
    }

    /**
     * pick image
     * https://developer.android.com/guide/topics/providers/document-provider
     */
    fun pickImageIntent(): Intent {
        var intent = Intent()
        intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }

    /**
     * take photo
     * https://developer.android.com/training/camera/photobasics
     */
    fun takePhotoIntent(context: Activity, outputFilePath: String): Intent {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.packageManager) != null) {
            val photoFile = File(outputFilePath)
            if (!photoFile.parentFile.exists()) {
                photoFile.parentFile.mkdirs()
            }

            // Continue only if the File was successfully created
            var photoURI = FileProvider.getUriForFile(context, PROVIDER_KEY, photoFile)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                photoURI = Uri.fromFile(photoFile)
            }

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }

        return takePictureIntent
    }

    /**
     * take photo
     * https://developer.android.com/training/camera/photobasics
     */
    fun videoIntent(context: Activity): Intent {
        var intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (intent.resolveActivity(context.packageManager) != null) {
            // Continue only if the File was successfully created
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)
            intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 50 * 1024 * 1024)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI)
        }
        return intent
    }

    /**
     * crop image
     */
    fun cropImageIntent(context: Activity, imageUri: Uri, size: Size, outPutFilePath: String): Intent {
        val outFile = File(outPutFilePath)
        if (!outFile.parentFile.exists()) {
            outFile.parentFile.mkdirs()
        }
        val outputUri = FileProvider.getUriForFile(context, PROVIDER_KEY, outFile)
        //通过FileProvider创建一个content类型的Uri
        val intent = Intent("com.android.camera.action.CROP")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.setDataAndType(imageUri, "image/*")
        intent.putExtra("crop", "true")
        intent.putExtra("aspectX", 0)
        intent.putExtra("aspectY", 0)
        intent.putExtra("outputX", 20)
        intent.putExtra("outputY", 1)
        intent.putExtra("scale", true)//去除黑边
//        intent.putExtra("scaleUpIfNeeded", true)//去除黑边
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true) // no face detection

        /* https://juejin.im/entry/586dbd798d6d8100586ac8e2
           fix java.lang.SecurityException: Permission Denial: opening provider android.support.v4.content.FileProvider from ProcessRecord{42725078 24872:com.android.camera/u0a14} (pid=24872, uid=10014) that is not exported from uid 10310
         */
        val resInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(packageName, outputUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        return intent
    }
}