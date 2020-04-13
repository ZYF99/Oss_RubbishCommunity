package com.zzz.oss_rubbishcommunity.ui.fragment.manageuser

import com.zzz.oss_rubbishcommunity.model.api.SimpleProfileResp


data class FetchAllUserProfileResultModel(
    val userProfileRspList:List<SimpleProfileResp>
)