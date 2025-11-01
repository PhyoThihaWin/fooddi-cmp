package com.pthw.food.expects

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import com.facebook.ads.*
import com.pthw.food.BuildConfig
import com.pthw.food.ui.composable.LoadingDialog
import fooddimultiplatform.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/**
 * Created by P.T.H.W on 25/07/2024.
 */

@Composable
actual fun MetaBanner(modifier: Modifier) {
    if (LocalView.current.isInEditMode) return
    val unitId = if (!BuildConfig.DEBUG) {
        stringResource(Res.string.meta_ad_banner)
    } else {
        stringResource(Res.string.meta_ad_banner_test)
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {


            AdView(it, unitId, AdSize.BANNER_HEIGHT_50).apply {
                loadAd(buildLoadAdConfig().withAdListener(object : AdListener {
                    override fun onError(p0: Ad?, p1: AdError?) {
                        Logger.e("MetaAd: ${p1?.errorMessage}")
                    }

                    override fun onAdLoaded(p0: Ad?) {
                        Logger.i("MetaAd: onAdLoaded")
                    }

                    override fun onAdClicked(p0: Ad?) {
                        Logger.i("MetaAd: onAdClicked")
                    }

                    override fun onLoggingImpression(p0: Ad?) {
                        Logger.i("MetaAd: onLoggingImpression")
                    }
                }).build())
            }
        },
    )
}

@Composable
actual fun MetaInterstitial(
    onFinished: () -> Unit
) {
    if (LocalView.current.isInEditMode) return
    val unitId = if (!BuildConfig.DEBUG) {
        stringResource(Res.string.meta_ad_interstitial)
    } else {
        stringResource(Res.string.meta_ad_interstitial_test)
    }

    Logger.i("Reached: MetaInterstitial")

    val context = LocalContext.current
    var adLoadSuccess by remember { mutableStateOf(false) }
    val interstitialAd: InterstitialAd = remember {
        InterstitialAd(context, unitId)
    }

    interstitialAd.apply {
        loadAd(
            buildLoadAdConfig().withAdListener(object : InterstitialAdListener {
                override fun onError(p0: Ad?, p1: AdError?) {
                    Logger.e("Ad was onError.")
                    adLoadSuccess = true
                }

                override fun onAdLoaded(p0: Ad?) {
                    Logger.i("Ad was loaded.")
                    adLoadSuccess = true
                }

                override fun onAdClicked(p0: Ad?) {
                    Logger.i("Ad was onAdClicked.")
                }

                override fun onLoggingImpression(p0: Ad?) {
                    Logger.i("Ad was onLoggingImpression.")
                }

                override fun onInterstitialDisplayed(p0: Ad?) {
                    Logger.i("Ad was onInterstitialDisplayed.")
                }

                override fun onInterstitialDismissed(p0: Ad?) {
                    Logger.i("Ad was onInterstitialDismissed.")
                }
            }).build()
        )
    }

    if (adLoadSuccess) {
        interstitialAd.show()
        onFinished()
    } else {
        LoadingDialog()
    }

}