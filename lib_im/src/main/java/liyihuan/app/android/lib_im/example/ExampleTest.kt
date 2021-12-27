package liyihuan.app.android.lib_im.example

import liyihuan.app.android.lib_im.IMManager
import liyihuan.app.android.lib_im.ImActionMsgListener
import liyihuan.app.android.lib_im.bean.PKRejectMsg

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description 怎么接收消息
 */
class ExampleTest {

    /**
     * 能不能做成自动反注册的
     */
    val imActionMsgListener = ImActionMsgListener()
    fun onResume() {
        IMManager.addC2CListener(imActionMsgListener)
    }

    fun onDestroy() {
        IMManager.removeC2CListener(imActionMsgListener)
    }

    fun solveMsg() {
        imActionMsgListener.onOptAction<String>("ACTION_ID") {
            // 接收到消息的数据再处理
        }
    }

    fun buildPkMsg(){
        val pkMsgParam = PKRejectMsg.PkMsgParam("拒绝", false)
        PKRejectMsg().createMsg(pkMsgParam)
    }
}