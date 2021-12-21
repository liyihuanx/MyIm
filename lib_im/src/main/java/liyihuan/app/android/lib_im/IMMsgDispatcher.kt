package liyihuan.app.android.lib_im

import com.tencent.imsdk.TIMConversationType
import com.tencent.imsdk.TIMMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import liyihuan.app.android.lib_im.base.BaseImFactory
import liyihuan.app.android.lib_im.base.BaseMsgInterceptor
import liyihuan.app.android.lib_im.base.IBaseMsgBean
import liyihuan.app.android.lib_im.parser.C2CMsgParser
import liyihuan.app.android.lib_im.parser.GroupMsgParser
import liyihuan.app.android.lib_im.parser.SystemMsgParser

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description 消息处理分发器
 */
object IMMsgDispatcher {
    val sysImParsers = ArrayList<SystemMsgParser>()
    val goupImParsers = ArrayList<GroupMsgParser>()
    val c2cMsgParsers = ArrayList<C2CMsgParser>()

    /**
     * 系统消息拦截器
     * 用于外外层拦截 如果消息被处理页面就不会收到
     */
    var systemMsgInterceptor: BaseMsgInterceptor? = null
    var groupMsgInterceptor: BaseMsgInterceptor? = null
    var c2cMsgInterceptor: BaseMsgInterceptor? = null

    /**
     *  消息工厂，中间人
     *  真正把消息扔出去的地方，所有需要监听消息的页面就加入到list中
     */
    val groupImFactory = ArrayList<BaseImFactory>()
    val c2cImFactory = ArrayList<BaseImFactory>()
    val systemImFactory = ArrayList<BaseImFactory>()




    /**
     * 处理接收到的消息
     * 解析，拦截，分发
     */
    fun onDispatchMsg(msg: TIMMessage) {
        GlobalScope.launch(Dispatchers.Main) {
            when (msg.conversation?.type) {
                TIMConversationType.System -> {
                    var asyncBeen: IBaseMsgBean? = null
                    // 1.解析消息
                    val job = async {
                        sysImParsers.forEach {
                            asyncBeen = it.parseMsg(msg)
                            if (asyncBeen !== null) {
                                return@forEach
                            }
                        }
                        asyncBeen
                    }
                    // 2.拦截消息
                    val been = job.await() ?: return@launch
                    if (systemMsgInterceptor?.onInterceptor(been) == true) {
                        return@launch
                    }
                    // 3.分发消息
                    systemImFactory.forEach {
                        it.onNewMsg(been)
                    }
                }

                TIMConversationType.Group -> {

                    var asyncBeen: IBaseMsgBean? = null
                    val job = async {
                        goupImParsers.forEach {
                            asyncBeen = it.parseMsg(msg)
                            if (asyncBeen !== null) {
                                return@forEach
                            }
                        }
                        asyncBeen
                    }

                    val been = job.await() ?: return@launch
                    if (groupMsgInterceptor?.onInterceptor(been!!) == true) {
                        return@launch
                    }
                    groupImFactory.forEach {
                        it.onNewMsg(been)
                    }
                }

                TIMConversationType.C2C -> {
                    val job = async {
                        var asyncBeen: IBaseMsgBean? = null
                        c2cMsgParsers.forEach {
                            asyncBeen = it.parseMsg(msg)
                            if (asyncBeen !== null) {
                                return@forEach
                            }
                        }
                        asyncBeen
                    }
                    val been = job.await() ?: return@launch
                    if (c2cMsgInterceptor?.onInterceptor(been) == true) {
                        return@launch
                    }
                    c2cImFactory.forEach {
                        it.onNewMsg(been)
                    }
                }

                else -> {

                }
            }
        }
    }
}