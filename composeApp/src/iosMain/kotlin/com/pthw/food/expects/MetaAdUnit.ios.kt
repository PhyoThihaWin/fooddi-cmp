package com.pthw.food.expects

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.UIKitView
import cocoapods.FBAudienceNetwork.*
import com.pthw.food.ui.composable.LoadingDialog
import fooddimultiplatform.composeapp.generated.resources.Res
import fooddimultiplatform.composeapp.generated.resources.meta_ad_banner
import fooddimultiplatform.composeapp.generated.resources.meta_ad_banner_test
import fooddimultiplatform.composeapp.generated.resources.meta_ad_interstitial
import fooddimultiplatform.composeapp.generated.resources.meta_ad_interstitial_test
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import org.jetbrains.compose.resources.stringResource
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

/**
 * Created by P.T.H.W on 25/07/2024.
 */

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
@Composable
actual fun MetaBanner(modifier: Modifier) {
    if (LocalInspectionMode.current) return Text("Advertisement Placeholder")
    val unitId = if (!Platform.isDebugBinary) {
        stringResource(Res.string.meta_ad_banner)
    } else {
        stringResource(Res.string.meta_ad_banner_test)
    }
    UIKitView(
        modifier = modifier.fillMaxWidth(),
        factory = {
//            val view = UIView(frame = CGRectMake(0.0, 0.0, 320.0, 50.0))
            val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
            FBAdView(unitId, kFBAdSizeHeight50Banner.readValue(), rootVC).apply {

                val listener = object : NSObject(), FBAdViewDelegateProtocol {
                    override fun adViewDidLoad(adView: FBAdView) {
                        Logger.i("MetaAd: onAdLoaded")

                    }

                    override fun adView(adView: FBAdView, didFailWithError: NSError) {
                        Logger.e("MetaAd: ${didFailWithError.localizedDescription}")
                    }
                }

                setFrame(CGRectMake(0.0, 0.0, 320.0, 50.0))
                setDelegate(listener)
                loadAd()
            }
        },
    )
}

@OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
@Composable
actual fun MetaInterstitial(
    onFinished: () -> Unit
) {
    if (LocalInspectionMode.current) return

    Logger.i("Reached: MetaInterstitial")

    val unitId = if (!Platform.isDebugBinary) {
        stringResource(Res.string.meta_ad_interstitial)
    } else {
        stringResource(Res.string.meta_ad_interstitial_test)
    }


    var adLoadSuccess by remember { mutableStateOf(false) }
    val interstitialAd: FBInterstitialAd = remember {
        FBInterstitialAd(unitId)
    }

    interstitialAd.apply {
        val listener = object : NSObject(), FBInterstitialAdDelegateProtocol {
            override fun interstitialAdDidLoad(ad: FBInterstitialAd) {
                Logger.i("MetaAd: interstitialAdDidLoad")
                adLoadSuccess = true
            }

            override fun interstitialAd(ad: FBInterstitialAd, didFailWithError: NSError) {
                adLoadSuccess = true
                Logger.e("MetaAd: ${didFailWithError.localizedDescription}")
            }

        }
        setDelegate(listener)
        loadAd()
    }

    val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController

    if (adLoadSuccess) {
        interstitialAd.showAdFromRootViewController(rootVC)
        onFinished()
    } else {
        LoadingDialog()
    }
}