package liyihuan.app.android.lib_im

import liyihuan.app.android.lib_im.base.BaseImFactory
import liyihuan.app.android.lib_im.base.IBaseMsgBean
import java.util.HashMap

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description 存放在 ArrayList<BaseImListener> 列表中
 */
class ImActionMsgListener : BaseImFactory {

    /**
     * 以后可以弄成LRUCache队列的
     */
    private val hashMap: HashMap<String, Function1<Any, Unit>> =
        HashMap<String, Function1<Any, Unit>>()

    fun <T> onOptAction(
        action: String,
        call: Function1<T, Unit>,
    ): ImActionMsgListener {
        hashMap[action] = call as Function1<Any, Unit>
        return this
    }


    override fun onNewMsg(msg: IBaseMsgBean) {
        val function1: Function1<Any, Unit>? = hashMap[msg.getAction()]
        function1?.invoke(msg)
    }

}