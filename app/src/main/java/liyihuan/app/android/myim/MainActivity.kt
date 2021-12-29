package liyihuan.app.android.myim

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.PermissionChecker
import kotlinx.android.synthetic.main.activity_main.*
import liyihuan.app.android.myim.ui.voice.IVoiceRecord
import liyihuan.app.android.lib_camera.PicPickHelper
import liyihuan.app.android.lib_camera.PickCallback
import liyihuan.app.android.lib_im.*
import liyihuan.app.android.lib_im.bean.ImageC2CMsg
import liyihuan.app.android.lib_im.bean.PkReqMsg
import liyihuan.app.android.lib_im.bean.SoundC2CMsg
import liyihuan.app.android.lib_im.bean.TextC2CMsg
import liyihuan.app.android.lib_im.utils.TypeUtils
import liyihuan.app.android.myim.ui.voice.PlayEngine
import liyihuan.app.android.myim.ui.voice.RecorderHelper

class MainActivity : AppCompatActivity() {
    val imActionMsgListener = ImActionMsgListener()

    /**
     * 语音录制helper
     */
    private val recorder by lazy {
        RecorderHelper()
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInfo.text = "我是：${UserInfoManager.nickName}"
        IMManager.addC2CListener(imActionMsgListener)
        IMManager.login(
            UserInfoManager.userid,
            GenerateTestUserSig.genTestUserSig(UserInfoManager.userid)
        )


        getMsgContent()


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


        btnSendSound.setOnTouchListener { v, event ->
            var y = 0f
            val offSetY = 200f // 取消发送需要的最小距离
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    y = event.y // 获取按下的位置
                    voiceView.visibility = View.VISIBLE
                    voiceView.showRecordStart()
                    recorder.startRecording()

                }
                MotionEvent.ACTION_MOVE -> {
                    val currentY = event.y
                    if (y - currentY > offSetY) {
                        voiceView.showRecordCancel()
                    } else {
                        voiceView.showRecordIng()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    voiceView.visibility = View.GONE
                    voiceView.showRecordCancel()
                    recorder.stopRecording()
                    Log.d("QWER", "onCreate: ${recorder.filePath}")
                    IMManager.sendMessage(SoundC2CMsg(SoundC2CMsg.SoundInfo(recorder.filePath, recorder.getTimeInterval())))
                }
            }
            true
        }


        btnCustomer.setOnClickListener {
            val pkReqMsg = PkReqMsg()
            val pkMsgParam = PkReqMsg.PkMsgParam("pk消息", false, "12345")
            pkReqMsg.createMsg(pkMsgParam)
            IMManager.sendMessage(pkReqMsg)
        }
    }

    private fun getMsgContent() {
        imActionMsgListener.onOptAction<TextC2CMsg>(MsgType.C2C_TEXT) {
            Log.d("QWER", "收到文本消息: ${TypeUtils.toJson(it)}")
        }

        imActionMsgListener.onOptAction<ImageC2CMsg>(MsgType.C2C_IMAGE) {
            Log.d("QWER", "收到图片消息: ${TypeUtils.toJson(it)}")
        }

        imActionMsgListener.onOptAction<SoundC2CMsg>(MsgType.C2C_SOUND) {
            Log.d("QWER", "收到语音消息: ${TypeUtils.toJson(it)}")
//            PlayEngine.play(it.msgContent?.path)
        }

        imActionMsgListener.onOptAction<PkReqMsg>(MsgType.CUSTOM_PK_REQ) {
            Log.d("QWER", "收到自定义消息: ${TypeUtils.toJson(it.paramBean)}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        IMManager.removeC2CListener(imActionMsgListener)
    }
}