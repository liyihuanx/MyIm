package liyihuan.app.android.lib_im.bean

import liyihuan.app.android.lib_im.base.BaseGroupMsgBean

/**
 * @ClassName: GroupMsgBean
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/20 22:57
 */
class GroupMsgBean : BaseGroupMsgBean() {
    var peerIdPositiveSend = ""


    override fun getGroupId(): String {
        if (peerIdPositiveSend.isNotEmpty()) {
            return peerIdPositiveSend
        }
        return mTxMessage.conversation?.peer ?: ""
    }
}