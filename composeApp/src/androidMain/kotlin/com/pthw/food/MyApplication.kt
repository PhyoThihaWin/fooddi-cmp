package com.pthw.food

import android.app.Application
import com.pthw.food.AudienceNetworkInitializeHelper
import com.pthw.food.di.initKoin
import com.pthw.food.expects.KmpNotifierInitializer
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

        // kmpnotifier
        KmpNotifierInitializer.initialize()
    }
}