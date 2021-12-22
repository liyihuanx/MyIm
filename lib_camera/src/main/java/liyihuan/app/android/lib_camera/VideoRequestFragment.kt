package liyihuan.app.android.lib_camera

import android.app.Activity
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.WorkerThread
import androidx.fragment.app.Fragment
import java.io.File

/**
 * @ClassName: VideoRequestFragment
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:45
 */
class VideoRequestFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.retainInstance = true
    }


    var callback: PickCallback? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {//从相册选取视频
            activity?.let {

                callback?.onSuccess(
                    arrayListOf(
                        ContentUriUtil.getDataFromUri(
                            it,
                            data.data!!,
                            ContentUriUtil.ContentType.video
                        ) ?: ""
                    )
                )
//                Observable.just(data.data)
//                    .map(object : Function<Uri, MediaParams> {
//                        override fun apply(t: Uri): MediaParams {
//                            var path = ContentUriUtil.getDataFromUri(it, t, ContentUriUtil.ContentType.video)
//                            var params = getVideoParams(path)
//                            if (params != null) {
//                                return params
//                            }
//                            throw Exception("获取视频资源失败")
//                        }
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({
//                        callback?.onSuccess(arrayListOf( ContentUriUtil.getDataFromUri(it, data.data, ContentUriUtil.ContentType.video)))
//                    }, {
//
//                    })
            }
        }
    }

    @WorkerThread
    fun getVideoParams(videoPath: String?): MediaParams? {
        if (FileConstants.CACHE_VIDEO_DIR == null) {
            activity?.application?.let { FileConstants.initFileConfig(it) }
        }
        if (TextUtils.isEmpty(videoPath)) {
            return null
        }
        var media = MediaMetadataRetriever()
        try {
            var path = videoPath
            val file = File(path)
            media.setDataSource(path)
            //取得指定时间（第6us）的Bitmap，即可以实现抓图（缩略图）功能
            val thumb = media.getFrameAtTime(3)
            val thumPath = FileConstants.CACHE_VIDEO_DIR + System.currentTimeMillis() + ".jpeg"
            BitmapUtils.saveBitmap2JPG(thumb, thumPath)
            val video_length =
                media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
            val size = FileUtil.getFileSize(file)
            val mimeType = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
            //如果不包含文件后缀就复制一份有后缀的文件
            if (!file.name.contains(".")) {
                var suffix = mimeType?.substring(mimeType.lastIndexOf("/") + 1)
                path = FileConstants.CACHE_VIDEO_DIR + file.name + "." + suffix
                var newFile = FileUtil.createFile(path)
                newFile?.let {
                    file.copyTo(it, true)
                }
            }
            return MediaParams(path, thumPath, size, video_length!!, mimeType)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            media?.let {
                it.release()
            }
        }
        return null
    }
}