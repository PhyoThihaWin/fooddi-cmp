package com.pthw.food.adview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Created by phyothihawin on 29/10/2025.
 */


@Composable
expect fun MetaBanner(modifier: Modifier)

@Composable
expect fun MetaInterstitial(
    onFinished: () -> Unit
)