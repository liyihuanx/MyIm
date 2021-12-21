package liyihuan.app.android.lib_im.parser

import com.tencent.imsdk.TIMMessage
import liyihuan.app.android.lib_im.base.BaseMsgParser
import liyihuan.app.android.lib_im.base.IBaseMsgBean

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description
 */
open class SystemMsgParser : BaseMsgParser {
    override fun parseMsg(msg: TIMMessage): IBaseMsgBean? {
        return null
    }
}