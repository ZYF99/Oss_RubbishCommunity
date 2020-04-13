package com.zzz.oss_rubbishcommunity.manager.sharedpref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonPref
import com.zzz.oss_rubbishcommunity.util.Constants

object SharedPrefModel : KotprefModel() {
    override val kotprefName: String = Constants.SHARED_PREF_FILE_NAME
    var hasLogin: Boolean by booleanPref(false)
    var userEmail: String by stringPref()
    var uid: String by stringPref()
    var openId: Int by intPref()
    var password: String by stringPref()
    var rememberPassword:Boolean by booleanPref()
    var token: String by stringPref()
    var userSettingMap: MutableMap<String, UserModel> by gsonPref(hashMapOf())

    private fun getUserModel(userIdS: String): UserModel =
        userSettingMap[userIdS] ?: UserModel().apply {
            userSettingMap[userIdS] = this
        }

    fun getUserModel(): UserModel = getUserModel(userEmail)

    fun setUserModel(modify: UserModel.() -> Unit) {
        val map = userSettingMap
        val user = map[userEmail] ?: UserModel()
        user.apply { modify.invoke(this) }
        map[userEmail] = user
        userSettingMap = map
    }

    fun setDefault() {
        setUserModel {
            //userSetting = null
        }
    }
}
