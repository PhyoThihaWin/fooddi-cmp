package com.pthw.food.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import fooddimultiplatform.composeapp.generated.resources.Res
import fooddimultiplatform.composeapp.generated.resources.logoblack
import org.jetbrains.compose.resources.painterResource

/**
 * Created by P.T.H.W on 04/04/2024.
 */

@Composable
fun CoilAsyncImage(
    image: Any,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    AsyncImage(
        modifier = modifier,
        model = image,
        placeholder = painterResource(Res.drawable.logoblack),
        error = painterResource(Res.drawable.logoblack),
        contentScale = contentScale,
        contentDescription = null,
    )
}