package liyihuan.app.android.myim.dialog

import android.content.DialogInterface
import android.util.Log
import liyihuan.app.android.myim.AppStateDistribute
import liyihuan.app.android.myim.R
import liyihuan.app.android.myim.UserState

/**
 * @ClassName: RechargeDialog
 * @Description: java类作用描述
 * @Author: liyihuan
 * @Date: 2021/12/29 21:49
 */
class RechargeDialog : BaseDialogFragment(), AppStateDistribute.UserStateListener {
    override fun getViewLayoutId(): Int {
        return R.layout.dialog_recharge
    }

    override fun initView() {
        AppStateDistribute.addListener(this)
        AppStateDistribute.onUserStateChange(UserState.RECHARGE.state)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        AppStateDistribute.onUserStateChange(UserState.RECHARGE_FINISH.state)
        AppStateDistribute.removeListener(this)
    }

    override fun onUserStateChange(state: String) {
        super.onUserStateChange(state)
        Log.d("QWER", "当前状态: $state")
    }
}