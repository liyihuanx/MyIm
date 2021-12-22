package liyihuan.app.android.lib_camera

import android.Manifest
import android.app.Dialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import liyihuan.app.android.lib_camera.permissions.RxPermissions
import java.lang.ref.WeakReference

/**
 * @ClassName: PicPickHelper
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/5/27 21:43
 */
class PicPickHelper(activity: FragmentActivity) {
    private var activityWeakReference = WeakReference(activity)
    private val photoRequestFragment by lazy { getPhotoRequstFragment(activity) }
    private val rxPermissions by lazy { RxPermissions(activity) }

    /**
     * 使用默认选择弹窗
     */
    fun show(size: Size?, callback: PickCallback) {
        photoRequestFragment.callback = callback
        photoRequestFragment.size = size
        val bottomDialog = activityWeakReference.get()?.let {
            Dialog(
                it,
                R.style.BottomViewWhiteMask
            )
        }
        val contentView = LayoutInflater.from(activityWeakReference.get())
            .inflate(R.layout.dialog_choose_pic, null)
        bottomDialog?.setCancelable(true)
        bottomDialog?.setCanceledOnTouchOutside(true)
        bottomDialog?.setContentView(contentView)
        bottomDialog?.show()

        val imageChooseListener = View.OnClickListener { v ->
            bottomDialog?.dismiss()
            when (v.id) {
                R.id.view1 -> {
                    fromCamera()
                }
                R.id.view2 -> {
                    fromLocal()
                }
            }
        }

        contentView.findViewById<TextView>(R.id.view1).setOnClickListener(imageChooseListener)
        contentView.findViewById<TextView>(R.id.view2).setOnClickListener(imageChooseListener)
        contentView.findViewById<TextView>(R.id.view3).setOnClickListener(imageChooseListener)

        val attributes = bottomDialog?.window?.attributes
        attributes?.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes?.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes?.gravity = Gravity.BOTTOM
        bottomDialog?.window?.attributes = attributes
    }


    /**
     * 打开相机选择
     */
    fun fromCamera(size: Size?, callback: PickCallback) {
        photoRequestFragment.callback = callback
        photoRequestFragment.size = size
        fromCamera()
    }

    /**
     * 打开相册
     */

    fun fromLocal(size: Size?, callback: PickCallback) {
        photoRequestFragment.callback = callback
        photoRequestFragment.size = size
        fromLocal()
    }


    // 获取Fragment的方法
    private fun getPhotoRequstFragment(activity: FragmentActivity): PhotoRequestFragment {
        // 查询是否已经存在了该Fragment，这样是为了让该Fragment只有一个实例
        var rxPermissionsFragment: PhotoRequestFragment? = findPhotoRequstFragment(activity)
        val isNewInstance = rxPermissionsFragment == null
        // 如果还没有存在，则创建Fragment，并添加到Activity中
        if (isNewInstance) {
            rxPermissionsFragment = PhotoRequestFragment()
            val fragmentManager = activity.supportFragmentManager

            fragmentManager
                .beginTransaction()
                .add(rxPermissionsFragment, TAG)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }

        return rxPermissionsFragment!!
    }

    // 利用tag去找是否已经有该Fragment的实例
    private fun findPhotoRequstFragment(activity: FragmentActivity): PhotoRequestFragment? {
        return activity.fragmentManager.findFragmentByTag(TAG) as PhotoRequestFragment?
    }


    private fun fromCamera() {
        activityWeakReference.get()?.let {
            rxPermissions.requestEachCombined(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
                .subscribe { permission ->
                    if (permission.granted) {//全部同意后调用
                        val mCameraFilePath = ImageChooseHelper.cameraFilePath
                        photoRequestFragment.mCameraFilePath = mCameraFilePath
                        photoRequestFragment.startActivityForResult(
                            ImageChooseHelper.takePhotoIntent(
                                it,
                                mCameraFilePath!!
                            ), REQUEST_CODE_CAMERA
                        )

                    } else if (permission.shouldShowRequestPermissionRationale) {//只要有一个选择：禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                        photoRequestFragment.callback?.onPermissonNotGet(permission.name)
                    } else {//只要有一个选择：禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                        photoRequestFragment.callback?.onPermissonNotGet(permission.name)
                    }
                }
        }
    }


    private fun fromLocal() {
        activityWeakReference.get()?.let {
            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe { permission ->
                    if (permission.granted) {//全部同意后调用
                        photoRequestFragment.startActivityForResult(
                            ImageChooseHelper.pickImageIntent(),
                            REQUEST_CODE_CHOOSE_LOCAL
                        )

                    } else if (permission.shouldShowRequestPermissionRationale) {//只要有一个选择：禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                        photoRequestFragment.callback?.onPermissonNotGet(permission.name)
                    } else {//只要有一个选择：禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                        photoRequestFragment.callback?.onPermissonNotGet(permission.name)
                    }
                }
        }
    }


    companion object {
        val REQUEST_CODE_CHOOSE_LOCAL = RequestCodeCreator.generate()
        val REQUEST_CODE_CAMERA = RequestCodeCreator.generate()
        val REQUEST_CODE_CROP = RequestCodeCreator.generate()


        val TAG = "PhotoRequestFragment"
    }
}
