package liyihuan.app.android.lib_im.parser

import android.util.Log
import com.tencent.imsdk.*
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.base.BaseMsgParser
import liyihuan.app.android.lib_im.base.IBaseMsgBean
import liyihuan.app.android.lib_im.example.PkReqMsg
import liyihuan.app.android.lib_im.example.TextC2CMsg
import liyihuan.app.android.lib_im.utils.TypeUtils
import org.json.JSONObject

/**
 * @author liyihuan
 * @date 2021/12/21
 * @Description
 */
open class C2CMsgParser : BaseMsgParser {

    override fun parseMsg(msg: TIMMessage): IBaseMsgBean? {
        var been: BaseMsgBean? = null
        val ele = msg.getElement(0)

        when (ele.type) {
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
                    been = TypeUtils.gson.fromJson(dataJson, classType)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            TIMElemType.Text -> {
                var e: TIMTextElem? = null
                e = ele as TIMTextElem?
                if (e == null) {
                    return null
                }
                try {
                    val dataJson = e.text
                    val jb: JSONObject = JSONObject(dataJson)
                    val userAction = jb.opt("userAction").toString()
                    Log.d("QWER", "${ele.type} --> parseMsg: ${dataJson}")
                    val classType = when (userAction) {
                        MsgType.C2C_TEXT -> TextC2CMsg::class.java
                        else -> null
                    } ?: return null
                    been = TypeUtils.gson.fromJson(dataJson, classType)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            TIMElemType.Face -> {
                var e: TIMFaceElem? = null
                e = ele as TIMFaceElem?
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
                    been = TypeUtils.gson.fromJson(dataJson, classType)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            TIMElemType.Image -> {
                var e: TIMImageElem? = null
                e = ele as TIMImageElem?
                if (e == null) {
                    return null
                }
                try {
                    val dataJson = e.path
                    val jb: JSONObject = JSONObject(dataJson)
                    val userAction = jb.opt("userAction").toString()
                    Log.d("QWER", "${ele.type} --> parseMsg: ${dataJson}")
                    val classType = when (userAction) {
                        "ACTION_ID" -> PkReqMsg::class.java
                        else -> null
                    } ?: return null
                    been = TypeUtils.gson.fromJson(dataJson, classType)
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