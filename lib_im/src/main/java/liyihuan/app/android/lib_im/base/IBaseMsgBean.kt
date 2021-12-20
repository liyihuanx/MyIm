package liyihuan.app.android.lib_im.base

import com.tencent.imsdk.TIMMessage

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
}