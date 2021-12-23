package liyihuan.app.android.lib_im.bean

import com.google.gson.annotations.Expose
import com.tencent.imsdk.TIMImageElem
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.utils.TypeUtils

/**
 * @ClassName: C2CMsg
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/21 20:39
 */
abstract class C2CMsg<T> : BaseMsgBean() {

    // 你想携带的参数
    private var msgParam = ""


    @Expose(serialize = false)
    var msgParamBean: T? = null
        get() {
            if (field == null) {
                val t = TypeUtils.findNeedType(javaClass)
                field = TypeUtils.fromJson<T>(msgParam, t)
            }
            return field
        }
        private set


    open fun createMsg(msgParamBean: T) {
        msgParam = TypeUtils.toJson(msgParamBean)
    }
}

/**
 *  接收到的消息类型为：
 * {
 *    "headPic":"",
 *    "msgParam":"\"liyihuan 发送了一条消息给 chenyalun\"",
 *    "nickName":"李逸欢",
 *    "userId":"liyihuan",
 *    "userAction":"1",
 *
 *    "mTxMessage":{ "msg":{} },
 * }
 */
class TextC2CMsg : C2CMsg<String>() {
    override fun createMsg(msgParamBean: String) {
        super.createMsg(msgParamBean)
        userAction = MsgType.C2C_TEXT
    }

}

class ImageC2CMsg : C2CMsg<String>() {

    override fun createMsg(msgParamBean: String) {
        super.createMsg(msgParamBean)
        val imageElem = TIMImageElem()
        imageElem.path = msgParamBean
        mTxMessage.addElement(imageElem)
        userAction = MsgType.C2C_IMAGE
    }
}