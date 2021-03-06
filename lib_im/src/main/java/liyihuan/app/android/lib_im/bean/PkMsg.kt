package liyihuan.app.android.lib_im.bean

import android.util.Log
import com.google.gson.annotations.Expose
import com.tencent.imsdk.TIMCustomElem
import com.tencent.imsdk.TIMMessage
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.utils.TypeUtils
import org.json.JSONObject

/**
 * @ClassName: PkMsg
 * @Description: 消息bean类的封装使用，系统消息，C2C消息，群聊消息都一个意思
 * @Author: liyihuan
 * @Date: 2021/12/20 23:05
 */
abstract class BaseCustomMsg<T> : BaseMsgBean() {

    private var param = ""

    /**
     * pk这一种消息就可以分为多种：
     *   1.请求Pk：可以携带一种参数体（pk人，pk地址，pk各种信息....）
     *   2.拒接Pk：又可以携带一种参数体（pk拒绝原因，pk拒绝的人，拒绝的是谁....）
     *   3.pk消息3：又一种参数体
     *   4.pk.....
     * 所有参数体最终都是存在paramBean中
     */
    @Expose(serialize = false)
    var paramBean: T? = null
        get() {
            if (field == null) {
                val t = TypeUtils.findNeedType(javaClass)
                field = TypeUtils.fromJson<T>(param, t)
            }
            return field
        }
        private set

    open fun createMsg(paramBean :T){
        // 新建一个传递的数据对象
        val dataObj = JSONObject()
        dataObj.put("msgAction", getAction())
        // 转成String
        param = TypeUtils.toJson(paramBean)

        // 放入value保存
        dataObj.put("value", param)

        val timCustomElem = TIMCustomElem()
        timCustomElem.data = dataObj.toString().toByteArray()

        mTxMessage.addElement(timCustomElem)
    }

    override fun addMsgContent(mTxMessage: TIMMessage): BaseMsgBean {
        val element = mTxMessage.getElement(0) as TIMCustomElem
        val dataJson = String(element.data)
        val jsonObject = JSONObject(dataJson)
        msgAction = jsonObject.optString("msgAction")
        param = jsonObject.optString("value")
        return this
    }
}

//pk请求
class PkReqMsg : BaseCustomMsg<PkReqMsg.PkMsgParam>() {

    data class PkMsgParam(
        var playUrl: String = "",
        var isAgain: Boolean = false,
        var pk_id: String = ""
    )

    override fun getAction() = MsgType.CUSTOM_PK_REQ
}

// pk拒接
class PKRejectMsg : BaseCustomMsg<PKRejectMsg.PkMsgParam>() {
    data class PkMsgParam(
        var reason: String = "",
        var isAgain: Boolean = false,
    )

    override fun getAction() = MsgType.CUSTOM_PK_REJECT


}