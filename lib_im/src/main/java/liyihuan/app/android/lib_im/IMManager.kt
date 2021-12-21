package liyihuan.app.android.lib_im

import android.app.Application
import com.tencent.imsdk.*

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description
 */
object IMManager {

    var isLogin = false

    /**
     *  初始化IM
     */
    fun init(context: Application, userConfig: TIMUserConfig, config: TIMSdkConfig){
        TIMManager.getInstance().init(context, config).toString()
        TIMManager.getInstance().userConfig =userConfig
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


    /**
     * 登录
     */
    fun login(uid: String, sign: String, callback: ImCallback? = null) {
        lastUid = uid
        lastSign = sign
        TIMManager.getInstance().login(uid, sign, object : TIMCallBack {
            override fun onSuccess() {
                isLogin = true
                lastUid = uid
                lastSign = sign
                callback?.onSuc()
            }

            override fun onError(p0: Int, p1: String?) {
                callback?.onFail(p0,p1)
            }
        })
    }

}