package com.zzz.oss_rubbishcommunity.model.image

data class GetQiNiuTokenRequestModel(
	val bucketName: String,
	val fileKeys: List<String>
)