package liyihuan.app.android.lib_im.base

/**
 * @ClassName: BaseMsgInterceptor
 * @Description: 消息拦截器
 * @Author: liyihuan
 * @Date: 2021/12/20 22:21
 */
interface BaseMsgInterceptor {
    /**
     * 可在消息解析前做拦截
     * true拦截，false不拦截
     */
    fun onInterceptor(msg: IBaseMsgBean): Boolean
}