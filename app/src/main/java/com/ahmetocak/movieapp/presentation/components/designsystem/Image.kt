package com.ahmetocak.movieapp.presentation.components.designsystem

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ahmetocak.movieapp.R

@Composable
fun AnimatedAsyncImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    imageUrl: String,
    errorImageDrawableId: Int = R.drawable.no_image_available
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
                painter = painterResource(id = errorImageDrawableId),
                contentDescription = null,
                contentScale = contentScale
            )
        }
    }
}

@Composable
fun AnimatedAsyncImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    imageUrl: String,
    borderStroke: BorderStroke = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
    borderShape: Shape = RoundedCornerShape(4.dp),
    errorImageDrawableId: Int = R.drawable.no_image_available
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
                    .alpha(1f.coerceAtMost(transition / .2f))
                    .border(borderStroke, borderShape),
                painter = painter,
                contentDescription = null,
                contentScale = contentScale,
                colorFilter = ColorFilter.colorMatrix(matrix)
            )
        }

        else -> {
            Image(
                modifier = modifier,
                painter = painterResource(id = errorImageDrawableId),
                contentDescription = null,
                contentScale = contentScale
            )
        }
    }
}