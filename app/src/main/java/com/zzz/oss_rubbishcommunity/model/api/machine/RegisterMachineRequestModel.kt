package com.zzz.oss_rubbishcommunity.model.api.machine

data class RegisterMachineRequestModel(
        val machineMacAddress: String,
        val machineMaker: String,
        val machineName: String,
        val machineType: Int,
        val machineVersion: String
)