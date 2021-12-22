package liyihuan.app.android.lib_camera

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File

/**
 * @ClassName: PhotoRequestFragment
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:42
 */
final class PhotoRequestFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.retainInstance = true // Destroy生命周期并不会执行
    }

    var size: Size? = null
    var callback: PickCallback? = null

    var mCameraFilePath: String? = null
    var mTempFilePath: String? = null


    private fun checkSizeCrop(): Boolean {
        return !(size == null || size!!.aspectX < 1 || size!!.aspectY < 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            PicPickHelper.REQUEST_CODE_CHOOSE_LOCAL -> {
                if (data?.data != null) {
                    if (!checkSizeCrop()) {
                        val originFileData =
                            ContentUriUtil.getDataFromUri(
                                context!!,
                                data.data!!,
                                ContentUriUtil.ContentType.image
                            )
                        originFileData?.let {

                            ImageCompression(context!!)
                                .setOutputFilePath(ImageChooseHelper.compressFilePath)
                                .setCompressCallback(object : PickCallback {
                                    override fun onSuccess(pathList: MutableList<String>) {
                                        callback?.onSuccess(pathList)
                                    }

                                }).execute(it)
                        }
                    } else {
                        mTempFilePath = ImageChooseHelper.cropFilePath
                        val filePath = ContentUriUtil.getDataFromUri(
                            context!!,
                            data.data!!,
                            ContentUriUtil.ContentType.image
                        )
                        val imageUri = FileProvider.getUriForFile(
                            context!!,
                            ImageChooseHelper.PROVIDER_KEY,
                            File(filePath!!)
                        )
                        val intent = ImageChooseHelper.cropImageIntent(
                            activity!!,
                            imageUri!!,
                            size!!,
                            mTempFilePath!!
                        )
                        startActivityForResult(intent, PicPickHelper.REQUEST_CODE_CROP)
                    }
                } else {
                    Log.e("TAG", "REQUEST_CODE_CHOOSE_LOCAL data.getData() is null")
                }
            }
            PicPickHelper.REQUEST_CODE_CROP -> {// 所有图片选取都得走得这一步
                ImageCompression(context!!)
                    .setOutputFilePath(ImageChooseHelper.compressFilePath)
                    .setCompressCallback(object : PickCallback {
                        override fun onSuccess(pathList: MutableList<String>) {
                            callback?.onSuccess(pathList)
                        }
                    }).execute(mTempFilePath)
            }

            PicPickHelper.REQUEST_CODE_CAMERA -> {
                if (!checkSizeCrop()) {
                    ImageCompression(context!!)
                        .setOutputFilePath(ImageChooseHelper.compressFilePath)
                        .setCompressCallback(object : PickCallback {
                            override fun onSuccess(pathList: MutableList<String>) {
                                callback?.onSuccess(pathList)
                            }
                        }).execute(mCameraFilePath)
                } else {
                    val imageUri = FileProvider.getUriForFile(
                            context!!,
                            ImageChooseHelper.PROVIDER_KEY,
                            File(mCameraFilePath!!)
                        )
                    mTempFilePath = ImageChooseHelper.cropFilePath
                    val intent = ImageChooseHelper.cropImageIntent(
                        activity!!,
                        imageUri,
                        size!!,
                        mTempFilePath!!
                    )
                    startActivityForResult(intent, PicPickHelper.REQUEST_CODE_CROP)
                }
            }

            else -> {
            }

        }
    }

}
