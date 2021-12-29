package liyihuan.app.android.lib_im.parser

import android.util.Log
import com.tencent.imsdk.*
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.base.BaseMsgParser
import liyihuan.app.android.lib_im.bean.*
import liyihuan.app.android.lib_im.utils.TypeUtils
import org.json.JSONObject

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description
 */
open class C2CMsgParser : BaseMsgParser {

    override fun parseMsg(msg: TIMMessage): BaseMsgBean? {
        // 获取第一个消息元素，这是你自己添加
        val ele = msg.getElement(0)
        val java = TextC2CMsg::class.java
        val c2CMsg = when (ele.type) {
            TIMElemType.Text -> TextC2CMsg::class.java
            TIMElemType.Image -> ImageC2CMsg::class.java
            TIMElemType.Sound -> SoundC2CMsg::class.java
            TIMElemType.Custom -> {
                var e: TIMCustomElem? = null
                e = ele as TIMCustomElem?
                if (e == null) {
                    return null
                }
                // 先拿到数据
                val dataJson = String(e.data)
                val jb: JSONObject = JSONObject(dataJson)

                // 拿到自定义的userAction标识
                var userAction = jb.optString("msgAction")
                // 根据userAction找到对应的Bean类
                when (userAction) {
                    MsgType.CUSTOM_PK_REQ -> PkReqMsg::class.java
                    MsgType.CUSTOM_PK_REJECT -> PKRejectMsg::class.java
                    else -> null
                }
            }

            else -> {
                null
            }
        }
        return c2CMsg?.newInstance()?.decorateMsg(msg)

    }
}


