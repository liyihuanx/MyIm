package liyihuan.app.android.myim.ui.voice

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.base_chat_input_voice_record.view.*
import liyihuan.app.android.myim.R

/**
 * @author created by liyihuanx
 * @date 2021/5/17
 * description: 类的描述
 */
class VoiceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private var frameAnimation: AnimationDrawable? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.base_chat_input_voice_record, this, true)
        setBackgroundResource(R.drawable.bg_voice_sending)
        microphone.setBackgroundResource(R.drawable.animation_voice)
        frameAnimation = microphone.background as AnimationDrawable
    }

    fun showRecordStart() {
        rlSending.visibility = View.VISIBLE
        rlCancelSend.visibility = View.GONE
        frameAnimation?.start()
    }

    fun showRecordIng() {
        showRecordStart()
    }

    fun showRecordCancel() {
        rlSending.visibility = View.GONE
        rlCancelSend.visibility = View.VISIBLE
        frameAnimation?.stop()
    }

}