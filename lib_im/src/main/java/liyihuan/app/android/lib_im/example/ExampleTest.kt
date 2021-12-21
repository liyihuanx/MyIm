package liyihuan.app.android.lib_im.example

import liyihuan.app.android.lib_im.IMMsgDispatcher
import liyihuan.app.android.lib_im.ImActionMsgListener
import liyihuan.app.android.lib_im.base.IBaseMsgBean

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
        IMMsgDispatcher.addC2CListener(imActionMsgListener)
    }

    fun onDestroy() {
        IMMsgDispatcher.removeC2CListener(imActionMsgListener)
    }

    fun solveMsg() {
        imActionMsgListener.onOptAction<String>("ACTION_ID") {
            // 接收到消息的数据再处理
        }
    }
}