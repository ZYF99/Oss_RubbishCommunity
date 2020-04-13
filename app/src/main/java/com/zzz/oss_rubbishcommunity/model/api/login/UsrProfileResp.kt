package com.zzz.oss_rubbishcommunity.model.api.login

import com.google.gson.annotations.SerializedName

data class UsrProfileResp(
        @SerializedName("usrProfileResp")
	val usrProfile: UsrProfile,
        val profileStatusResp: ProfileStatusResp
) {
	data class ProfileStatusResp(
		val completelyRegistrationFlag: String,
		val flagEmailVerify: String,
		val uinStatus: String,
		val uinType: String
	)
}

