package liyihuan.app.android.lib_im.bean

import android.util.Log
import com.google.gson.annotations.Expose
import com.tencent.imsdk.*
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.base.BaseMsgBean
import liyihuan.app.android.lib_im.utils.TypeUtils
import java.io.File

/**
 * @ClassName: C2CMsg
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/21 20:39
 */
abstract class C2CMsg<T> : BaseMsgBean() {

    @Expose(serialize = false)
    var msgContent: T? = null


    fun decorateMsg(mTxMessage: TIMMessage): BaseMsgBean {
        // 设置一下消息发送者的信息
        setMessageInfo()
        return addMsgContent(mTxMessage)
    }

    abstract fun addMsgContent(mTxMessage: TIMMessage): BaseMsgBean
}

class TextC2CMsg() : C2CMsg<String>() {
    override fun getAction() = MsgType.C2C_TEXT

    /**
     * 发送消息时用的
     */
    constructor(content: String) : this() {
        val elem = TIMTextElem()
        elem.text = content
        mTxMessage.addElement(elem)
    }


    /**
     * 接受到的消息，包装一下
     */
    override fun addMsgContent(mTxMessage: TIMMessage): BaseMsgBean {
        msgContent = (mTxMessage.getElement(0) as TIMTextElem).text
        return this
    }


}

class ImageC2CMsg() : C2CMsg<String>() {
    constructor(path: String) : this() {
        val elem = TIMImageElem()
        elem.path = path
        mTxMessage.addElement(elem)
    }



    override fun getAction() = MsgType.C2C_IMAGE
    override fun addMsgContent(mTxMessage: TIMMessage): BaseMsgBean {
        val timImageElem = mTxMessage.getElement(0) as TIMImageElem
        timImageElem.imageList.forEach {
            if (it.type == TIMImageType.Thumb) {
                msgContent = it.url
                return@forEach
            }
        }
        return this
    }
}