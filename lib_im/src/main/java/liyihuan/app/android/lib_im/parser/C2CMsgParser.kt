package liyihuan.app.android.lib_im.parser

import android.util.Log
import com.tencent.imsdk.*
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.base.BaseMsgParser
import liyihuan.app.android.lib_im.bean.ImageC2CMsg
import liyihuan.app.android.lib_im.bean.PkReqMsg
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
        var been: BaseMsgBean? = null
        // 获取第一个消息元素，这是你自己添加
        val ele = msg.getElement(0)

        when (ele.type) {
            TIMElemType.Text -> {
                var e: TIMTextElem? = null
                e = ele as TIMTextElem?
                if (e == null) {
                    return null
                }
                try {
                    // 这一串是自定义的Bean中有的参数
                    val dataJson = e.text

                    // 这里只是为了拿userAction
                    val jb: JSONObject = JSONObject(dataJson)
                    val userAction = jb.opt("userAction").toString()

                    Log.d("QWER", "${ele.type} --> parseMsg: ${dataJson}")
                    val classType = when (userAction) {
                        MsgType.C2C_TEXT -> TextC2CMsg::class.java
                        else -> null
                    } ?: return null
                    been = TypeUtils.fromJson(dataJson, classType)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            TIMElemType.Custom -> {
                var e: TIMCustomElem? = null
                e = ele as TIMCustomElem?
                if (e == null) {
                    return null
                }
                try {
                    val dataJson = String(e.data)
                    val jb: JSONObject = JSONObject(dataJson)
                    val userAction = jb.opt("userAction").toString()
                    Log.d("QWER", "${ele.type} --> parseMsg: ${dataJson}")
                    val classType = when (userAction) {
                        "ACTION_ID" -> PkReqMsg::class.java
                        else -> null
                    } ?: return null
                    been = TypeUtils.fromJson(dataJson, classType)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            TIMElemType.Image -> {
                var e: TIMImageElem? = null
                e = ele as TIMImageElem?
                // 转换失败
                if (e == null) {
                    return null
                }
                try {
                    // 拿到图片
                    val imageUrl = e.path
                    Log.d("QWER", "parseMsg: $imageUrl")
                    been = ImageC2CMsg()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }



            else -> {

            }
        }

        been?.setTxMsg(msg)
        return been
    }
}