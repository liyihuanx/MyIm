package liyihuan.app.android.myim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_example1.*

class ExampleActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example1)

        AppStateDistribute.onUserStateChange(UserState.UserState1.state)
        tvState.text = "ExampleActivity1"
        openExample2.setOnClickListener {
            val intent = Intent(this, ExampleActivity2::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}