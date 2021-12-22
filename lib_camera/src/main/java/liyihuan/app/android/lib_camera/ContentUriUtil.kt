package liyihuan.app.android.lib_camera

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

/**
 * @ClassName: ContentUriUtil
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:39
 */

object ContentUriUtil {

    var MIMETYPE_MP4 = "video/mp4"

//    class UriResult constructor(var path: String? = null,
//                                var thumbPath: String? = null,
//                                var size: Long = -1,
//                                var duration: Long = -1,
//                                var mimeType: String? = null
//    )

    enum class ContentType(private var type: String) {
        image("image"),
        video("video"),
        audio("audio"),
        unkown("unkown");

        companion object {
            fun valueOf(type: String): ContentType {
                return when (type) {
                    "image" -> image
                    "video" -> video
                    "audio" -> audio
                    "unkown" -> unkown
                    else -> unkown
                }
            }
        }
    }

    @SuppressLint("NewApi")
    fun getDataFromUri(context: Context, uri: Uri, contentType: ContentType?): String? {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) { // DocumentProvider
            if (isExternalStorageDocument(uri)) { // ExternalStorageProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
//                    return UriResult(Environment.getExternalStorageDirectory().toString() + "/" + split[1])
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {  // downloagProvider
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), id.toLong())
                return getDataColumn(context, contentType, contentUri, null, null)
            } else if (isMediaDocument(uri)) { // MediaProvider
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = ContentType.valueOf(split[0])
                var contentUri: Uri? = null
                if (ContentType.image == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if (ContentType.video == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if (ContentType.audio == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, type, contentUri, selection, selectionArgs)
            }
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.scheme!!, ignoreCase = true)) { // DownloadsProvider
            return getDataColumn(context, contentType, uri, null, null)
        } else if (ContentResolver.SCHEME_FILE.equals(uri.scheme!!, ignoreCase = true)) { // File
            return uri.path
//            if (contentType != null && contentType == ContentUriUtil.ContentType.video) {
//                var time = VideoUtil.getVideoLength(uri.path)
//                var thumb = VideoUtil.createVideoThumbnail(uri.path, 400, 400)
//                var tempFilePath = FileConstants.CACHE_VIDEO_DIR + File.separator + System.currentTimeMillis() + ".jpeg"
//                BitmapUtils.saveBitmap2JPG(thumb, tempFilePath)
//                return UriResult(uri.path, duration = time, thumbPath = tempFilePath)
//            } else {
//                return UriResult(uri.path)
//            }
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param contentUri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, type: ContentType?, contentUri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var contentResolver = context.contentResolver
        val mediaIdColumn = MediaStore.MediaColumns._ID
        val mimeTypeColumn = MediaStore.MediaColumns.MIME_TYPE
        val dataColumn = MediaStore.MediaColumns.DATA
        val sizeColumn = MediaStore.MediaColumns.SIZE
        val durationColumn = MediaStore.Video.Media.DURATION
        MediaStore.Files.FileColumns.DATA
        val projection = if (type == ContentType.video || type == ContentType.audio) {
            arrayOf(mediaIdColumn, mimeTypeColumn, dataColumn, sizeColumn, durationColumn)
        } else {
            arrayOf(mediaIdColumn, mimeTypeColumn, dataColumn, sizeColumn)
        }

        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(contentUri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                var mediaId = cursor.getLong(cursor.getColumnIndexOrThrow(mediaIdColumn))
                val mimeType = cursor.getString(cursor.getColumnIndexOrThrow(mimeTypeColumn))
                var path = cursor.getString(cursor.getColumnIndexOrThrow(dataColumn))
                var size = cursor.getLong(cursor.getColumnIndexOrThrow(sizeColumn))
                var duration = 0L
                if (type == ContentType.video || type == ContentType.audio) {
                    duration = cursor.getLong(cursor.getColumnIndexOrThrow(durationColumn))
                }
//                var result = UriResult(path, null, size, duration, mimeType)
//                if (ContentType.image == type || ContentType.video == type) {
//                    var thumbPath = getThumbFromUri(contentResolver, type, mediaId)
//                    if (thumbPath == null && ContentType.video == type) {
//                        thumbPath = FileConstants.CACHE_VIDEO_DIR + File.separator + System.currentTimeMillis() + ".jpeg"
//                        var thumb = VideoUtil.createVideoThumbnail(path)
//                        BitmapUtils.saveBitmap2JPG(thumb, thumbPath)
//                    }
//                    result.thumbPath = thumbPath
//                }
                return path
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
    }

//    private fun getThumbFromUri(contentResolver: ContentResolver, type: ContentType?, mediaId: Long): String? {
//        var contentUri: Uri? = null
//        var thumbColumns: Array<String>? = null
//        var selection: String? = null
//        var thumbColumnName: String? = null
//        if (ContentType.image == type) {
//            //主动生成缩略图
//            MediaStore.Images.Thumbnails.getThumbnail(contentResolver, mediaId, MediaStore.Video.Thumbnails.MINI_KIND, null);
//
//            contentUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
//            thumbColumnName = MediaStore.Images.Thumbnails.DATA
//            thumbColumns = arrayOf(thumbColumnName, MediaStore.Images.Thumbnails.IMAGE_ID)
//            selection = MediaStore.Images.Thumbnails.IMAGE_ID + "=" + mediaId
//        } else if (ContentType.video == type) {
//            MediaStore.Video.Thumbnails.getThumbnail(contentResolver, mediaId, MediaStore.Video.Thumbnails.MINI_KIND, null);
//
//            contentUri = MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
//            thumbColumnName = MediaStore.Video.Thumbnails.DATA
//            thumbColumns = arrayOf(thumbColumnName, MediaStore.Video.Thumbnails.VIDEO_ID)
//            selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=" + mediaId
//        }
//        if (contentUri != null) {
//            var thumbCursor: Cursor? = null
//            try {
//                thumbCursor = contentResolver.query(contentUri, thumbColumns, selection, null, null)
//                var data: String? = null
//                if (thumbCursor.moveToFirst()) {
//                    data = thumbCursor.getString(thumbCursor.getColumnIndex(thumbColumnName))
//                }
//                return data
//            } catch (e: Exception) {
//            } finally {
//                thumbCursor?.close()
//            }
//        }
//        return null
//    }

    private fun getThumb(): String {
        return ""
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}