package com.pthw.food.adview

import android.content.Context
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.AudienceNetworkAds.InitListener
import com.facebook.ads.AudienceNetworkAds.InitResult
import com.pthw.food.BuildConfig
import com.pthw.food.Logger
import com.pthw.food.R

/**
 * Sample class that shows how to call initialize() method of Audience Network SDK.
 */
class AudienceNetworkInitializeHelper : InitListener {
    override fun onInitialized(result: InitResult) {
        Logger.i("${AudienceNetworkAds.TAG}${result.message}")
    }

    companion object {
        /**
         * It's recommended to call this method from Application.onCreate().
         * Otherwise you can call it from all Activity.onCreate()
         * methods for Activities that contain ads.
         *
         * @param context Application or Activity.
         */
        fun initialize(context: Context) {
            if (!AudienceNetworkAds.isInitialized(context)) {
                if (BuildConfig.DEBUG) {
                    AdSettings.turnOnSDKDebugger(context)
                    AdSettings.addTestDevice(context.getString(R.string.test_device_hash))
                }

                AudienceNetworkAds
                    .buildInitSettings(context)
                    .withInitListener(AudienceNetworkInitializeHelper())
                    .initialize()
            }
        }
    }
}