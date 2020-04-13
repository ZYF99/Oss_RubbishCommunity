package com.zzz.oss_rubbishcommunity.ui.fragment.login

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.zzz.oss_rubbishcommunity.MainApplication
import com.zzz.oss_rubbishcommunity.ui.base.BaseViewModel
import com.zzz.oss_rubbishcommunity.manager.api.UserService
import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel
import com.zzz.oss_rubbishcommunity.model.api.login.LoginOrRegisterRequestModel
import com.zzz.oss_rubbishcommunity.model.api.login.LoginOrRegisterResultModel
import com.zzz.oss_rubbishcommunity.util.*
import org.kodein.di.generic.instance

class LoginViewModel(application: Application) : BaseViewModel(application) {
    private val userService by instance<UserService>()
    val userEmail = MutableLiveData("")
    val password = MutableLiveData("")
    val isLoginSuccess = MutableLiveData(false)
    val rememberPassword = MutableLiveData(false)

    fun init() {
        rememberPassword.value = SharedPrefModel.rememberPassword
        userEmail.value = SharedPrefModel.userEmail
        if (SharedPrefModel.rememberPassword)
            password.value = SharedPrefModel.password
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun login(action:(()->Unit)? = null) {
        val versionName = getVersionName(MainApplication.instance) //APP版本号
        val deviceBrand = deviceBrand //手机品牌
        val osVersion = systemVersion //系统版本号
        val systemModel = systemModel //系统
        userService.loginOrRegister(
                LoginOrRegisterRequestModel(
                        LoginOrRegisterRequestModel.DeviceInfo(
                                versionName ?: "",
                                deviceBrand,
                                getPhoneIMEI(MainApplication.instance) ?: "",
                                "Android",
                                systemModel,
                                osVersion
                        ),
                        password.value ?: "",
                        false,
                        userEmail.value ?: ""
                )
        )
                .autoProgressDialog()
                .doOnApiSuccess {
                    saveUserData(it.data)
                    action?.invoke()
                }
    }

    //存user的数据到本地
    private fun saveUserData(loginResultModel: LoginOrRegisterResultModel?) {
        SharedPrefModel.hasLogin = true
        SharedPrefModel.userEmail = userEmail.value!!
        SharedPrefModel.password = password.value!!
        SharedPrefModel.token = loginResultModel?.token ?: ""
        SharedPrefModel.uid = loginResultModel?.openId ?: ""
        SharedPrefModel.rememberPassword = rememberPassword.value!!
    }

}
