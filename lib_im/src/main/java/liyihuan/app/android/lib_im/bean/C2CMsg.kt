package liyihuan.app.android.lib_im.example

import com.google.gson.annotations.Expose
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.UserInfoManager
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.utils.TypeUtils

/**
 * @ClassName: C2CMsg
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/21 20:39
 */
open class C2CMsg<T> : BaseMsgBean() {
    var userId = ""
    var nickName = ""
    var headPic = ""
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
        userId = UserInfoManager.userid
        nickName = UserInfoManager.username
        headPic = UserInfoManager.userHeader
        msgParam = TypeUtils.gson.toJson(msgParamBean)
    }


}

/**
 *  接收到的消息类型为：
 * {
 *    "headPic":"",
 *    "msgParam":"\"liyihuan 发送了一条消息给 chenyalun\"",
 *    "nickName":"李逸欢",
 *    "userId":"liyihuan",
 *    "mTxMessage":{ "msg":{}},
 *    "userAction":"1"
 * }
 */
class TextC2CMsg : C2CMsg<String>() {
    override fun createMsg(msgParamBean: String) {
        super.createMsg(msgParamBean)
        userAction = MsgType.C2C_TEXT
    }

}

class ImageC2CMsg : C2CMsg<ImageC2CMsg.ImageParam>() {
    class ImageParam {
        val imageUrl = ""
    }

    override fun createMsg(msgParamBean: ImageParam) {
        super.createMsg(msgParamBean)
        userAction = MsgType.C2C_IMAGE

    }

}