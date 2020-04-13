package com.zzz.oss_rubbishcommunity.manager.base

import com.zzz.oss_rubbishcommunity.model.api.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset

class NetErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val resStr = response.body?.source()?.buffer?.clone()?.readString(Charset.defaultCharset())

        //判断Http返回码
        if (response.code in 400..503) {
            throw ServerError(
                response.code,
                response.message
            )
        } else {
            //判断是否是 自己的服务端返回的数据 （数据格式是   {"meta":{"code":...,"msg":"..."},"data":{。。。}} ）
            if(resStr?.startsWith("{\"meta\":{\"code\"") == true){
                //判断meta中的业务状态码，例如密码错误，meta中的code就会是一个大于1000的数字，数字由后端定义
                val code = resStr.getCode()
                val message = resStr.getMessage()
                when{
                    code ?: 0 in 2000..3000 -> throw ApiException(
                            code ?: 0, message ?: ""
                    )
                    code ?: 0 in 400..500 -> throw ApiException(
                            code ?: 0, message ?: ""
                    )
                }
            }

        }
        return response
    }
}

//从json结果获取请求的 meta 状态中的 code
private fun String.getCode(): Int {
    val str = this.split(",")[0]
    val code = str.replace("{\"meta\":{\"code\":", "")
    return code.toInt()
}

//从json结果获取请求的 meta 状态中的 meassage
private fun String.getMessage(): String {
    val str = this.split(",")[1]
    val tempStr = str.replace("\"msg\":\"", "")
    val message = tempStr.replace("\"}}", "")
    return message
}


data class ServerError(val code: Int, val msg: String) : IOException()
