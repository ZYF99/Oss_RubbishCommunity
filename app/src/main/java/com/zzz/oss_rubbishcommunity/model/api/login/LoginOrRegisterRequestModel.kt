package com.zzz.oss_rubbishcommunity.model.api.login

import com.squareup.moshi.Json

data class LoginOrRegisterRequestModel(
		val deviceInfo: DeviceInfo,
		val password: String,
		val register: Boolean,
		val userName: String
) {
	
	data class DeviceInfo(
		val appVersion: String,
		val devName: String,
		@Json(name = "imei")
		val iMei: String,
		val platform:String,
		val systemModel: String,
		val systemVersion: String
	)
	
	
}