package liyihuan.app.android.myim.ui.voice

import android.text.TextUtils
import java.io.File
import java.io.IOException

/**
 * @ClassName: FileUtil
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/27 22:02
 */
object FileUtil {


    fun createFile(path: String): File? {
        var file: File? = null
        if (!TextUtils.isEmpty(path)) {
            try {
                file = File(path)
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                if (file.exists()) {
                    file.delete()
                }
                file.createNewFile()
            } catch (var3: IOException) {
                var3.printStackTrace()
            }
        }
        return file
    }
}