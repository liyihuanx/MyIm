package liyihuan.app.android.lib_im.parser

import android.util.Log
import com.tencent.imsdk.*
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.base.BaseMsgParser
import liyihuan.app.android.lib_im.bean.ImageC2CMsg
import liyihuan.app.android.lib_im.bean.TextC2CMsg
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
        return when (ele.type) {
            TIMElemType.Text -> TextC2CMsg().decorateMsg(msg)
            TIMElemType.Image -> ImageC2CMsg().decorateMsg(msg)
            else -> {
                null
            }
        }
    }
}


//TIMElemType.Custom -> {
//    var e: TIMCustomElem? = null
//    e = ele as TIMCustomElem?
//    if (e == null) {
//        return null
//    }
//    try {
//        val dataJson = String(e.data)
//        val jb: JSONObject = JSONObject(dataJson)
//        val userAction = jb.opt("userAction").toString()
//        Log.d("QWER", "${ele.type} --> parseMsg: ${dataJson}")
//        val classType = when (userAction) {
//            "ACTION_ID" -> PkReqMsg::class.java
//            else -> null
//        } ?: return null
//        been = TypeUtils.fromJson(dataJson, classType)
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//}