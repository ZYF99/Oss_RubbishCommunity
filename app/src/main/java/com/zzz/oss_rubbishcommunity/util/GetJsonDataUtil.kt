package com.zzz.oss_rubbishcommunity.util

import android.content.Context
import android.content.res.AssetManager
import com.zzz.oss_rubbishcommunity.manager.base.globalMoshi
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.zzz.oss_rubbishcommunity.MainApplication
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@JsonClass(generateAdapter = false)
data class CityCodeModel(
    val provinceList:List<Province>?
)

@JsonClass(generateAdapter = true)
data class Province(
    val name:String,
    val code:String,
    val city:List<City>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class City(
    val name:String,
    val code:String,
    val area:List<Area>
)

@JsonClass(generateAdapter = true)
data class Area(
    val name:String,
    val code:String
)

fun <T> jsonToList(json: String, classOfT: Class<T>, moshi: Moshi = globalMoshi): List<T>? {
    val type = Types.newParameterizedType(List::class.java, classOfT)
    val adapter = moshi.adapter<List<T>>(type)
    return adapter.fromJson(json)
}


class GetJsonDataUtil {
    fun getJson(context: Context, fileName: String?): String {
        val stringBuilder = StringBuilder()
        try {
            val assetManager: AssetManager = context.assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName!!)
                )
            )
            var line: String? = null
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}

fun getProvinceModelList():List<Province>?{
    //获取assets目录下的json文件数
    val jsonData = GetJsonDataUtil().getJson(MainApplication.instance, "city_code.json")
    return jsonData.jsonToList()
}