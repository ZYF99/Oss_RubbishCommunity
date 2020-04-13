package com.zzz.oss_rubbishcommunity.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel

class LoginActivity : AppCompatActivity() {

    //纪录第一次点击back时的时间戳（以毫秒为单位的时间）
    private var quiteTime: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //利用 navigationController 设置打开登录界面，并设置在 当前从activity 布局的 nav_guide_fragment 控件中
        if (SharedPrefModel.hasLogin) {
            showMainActivity(this)
        } else {
            findNavController(R.id.nav_guide_fragment).navigate(
                R.id.LoginFragment
            )
        }
    }


    override fun onBackPressed() {
        if (System.currentTimeMillis() - quiteTime > 3000) {
            Toast.makeText(
                this, R.string.exit_info, Toast.LENGTH_SHORT
            ).show()
            quiteTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

}

fun showLoginActivity(activityTemp: Activity, isFromMain: Boolean = false) {
    activityTemp.startActivity(
        Intent(activityTemp, LoginActivity::class.java)
            .putExtra("isFromMain", isFromMain)
    )
    activityTemp.finish()
}
