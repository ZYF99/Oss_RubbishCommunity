package com.zzz.oss_rubbishcommunity.ui.fragment.managemachine

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zzz.oss_rubbishcommunity.manager.api.MachineService
import com.zzz.oss_rubbishcommunity.manager.api.UserService
import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel
import com.zzz.oss_rubbishcommunity.model.api.SimpleProfileResp
import com.zzz.oss_rubbishcommunity.model.api.machine.Machine
import com.zzz.oss_rubbishcommunity.model.api.machine.RegisterMachineRequestModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseViewModel
import org.kodein.di.generic.instance
import java.io.File

class ManageMachineViewModel(application: Application) : BaseViewModel(application) {

    private val userService by instance<UserService>()
    private val machineService by instance<MachineService>()
    val isRefreshing = MutableLiveData(false)
    var oldMachineList = emptyList<Machine>()
    val machineList = MutableLiveData(emptyList<Machine>())
    val searchKey = MutableLiveData("")
    val avatarFile = MutableLiveData<File>()


    //获取设备信息列表
    fun fetchMachines() {
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

    //注册新设备
    fun registerMachine(
            machineMacAddress: String,
            machineName: String,
            machineType: Int,
            machineVersion: String
    ) {
        machineService.registerMachine(RegisterMachineRequestModel(
                machineMacAddress,
                SharedPrefModel.getUserModel().simpleProfileResp?.name ?: "",
                machineName,
                machineType,
                machineVersion
        )).doOnApiSuccess {

        }
    }

}