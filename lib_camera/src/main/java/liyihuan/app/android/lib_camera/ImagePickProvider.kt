package liyihuan.app.android.lib_camera

import android.content.Context
import android.content.pm.ProviderInfo
import androidx.core.content.FileProvider

/**
 * @ClassName: ImagePickProvider
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:41
 */
class ImagePickProvider : FileProvider() {

    override fun attachInfo(context: Context, info: ProviderInfo) {
        super.attachInfo(context, info)
        ImageChooseHelper.init(context)
    }
}