package com.zzz.oss_rubbishcommunity.ui.fragment.manageuser

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zzz.oss_rubbishcommunity.manager.api.UserService
import com.zzz.oss_rubbishcommunity.model.api.SimpleProfileResp
import com.zzz.oss_rubbishcommunity.ui.base.BaseViewModel
import org.kodein.di.generic.instance
import java.io.File

class ManageUserViewModel(application: Application) : BaseViewModel(application) {

    private val userService by instance<UserService>()
    val isRefreshing = MutableLiveData(false)
    var oldUserList = emptyList<SimpleProfileResp>()
    val userList = MutableLiveData(emptyList<SimpleProfileResp>())
    val searchKey = MutableLiveData("")
    val avatarFile = MutableLiveData<File>()

    //获取用户信息列表
    fun fetchUsers() {
/*        userService.fetchUserList()
            .doOnApiSuccess {
                val profileList = it.data?.userProfileRspList
                val ossList =
                    profileList?.filter { profile -> profile.role == ROLE_OSS }
                        ?.sortedBy { profile -> profile.createTime }
                        ?: emptyList() //管理员列表
                val commonUserList =
                    profileList?.filter { profile -> profile.role == ROLE_USER }
                        ?.sortedBy { profile -> profile.createTime }
                        ?: emptyList() //普通用户列表
                val resultList = listOf<Profile>().toMutableList()
                    .apply {
                        toMutableList()
                        addAll(ossList)
                        addAll(commonUserList)
                    }.toList()

                userList.postValue(resultList)
                oldUserList = resultList
            }*/
    }


}