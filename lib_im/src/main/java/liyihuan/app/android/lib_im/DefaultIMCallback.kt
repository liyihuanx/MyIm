package liyihuan.app.android.lib_im

import android.util.Log
import liyihuan.app.android.lib_im.ImCallback

/**
 * @ClassName: DefaultIMCallback
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/21 20:26
 */
class DefaultIMCallback(private val suc: String = "") : ImCallback {
    override fun onSuc() {
        Log.d("QWER", "onSuc: $suc")
    }

    override fun onFail(code: Int, msg: String?) {
        Log.d("QWER", "onFail: $code -- $msg")
    }
}