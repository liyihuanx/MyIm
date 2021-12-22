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
            val textC2CMsg = TextC2CMsg()
            textC2CMsg.createMsg("${UserInfoManager.userid} 发送了一条消息给 ${UserInfoManager.receiverid}")
            IMManager.sendC2CTextMessage(
                UserInfoManager.receiverid,
                textC2CMsg
            )
        }

        btnSendPic.setOnClickListener {
            PicPickHelper(this).show(Size(1, 2), object : PickCallback {
                override fun onSuccess(pathList: MutableList<String>) {
                    val result = pathList[0]
                    Log.d("QWER", "返回照片: $result")

                    val imageC2CMsg = ImageC2CMsg()
                    imageC2CMsg.createMsg(ImageC2CMsg.ImageParam(result))
                    IMManager.sendC2CPicMessage(UserInfoManager.receiverid, imageC2CMsg, result)
                }

            })
        }

        imActionMsgListener.onOptAction<TextC2CMsg>(MsgType.C2C_TEXT) {
            Log.d("QWER", "收到消息: ${it.msgParamBean}")
        }

//        lifecycle.addObserver()
    }

    override fun onDestroy() {
        super.onDestroy()
        IMManager.removeC2CListener(imActionMsgListener)
    }
}