package liyihuan.app.android.myim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import liyihuan.app.android.lib_camera.PicPickHelper
import liyihuan.app.android.lib_camera.PickCallback
import liyihuan.app.android.lib_camera.Size
import liyihuan.app.android.lib_im.*
import liyihuan.app.android.lib_im.bean.ImageC2CMsg
import liyihuan.app.android.lib_im.bean.TextC2CMsg
import liyihuan.app.android.lib_im.utils.TypeUtils

class MainActivity : AppCompatActivity() {
    val imActionMsgListener = ImActionMsgListener()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tvInfo.text = "我是：${UserInfoManager.username}"
        IMManager.addC2CListener(imActionMsgListener)

        IMManager.login(
            UserInfoManager.userid,
            GenerateTestUserSig.genTestUserSig(UserInfoManager.userid)
        )

        btnSend.setOnClickListener {
            IMManager.sendMessage(TextC2CMsg("${UserInfoManager.userid} 发送了一条消息给 ${UserInfoManager.receiverid}"))
        }

        btnSendPic.setOnClickListener {
            PicPickHelper(this).show(null, object : PickCallback {
                override fun onSuccess(pathList: MutableList<String>) {
                    val path = pathList[0]
                    IMManager.sendMessage(ImageC2CMsg(path))
                }

            })
        }

        imActionMsgListener.onOptAction<TextC2CMsg>(MsgType.C2C_TEXT) {
            Log.d("QWER", "收到文本消息: ${TypeUtils.toJson(it)}")
        }

        imActionMsgListener.onOptAction<ImageC2CMsg>(MsgType.C2C_IMAGE) {
            Log.d("QWER", "收到图片消息: ${TypeUtils.toJson(it)}")
        }

//        lifecycle.addObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        IMManager.removeC2CListener(imActionMsgListener)
    }
}