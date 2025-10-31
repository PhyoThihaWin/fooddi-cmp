package com.pthw.food

import android.app.Application
import com.pthw.food.adview.AudienceNetworkInitializeHelper
import com.pthw.food.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MyApplication)
        }

        // meta audience network
        AudienceNetworkInitializeHelper.initialize(this)
    }
}