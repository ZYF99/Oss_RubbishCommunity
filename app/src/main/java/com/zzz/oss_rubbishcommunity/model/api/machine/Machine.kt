package com.zzz.oss_rubbishcommunity.model.api.machine

import com.zzz.oss_rubbishcommunity.model.api.SimpleProfileResp

data class Machine(
        val machineMacAddress: String,
        val bindKey: String,
        val machineMaker: String,
        val machineName: String,
        val nikeName: String,
        val machineType: Int,
        val machineVersion: String,
        val simpleProfileResp: SimpleProfileResp,
        val bindTime: Long
) {
    val machineTypeStr = machineType.toString()
}