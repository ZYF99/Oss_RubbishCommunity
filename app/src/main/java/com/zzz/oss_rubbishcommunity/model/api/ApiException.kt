package com.zzz.oss_rubbishcommunity.model.api
import com.squareup.moshi.JsonClass
import java.io.IOException

@JsonClass(generateAdapter = false)
data class ApiException(
    val code:Int,
    val messageStr:String
) : IOException()