package liyihuan.app.android.myim.ui.voice

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Message
import android.text.TextUtils
import liyihuan.app.android.myim.IMApplication

import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

/**
 * 录音工具
 */
class RecorderHelper {
    /**
     * 获取录音文件地址
     */
    var filePath: String = ""
    private var mRecorder: MediaRecorder? = null
    private var startTime: Long = 0
    var durInterval: Long = 0
        private set
    var isRecording = false
        private set

    var minTimeSpan = 1000
    var maxTimeSpan = 60 * 1000

    private val msgReCordWhat = 12321
    private val msgPlaying = 12311

    private val timeHander = @SuppressLint("HandlerLeak")
    object : android.os.Handler() {
        override fun dispatchMessage(msg: Message) {
            super.dispatchMessage(msg)
            if (msg.what == msgReCordWhat) {

                if (curruntStatus == RecordStatus.recording) {
                    val span = System.currentTimeMillis() - startTime
                    if (span > maxTimeSpan) {
                        stopRecording()
                    }
                    timeCall?.invoke(((span) / 1000).toInt())
                    sendEmptyMessageDelayed(msgReCordWhat, 1000)
                }
            }

            if (msg.what == msgPlaying) {

                if (curruntStatus == RecordStatus.playing) {
                    timeCall?.invoke(((System.currentTimeMillis() - startPlayingTime) / 1000).toInt())
                    sendEmptyMessageDelayed(msgPlaying, 1000)
                }
            }
        }
    }

    enum class RecordStatus {
        waiting, recording, recorded, playing
    }

    var curruntStatus: RecordStatus = RecordStatus.waiting
        private set

    var statusCall: ((recordStatus: RecordStatus) -> Unit)? = null
    var timeCall: ((int: Int) -> Unit)? = null


    private var startPlayingTime = 0L

    /**
     * 播放重置
     */
    fun reset() {
        stopPlay()
        durInterval = 0L
        filePath = ""
        curruntStatus = RecordStatus.waiting
        statusCall?.invoke(curruntStatus)
    }

    /**
     * 暂停播放
     */
    fun stopPlay() {
        if (curruntStatus != RecordStatus.playing) {
            return
        }
        PlayEngine.pausePlay()
        curruntStatus = RecordStatus.recorded
        statusCall?.invoke(curruntStatus)
    }

    /**
     * 播放
     */
    fun tryPlay() {
        if (TextUtils.isEmpty(filePath)) {
            return
        }
        startPlayingTime = System.currentTimeMillis()
        PlayEngine.play(filePath, object : PlayEngine.AnimInterface {
            override fun stopAnim() {

            }

            override fun startAnim() {
            }

        }, object : PlayEngine.PlayListener {
            override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
            }

            override fun onPause(flag: Int, pause: Boolean) {
                curruntStatus = RecordStatus.recorded
                statusCall?.invoke(curruntStatus)
            }

            override fun onStart(fromCache: Boolean) {
                curruntStatus = RecordStatus.playing
                statusCall?.invoke(curruntStatus)
                //timeHander.sendEmptyMessageDelayed(msgPlaying,1000)
            }

        })
    }

    /**
     * 开始录音
     */
    fun startRecording() {
        if (isRecording) {
            mRecorder!!.release()
            mRecorder = null
        }
        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder!!.setOutputFile(filePath)
        startTime = System.currentTimeMillis()
        try {
            mRecorder!!.prepare()
            mRecorder!!.start()
            isRecording = true
            curruntStatus = RecordStatus.recording
            statusCall?.invoke(curruntStatus)
            timeHander.sendEmptyMessageDelayed(msgReCordWhat, 1000)

        } catch (e: Exception) {
            timeHander.removeMessages(msgReCordWhat)
            curruntStatus = RecordStatus.waiting
            statusCall?.invoke(curruntStatus)

        }
    }

    /**
     * 停止录音
     */
    fun stopRecording() {
        durInterval = System.currentTimeMillis() - startTime

        try {
            if (durInterval > 1000) {
                mRecorder!!.stop()
            }
            mRecorder!!.reset()
            mRecorder!!.release()
            mRecorder = null
            isRecording = false


            if (durInterval >= minTimeSpan) {
                curruntStatus = RecordStatus.recorded
                statusCall?.invoke(curruntStatus)
            } else {
                curruntStatus = RecordStatus.waiting
                statusCall?.invoke(curruntStatus)
            }

        } catch (e: Exception) {
            curruntStatus = RecordStatus.waiting
            statusCall?.invoke(curruntStatus)
        }
    }

    /**
     * 录用重置
     */
    fun resetRecorded() {
        try {
            isRecording = false
            mRecorder?.let {
                it.stop()
                it.reset()
                it.release()
                FileUtil.createFile(filePath)
            }
            curruntStatus = RecordStatus.waiting
            statusCall?.invoke(curruntStatus)
            durInterval = 0L
            mRecorder = null
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 获取录音文件
     */
    val date: ByteArray?
        get() = try {
            readFile(File(filePath))
        } catch (e: IOException) {
            null
        }

    /**
     * 获取录音时长,单位秒
     */
    fun getTimeInterval(): Long {
        return durInterval / 1000
    }

    companion object {
        private const val TAG = "RecorderHelper"

        /**
         * 将文件转化为byte[]
         *
         * @param file 输入文件
         */
        @Throws(IOException::class)
        private fun readFile(file: File): ByteArray { // Open file
            val f = RandomAccessFile(file, "r")
            return try { // Get and check length
                val longlength = f.length()
                val length = longlength.toInt()
                if (length.toLong() != longlength) {
                    throw IOException("File size >= 2 GB")
                }
                // Read file and return data
                val data = ByteArray(length)
                f.readFully(data)
                data
            } finally {
                f.close()
            }
        }
    }

    /**
     * 创建文件
     */
    init {

        filePath = IMApplication.appContext.getCacheDir().getPath() + "/audio/tempAudio_" + System.currentTimeMillis() + ".mp3"
        FileUtil.createFile(filePath)
    }
}