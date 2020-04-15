package com.zzz.oss_rubbishcommunity.manager.api

import com.zzz.oss_rubbishcommunity.model.api.machine.RegisterMachineRequestModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface MachineService {

    //注册新设备
    @POST("api/IoTDA/register")
    fun registerMachine(@Body registerMachineRequestModel: RegisterMachineRequestModel): Single<ResponseBody>


}