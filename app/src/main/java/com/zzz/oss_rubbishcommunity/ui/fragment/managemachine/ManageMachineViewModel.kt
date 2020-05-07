package com.zzz.oss_rubbishcommunity.ui.fragment.managemachine

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.zzz.oss_rubbishcommunity.manager.api.MachineService
import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel
import com.zzz.oss_rubbishcommunity.model.api.machine.FetchAllMachinesRequestModel
import com.zzz.oss_rubbishcommunity.model.api.machine.Machine
import com.zzz.oss_rubbishcommunity.model.api.machine.RegisterMachineRequestModel
import com.zzz.oss_rubbishcommunity.ui.base.BaseViewModel
import io.reactivex.Single
import org.kodein.di.generic.instance
import java.util.*

class ManageMachineViewModel(application: Application) : BaseViewModel(application) {

    //private val userService by instance<UserService>()
    private val machineService by instance<MachineService>()
    val isRefreshing = MutableLiveData(false)
    val machineList = MutableLiveData(emptyList<Machine>())
    var oldMachineList = emptyList<Machine>()
    val searchKey = MutableLiveData("")

    //获取设备信息列表
    fun fetchMachines() {
        machineService.fetchAllMachines(
                FetchAllMachinesRequestModel(
                        endTime = Date().time,
                        startTime = 1
                )
        ).dealRefresh()
                .doOnApiSuccess {
                    val machineListTemp = it.data?.machineDetailInfoList
                    val res =
                            machineListTemp
                                    ?.sortedBy { machine -> machine.bindTime }
                                    ?: emptyList() //机器列表


                    machineList.postValue(res)
                    oldMachineList = res
                }
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

    private fun <T> Single<T>.dealRefresh() = doOnSubscribe { isRefreshing.postValue(true)}
            .doFinally { isRefreshing.postValue(false) }

}