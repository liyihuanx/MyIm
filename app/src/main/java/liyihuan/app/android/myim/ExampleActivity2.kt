package liyihuan.app.android.myim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_example1.*

class ExampleActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example1)

        AppStateDistribute.onUserStateChange(UserState.UserState2.state)
        tvState.text = "ExampleActivity2"

        openExample2.setOnClickListener {
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        AppStateDistribute.popToState(UserState.UserStateBegin.state)

    }
}