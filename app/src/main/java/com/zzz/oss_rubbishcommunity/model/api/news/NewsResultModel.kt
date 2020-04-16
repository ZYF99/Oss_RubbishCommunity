package com.zzz.oss_rubbishcommunity.model.api.news

import com.zzz.oss_rubbishcommunity.manager.sharedpref.SharedPrefModel
import com.zzz.oss_rubbishcommunity.model.api.SimpleProfileResp

data class NewsResultModel(
        val newsDetailList: List<News>?
) {
    data class News(
            val newsId: Long?,
            val title: String?,
            val createDate: Long?,
            val category: String?,
            val authorProfile: SimpleProfileResp?,
            val newsType: Int?,
            val payloadType: Int?,
            val payload: String?,
            val frontCoverImages: List<String>?
    ) {
        fun isTEXT() = (payloadType == 1)
        fun isURL() = (payloadType == 2)
        fun isMD() = (payloadType == 3)
        fun isHTML() = (payloadType == 4)
        fun isBanner() = (newsType == 1)
        val foregroundImage = if (frontCoverImages?.isNotEmpty() == true) frontCoverImages[0]
        else ""

        companion object {
            fun createDefault() = NewsResultModel.News(
                    newsId = 0,
                    title = "",
                    createDate = 0,
                    category = "普通",
                    authorProfile = SharedPrefModel.getUserModel().simpleProfileResp,
                    newsType = 0,
                    payloadType = 1,
                    payload = "",
                    frontCoverImages = emptyList()
            )
        }
    }

}
