package liyihuan.app.android.myim

import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.base.BaseMsgInterceptor

/**
 * @ClassName: GroupMsgInterceptor
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/29 23:29
 */
class GroupMsgInterceptor : BaseMsgInterceptor, AppStateDistribute.UserStateListener {

    init {
        AppStateDistribute.addListener(this)
    }

    var isInterceptor = false

    override fun onUserStateChange(state: String) {
        if (state == UserState.UserStateBegin.state) {
            isInterceptor = true
        }
    }


    override fun onInterceptor(msg: BaseMsgBean): Boolean {
        return isInterceptor
    }
}