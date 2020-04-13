package com.zzz.oss_rubbishcommunity.ui.fragment.login

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zzz.oss_rubbishcommunity.MainApplication
import com.zzz.oss_rubbishcommunity.ui.base.BaseFragment
import com.zzz.oss_rubbishcommunity.R
import com.zzz.oss_rubbishcommunity.databinding.FragmentLoginBinding
import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel
import com.zzz.oss_rubbishcommunity.ui.activity.showMainActivity

//登录的Fragment，BaseFragment 的子类 ，定义其中的 布局 Binding 和 ViewModel 的类型
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
        LoginViewModel::class.java, layoutRes = R.layout.fragment_login
) {

    //初始化控件
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        //登录按钮 点击事件 点击调用 ViewModel 的 login 发起登录网络请求
        binding.btnLogin.setOnClickListener {
            when {
                (viewModel.userEmail.value ?: "").isEmpty() -> MainApplication.showToast("帐号不能为空")
                (viewModel.password.value
                        ?: "").length < 6 -> MainApplication.showToast("密码长度必须在6位及以上")
                else -> viewModel.login{jumpToMain()}
            }
        }

        //判读是否已经登录
        if (SharedPrefModel.hasLogin) viewModel.login{jumpToMain()}

        //是否成功登录的状态监听 成功便将信息存在本地 并跳转至主界面

    }

    //初始化ViewModel
    override fun initData() {
        viewModel.init()
    }

    private fun jumpToMain(){
        //跳转至主界面
        showMainActivity(activity as AppCompatActivity)
    }

}