package liyihuan.app.android.lib_camera

/**
 * @ClassName: PickCallback
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:43
 */
interface PickCallback {

    fun onSuccess(pathList: MutableList<String>)
    fun onCancel(){}
    fun onPermissonNotGet(permission: String){}
}