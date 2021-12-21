package liyihuan.app.android.lib_im

import android.util.Log
import com.tencent.imsdk.TIMMessage
import com.tencent.imsdk.TIMValueCallBack

/**
 * @ClassName: TIMValueCallBackWarp
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/21 20:34
 */
class TIMValueCallBackWarp(private val callback: ImCallback?) : TIMValueCallBack<TIMMessage> {

    /**
     * 出错时回调
     *
     * @param code 错误码，详细描述请参见错误码表
     * @param desc 错误描述
     */
    override fun onError(code: Int, desc: String?) {
        callback?.onFail(code, desc)
        if(code==6071 ){
//            IMManager.reLogin(null)
        }
    }

    /**
     * 成功时回调
     */
    override fun onSuccess(t: TIMMessage?) {
        callback?.onSuc()
    }
}