package com.ahmetocak.movieapp.presentation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ahmetocak.movieapp.R

@Composable
fun AnimatedAsyncImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    imageUrl: String
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .allowHardware(false)
            .crossfade(true)
            .build()
    )

    val transition by animateFloatAsState(
        targetValue = if (painter.state is AsyncImagePainter.State.Success) 1f else 0f,
        label = "image transition animation"
    )

    val matrix = ColorMatrix()
    matrix.setToSaturation(transition)

    when (painter.state) {
        is AsyncImagePainter.State.Loading -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                modifier = modifier
                    .scale(.8f + (.2f * transition))
                    .graphicsLayer { rotationX = (1f - transition) * 5f }
                    .alpha(1f.coerceAtMost(transition / .2f)),
                painter = painter,
                contentDescription = null,
                contentScale = contentScale,
                colorFilter = ColorFilter.colorMatrix(matrix)
            )
        }

        else -> {
            Image(
                modifier = modifier,
                painter = painterResource(id = R.drawable.no_image_available),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}