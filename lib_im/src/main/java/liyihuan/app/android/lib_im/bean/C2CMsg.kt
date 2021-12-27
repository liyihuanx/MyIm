package liyihuan.app.android.lib_im.bean

import android.util.Log
import com.google.gson.annotations.Expose
import com.tencent.imsdk.*
import liyihuan.app.android.lib_im.MsgType
import liyihuan.app.android.lib_im.base.BaseMsgBean

/**
 * @ClassName: C2CMsg
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/21 20:39
 */
abstract class C2CMsg<T> : BaseMsgBean() {

    @Expose(serialize = false)
    var msgContent: T? = null

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

class SoundC2CMsg() : C2CMsg<SoundC2CMsg.SoundInfo>() {
    constructor(soundInfo: SoundInfo) : this() {
        val elem = TIMSoundElem()
        elem.path = soundInfo.path
        elem.duration = soundInfo.duration
        mTxMessage.addElement(elem)
    }

    override fun getAction() = MsgType.C2C_SOUND
    override fun addMsgContent(mTxMessage: TIMMessage): BaseMsgBean {
        val timSoundElem = mTxMessage.getElement(0) as TIMSoundElem
        var soundPath = ""
        timSoundElem.getUrl(object : TIMValueCallBack<String> {
            override fun onError(code: Int, desc: String?) {
            }

            override fun onSuccess(path: String) {
                soundPath = path
            }

        })
        msgContent = SoundInfo(soundPath, timSoundElem.duration)
        return this
    }

    data class SoundInfo(
        val path: String = "",
        val duration: Long = 0
    )

}

