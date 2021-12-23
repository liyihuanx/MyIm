package liyihuan.app.android.lib_im.base

import com.tencent.imsdk.TIMMessage
import com.tencent.imsdk.TIMUserProfile
import com.tencent.imsdk.TIMValueCallBack

/**
 * @ClassName: IBaseMsg
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/20 22:25
 */
interface IBaseMsgBean {

    /**
     * 获取消息
     */
    fun getTimMsg(): TIMMessage

    /**
     * 消息携带的指令Id
     */
    fun getAction(): String


    /**
     * 是否是自己发送的消息
     */
    fun isSelf(): Boolean

    /**
     * 获取在会话列表中展示
     */
//    fun getConvShowContent(): String

    /**
     * 获取发送者信息
     */
    fun getSenderProfile(callBack: TIMValueCallBack<TIMUserProfile>)
}