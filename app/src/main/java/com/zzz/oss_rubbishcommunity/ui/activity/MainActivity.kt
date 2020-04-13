package com.zzz.oss_rubbishcommunity.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zzz.oss_rubbishcommunity.ui.base.setupWithNavController
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //纪录第一次点击back时的时间戳（以毫秒为单位的时间）
    private var quiteTime: Long = System.currentTimeMillis()
    private var currentNavController: LiveData<NavController>? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //通过dataBinding绑定activity_main布局，并得到Binding文件
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //当界面是首次被打开时，设置BottomNavigation
        //因为若不是首次打开 Activity的 savedInstanceState 参数会有值，所以判断其是否为空即可判断是否是首次启动
        if (savedInstanceState == null) {
            //setUpOnBackPressedDispatcher()
            setupBottomNavigationBar()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState!!)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomnavigation)
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.top_news,
                R.navigation.top_mine
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment_container,
            intent = intent
        )
        //将MainActivity中的当前NavController赋值为以上
        currentNavController = controller
        //默认选中咨询页面
        bottomNavigationView.selectedItemId = R.id.navigation_news
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
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

fun showMainActivity(activityTemp: AppCompatActivity) {
    activityTemp.startActivity(Intent(activityTemp, MainActivity::class.java))
    activityTemp.finish()
}


