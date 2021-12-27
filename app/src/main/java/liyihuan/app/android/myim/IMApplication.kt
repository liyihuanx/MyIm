package liyihuan.app.android.myim

import android.app.Application
import android.content.Context
import liyihuan.app.android.lib_im.IMHelp
import liyihuan.app.android.lib_im.IMManager

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description
 */
class IMApplication : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        IMManager.init(this)
        appContext = this
    }
}