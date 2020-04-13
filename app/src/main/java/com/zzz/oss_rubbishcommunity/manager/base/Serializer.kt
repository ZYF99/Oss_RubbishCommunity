package com.zzz.oss_rubbishcommunity.manager.base

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

val globalMoshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
