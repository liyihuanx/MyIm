package liyihuan.app.android.lib_im.example

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.utils.TypeUtils

/**
 * @ClassName: PkMsg
 * @Description: 消息bean类的封装使用，系统消息，C2C消息，群聊消息都一个意思
 * @Author: liyihuan
 * @Date: 2021/12/20 23:05
 */
open class BasePkMsg<T> :  BaseMsgBean(){
    var userId = ""
    var nickName = ""
    var headPic = ""
    var param = ""

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
                field = TypeUtils.gson.fromJson<T>(param, t)
            }
            return field
        }
        private set


    open fun createMsg(paramBean: T) {

    }
}

//pk请求
class PkReqMsg : BasePkMsg<PkReqMsg.PkMsgParam>() {

    // String= TCConstants.PK_REQUEST.toString()
    class PkMsgParam {
        var playUrl = ""
        var isAgain = false
        var pk_id = ""
    }

    override fun createMsg(paramBean: PkMsgParam) {
        super.createMsg(paramBean)
        userAction = ""
    }
}

// pk拒接
class PKRejectMsg : BasePkMsg<PKRejectMsg.PkMsgParam>() {

    // String= TCConstants.PK_REJECT.toString()
    class PkMsgParam {
        var reason = ""
        var isAgain = false
    }

    override fun createMsg(paramBean: PkMsgParam) {
        super.createMsg(paramBean)
        userAction = ""
    }
}