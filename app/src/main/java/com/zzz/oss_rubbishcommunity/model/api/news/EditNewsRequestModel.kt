package com.zzz.oss_rubbishcommunity.model.api.news

data class EditNewsRequestModel(
        val category: String?,
        val frontCoverImages: List<String>?,
        val newsId: Long?,
        val newsType: Int?,
        val payload: String?,
        val title: String?
)