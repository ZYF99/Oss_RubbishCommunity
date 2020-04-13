package com.zzz.oss_rubbishcommunity.manager.api

import com.zzz.oss_rubbishcommunity.model.api.login.LoginOrRegisterRequestModel
import com.zzz.oss_rubbishcommunity.model.api.ResultModel
import com.zzz.oss_rubbishcommunity.model.api.login.LoginOrRegisterResultModel
import com.zzz.oss_rubbishcommunity.model.api.login.UsrProfileResp
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface UserService {

    //登录或注册
    @POST("api/account/login")
    fun loginOrRegister(
            @Body loginOrRegisterRequestModel: LoginOrRegisterRequestModel
    ): Single<ResultModel<LoginOrRegisterResultModel>>

    //修改用户信息
    @PUT("api/account/profile/modify")
    fun editUserInfo(@Body modifyMap: HashMap<String, String>): Single<ResultModel<String>>

    //刷新用户详细信息
    @GET("api/account/profile/refresh")
    fun fetchUserProfile(@Query("openId")openId:String? = null): Single<ResultModel<UsrProfileResp>>

    //获取用户卡片信息（头像 名字 签名等基本信息）
    @GET("api/account/info/{openId}/get")
    fun fetchUserCardByOpenId(@Path("openId") openId: String?): Single<ResultModel<UsrProfileResp>>

    //注销
    @POST("api/account/logout")
    fun logout(): Single<ResponseBody>


}