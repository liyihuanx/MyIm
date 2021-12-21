package liyihuan.app.android.myim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tencent.imsdk.common.ICallback
import kotlinx.android.synthetic.main.activity_main.*
import liyihuan.app.android.lib_im.IMManager
import liyihuan.app.android.lib_im.ImCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}