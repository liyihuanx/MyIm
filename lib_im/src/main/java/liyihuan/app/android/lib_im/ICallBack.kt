package liyihuan.app.android.lib_im

/**
 * @ClassName: ICallBack
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/20 22:28
 */
interface ICallBack {
    fun onSuc()
    fun onFail(code: Int, msg: String?)
}