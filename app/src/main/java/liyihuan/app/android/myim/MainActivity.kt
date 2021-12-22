package liyihuan.app.android.myim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import liyihuan.app.android.lib_im.*
import liyihuan.app.android.lib_im.bean.TextC2CMsg

class MainActivity : AppCompatActivity() {
    val imActionMsgListener = ImActionMsgListener()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tvInfo.text = "我是：${UserInfoManager.username}"
        IMManager.addC2CListener(imActionMsgListener)

        btnLogin.setOnClickListener {
            IMManager.login(
                UserInfoManager.userid,
                GenerateTestUserSig.genTestUserSig(UserInfoManager.userid)
            )
        }

        btnSend.setOnClickListener {
            val textC2CMsg = TextC2CMsg()
            textC2CMsg.createMsg("${UserInfoManager.userid} 发送了一条消息给 ${UserInfoManager.receiverid}")
            IMManager.sendC2CTextMessage(
                UserInfoManager.receiverid,
                textC2CMsg
            )
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