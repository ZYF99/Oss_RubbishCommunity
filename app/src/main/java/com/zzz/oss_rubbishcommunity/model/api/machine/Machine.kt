package com.zzz.oss_rubbishcommunity.model.api.machine

data class Machine(
        val machineMacAddress: String,
        val machineMaker: String,
        val machineName: String,
        val machineType: Int,
        val machineVersion: String
)