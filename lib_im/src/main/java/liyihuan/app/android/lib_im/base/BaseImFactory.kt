package liyihuan.app.android.lib_im.base

/**
 * @ClassName: BaseImListener
 * @Description: 消息主持人，想要收到消息就继承这个接口
 * @Author: liyihuan
 * @Date: 2021/12/20 22:30
 */
interface BaseImFactory {

    fun onNewMsg(msg: BaseMsgBean)

}