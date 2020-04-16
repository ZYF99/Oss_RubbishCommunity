package com.zzz.oss_rubbishcommunity.model.api.news

data class AddNewsRequestModel(
        val category: String? = "普通",
        val frontCoverImages: List<String>?,
        val newsType: Int?,
        val payload: String?,
        val payloadType: Int?,
        val title: String?
)