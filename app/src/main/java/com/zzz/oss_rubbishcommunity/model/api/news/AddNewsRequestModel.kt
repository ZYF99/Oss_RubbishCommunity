package com.zzz.oss_rubbishcommunity.model.api.news

data class AddNewsRequestModel(
    val title: String,
    val type: Int,
    val content: String,
    val imageList: List<String>?,
    val top: Int
)