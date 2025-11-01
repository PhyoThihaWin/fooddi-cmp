package com.pthw.food

import android.content.Context
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.pthw.food.expects.Logger

/**
 * Sample class that shows how to call initialize() method of Audience Network SDK.
 */
class AudienceNetworkInitializeHelper : AudienceNetworkAds.InitListener {
    override fun onInitialized(result: AudienceNetworkAds.InitResult) {
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
                }

                AudienceNetworkAds
                    .buildInitSettings(context)
                    .withInitListener(AudienceNetworkInitializeHelper())
                    .initialize()
            }
        }
    }
}