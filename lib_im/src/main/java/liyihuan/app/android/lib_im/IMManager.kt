package liyihuan.app.android.lib_im

import android.app.Application
import com.google.gson.Gson
import com.tencent.imsdk.*
import com.tencent.imsdk.BuildConfig
import liyihuan.app.android.lib_im.base.BaseImFactory
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.base.BaseMsgInterceptor
import liyihuan.app.android.lib_im.base.IBaseMsgBean
import liyihuan.app.android.lib_im.parser.C2CMsgParser
import liyihuan.app.android.lib_im.parser.GroupMsgParser
import liyihuan.app.android.lib_im.parser.SystemMsgParser

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description
 */
object IMManager {

    var isLogin = false
    var isSetIm = false

    /**
     *  初始化IM
     */
    fun init(context: Application) {
        // IM添加解析器，拦截器
        IMMsgDispatcher.goupImParsers.add(GroupMsgParser())
        IMMsgDispatcher.sysImParsers.add(SystemMsgParser())
        IMMsgDispatcher.c2cMsgParsers.add(C2CMsgParser())

        //群消息拦截
        IMMsgDispatcher.groupMsgInterceptor = object : BaseMsgInterceptor {
            override fun onInterceptor(msg: IBaseMsgBean): Boolean {
                return false
            }
        }
        // 系统消息拦截
        IMMsgDispatcher.systemMsgInterceptor = object : BaseMsgInterceptor {
            override fun onInterceptor(msg: IBaseMsgBean): Boolean {
                return false
            }
        }
        // 私聊消息拦截
        IMMsgDispatcher.c2cMsgInterceptor = object : BaseMsgInterceptor {
            override fun onInterceptor(msg: IBaseMsgBean): Boolean {
                return false
            }
        }


        isSetIm = true
        // IM SDK 基本配置
        val timSdkConfig = TIMSdkConfig(EnvironmentConfig.IMSDK_APPID) // 禁止掉im日志显示
            .setLogLevel(TIMLogLevel.VERBOSE)
            .enableLogPrint(false)
        timSdkConfig.logListener = TIMLogListener { level, tag, msg ->
            //可以通过此回调将 sdk 的 log 输出到自己的日志系统中
            if (BuildConfig.DEBUG) {
            }
        }
        //基本用户配置
        val userConfig = TIMUserConfig()
        //初始化群设置
        userConfig.groupSettings = TIMGroupSettings()
        //禁止服务器自动代替上报已读
        userConfig.disableAutoReport(true)
        userConfig.isReadReceiptEnabled = true
        userConfig.userStatusListener = object : TIMUserStatusListener {
            override fun onForceOffline() {

            }

            override fun onUserSigExpired() {

            }
        }
        userConfig.isReadReceiptEnabled = false


        // 初始化IM
        TIMManager.getInstance().init(context, timSdkConfig).toString()
        TIMManager.getInstance().userConfig = userConfig
        TIMManager.getInstance().addMessageListener { msgs ->
            /**
             * 收到新消息回调
             * @param msgs 收到的新消息
             * @return 正常情况下，如果注册了多个listener, SDK会顺序回调到所有的listener。当碰到listener的回调返回true的时候，将终止继续回调后续的listener。
             */
            msgs?.forEach { msg ->
                IMMsgDispatcher.onDispatchMsg(msg)
            }
            true
        }
    }

    private var lastUid: String = ""
    private var lastSign: String = ""


    fun addC2CListener(imMsgListener: BaseImFactory) {
        if (IMMsgDispatcher.c2cImFactory.contains(imMsgListener)) {
            return
        }
        IMMsgDispatcher.c2cImFactory.add(imMsgListener)
    }

    fun removeC2CListener(imMsgListener: BaseImFactory) {
        IMMsgDispatcher.c2cImFactory.remove(imMsgListener)
    }


    /**
     * 登录
     */
    fun login(uid: String, sign: String, callback: ImCallback = DefaultIMCallback("登录成功")) {
        lastUid = uid
        lastSign = sign
        TIMManager.getInstance().login(uid, sign, object : TIMCallBack {
            override fun onSuccess() {
                isLogin = true
                lastUid = uid
                lastSign = sign
                callback.onSuc()
            }

            override fun onError(p0: Int, p1: String?) {
                callback.onFail(p0, p1)
            }
        })
    }

    /**
     * 发送
     */
    fun sendC2CTextMessage(chatId: String, timMessage: BaseMsgBean, callback: ImCallback = DefaultIMCallback("发送成功")) {
        val json = Gson().toJson(timMessage)
        val timMsg = TIMMessage()
        val elem = TIMTextElem()
        elem.text = json
        timMsg.addElement(elem)
        checkLogin(callback) {
            // 创建一个会话
            val conversation =
                TIMManager.getInstance().getConversation(TIMConversationType.C2C, chatId)
            // 发送消息
            conversation.sendMessage(timMsg, TIMValueCallBackWarp(callback))
        }
    }

    /**
     * 检查是否登录
     */
    private fun checkLogin(callback: ImCallback? = null, function: () -> Unit) {
        if (!isLogin) {
            login(lastUid, lastSign, object : ImCallback {
                override fun onSuc() {
                    function.invoke()
                }

                override fun onFail(code: Int, msg: String?) {
                    callback?.onFail(code, msg)
                }
            })
        } else {
            function.invoke()
        }
    }

}