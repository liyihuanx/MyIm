package liyihuan.app.android.lib_im.base

import com.tencent.imsdk.TIMMessage

/**
 * @ClassName: BaseMsgParser
 * @Description: 解析器基类
 * @Author: liyihuan
 * @Date: 2021/12/20 22:15
 */
interface BaseMsgParser {
    /**
     * 解析方法
     * TIMMessage 代表每个消息
     * BaseMsgBean 是自己的消息的基类
     * 就是把腾讯的消息 转换成 自己的消息
     */
    fun parseMsg(msg: TIMMessage): IBaseMsgBean?
}