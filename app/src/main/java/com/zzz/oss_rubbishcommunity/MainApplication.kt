package com.zzz.oss_rubbishcommunity

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.multidex.MultiDex
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.Gson
import com.jakewharton.threetenabp.AndroidThreeTen
import com.zzz.oss_rubbishcommunity.manager.di.apiModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import timber.log.Timber

class MainApplication : Application(), KodeinAware {

    companion object {
        var nowUserId: String = ""

        lateinit var instance: MainApplication

        fun showToast(str: String) {
            Toast.makeText(instance, str, Toast.LENGTH_SHORT).show()
        }
    }

    override val kodein by Kodein.lazy {
        import(androidXModule(this@MainApplication))
        import(apiModule)
        /* bindings */
    }

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
        Kotpref.gson = Gson()
        AndroidThreeTen.init(this)
        instance = this
        debug()
    }

    private fun debug() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}