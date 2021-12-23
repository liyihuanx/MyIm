package liyihuan.app.android.lib_im.base

import com.google.gson.annotations.Expose
import com.tencent.imsdk.TIMMessage
import com.tencent.imsdk.TIMUserProfile
import com.tencent.imsdk.TIMValueCallBack
import liyihuan.app.android.lib_im.EnvironmentConfig
import liyihuan.app.android.lib_im.UserInfoManager

/**
 * @ClassName: BaseMsgBean
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/20 22:15
 */
abstract class BaseMsgBean : TIMMessage() {

    // 用户信息
    var userId: String = UserInfoManager.userid
    var nickName: String = UserInfoManager.username
    var headPic: String = UserInfoManager.userHeader

    /**
     * 不需要序列化和反序列化
     */
    @Expose(serialize = false, deserialize = false)
    var mTxMessage: TIMMessage = TIMMessage()

    // 自己给消息定的ID
    var userAction: String = "-1000"

}

// 创建一个消息的共同变量
//  TIMMessage(消息体) 用来添加元素
//  UserInfo(用户信息)
//  createMsg(创建消息的方法) -->
//  TIMElem (消息泛型元素泛型)
//  paramBean(携带的泛型参数)

// 不是C2C消息的话 ---> 能不能给C2C消息也加上？？
// userAction(消息的类型)
