package liyihuan.app.android.lib_im.base

/**
 * @ClassName: GroupMsgBean
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/20 22:32
 */
abstract class BaseGroupMsgBean : BaseMsgBean() {
    /**
     * 获取群组id
     */
    abstract fun getGroupId(): String
}