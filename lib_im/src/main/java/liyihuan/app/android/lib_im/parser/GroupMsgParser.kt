package liyihuan.app.android.lib_im.parser

import com.tencent.imsdk.TIMMessage
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.base.BaseMsgParser

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description
 */
open class GroupMsgParser : BaseMsgParser {
    override fun parseMsg(msg: TIMMessage): BaseMsgBean? {
        return null
    }
}