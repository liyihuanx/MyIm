package liyihuan.app.android.lib_im.base

import com.google.gson.annotations.Expose
import com.tencent.imsdk.TIMMessage
import com.tencent.imsdk.TIMUserProfile
import com.tencent.imsdk.TIMValueCallBack

/**
 * @ClassName: BaseMsgBean
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/20 22:15
 */
abstract class BaseMsgBean : IBaseMsgBean {
    open var userAction = "-1000"
    /**
     * 不需要序列化和反序列化
     */
    @Expose(serialize = false, deserialize = false)
    open var mTxMessage: TIMMessage = TIMMessage()

    fun setTxMsg(txMessage: TIMMessage) {
        mTxMessage = txMessage
    }

    override fun getAction(): String {
        return userAction
    }

    override fun getTimMsg(): TIMMessage {
        return mTxMessage
    }


    open fun getSenderProfile(callBack: TIMValueCallBack<TIMUserProfile?>) {
        mTxMessage.getSenderProfile(callBack)
    }

}