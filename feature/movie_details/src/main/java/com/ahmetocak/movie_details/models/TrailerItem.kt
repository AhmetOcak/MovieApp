package com.ahmetocak.movie_details.models

import android.view.View
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ahmetocak.common.helpers.conditional
import com.ahmetocak.designsystem.WindowSizeClasses
import com.ahmetocak.designsystem.computeWindowHeightSize
import com.ahmetocak.movie_details.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
internal fun TrailerItem(
    videoId: String,
    isScreenHeightCompact: Boolean = computeWindowHeightSize() == WindowSizeClasses.COMPACT
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var player: YouTubePlayer? by remember { mutableStateOf(null) }
    val view: View = View.inflate(LocalContext.current, R.layout.youtube_trailer_item, null)
    val overlayView: View = view.findViewById(R.id.overlay_view)

    overlayView.setOnClickListener {
        player?.play()
    }

    val youTubePlayerView: YouTubePlayerView by remember {
        mutableStateOf(view.findViewById<YouTubePlayerView?>(R.id.youtube_player_view).apply {
            enableAutomaticInitialization = false
            initialize(youTubePlayerListener = object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    player = youTubePlayer
                    youTubePlayer.cueVideo(videoId, 0f)
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState
                ) {
                    super.onStateChange(youTubePlayer, state)
                    when (state) {
                        PlayerConstants.PlayerState.VIDEO_CUED -> {
                            overlayView.visibility = View.VISIBLE
                        }

                        PlayerConstants.PlayerState.PAUSED -> {
                            overlayView.visibility = View.VISIBLE
                        }

                        else -> {
                            overlayView.visibility = View.GONE
                        }
                    }
                }
            })
            lifecycleOwner.lifecycle.addObserver(this)
        })
    }

    DisposableEffect(Unit) {
        onDispose {
            player?.pause()
            youTubePlayerView.apply {
                release()
                lifecycleOwner.lifecycle.removeObserver(this)
            }
        }
    }

    ElevatedCard(
        modifier = Modifier.conditional(condition = isScreenHeightCompact,
            ifTrue = { height(LocalConfiguration.current.screenHeightDp.dp - 32.dp) },
            ifFalse = { width(LocalConfiguration.current.screenWidthDp.dp - 32.dp) })
    ) {
        AndroidView(
            factory = { view },
            modifier = Modifier
                .conditional(condition = isScreenHeightCompact,
                    ifTrue = { height(LocalConfiguration.current.screenHeightDp.dp - 32.dp) },
                    ifFalse = { width(LocalConfiguration.current.screenWidthDp.dp - 32.dp) })
                .aspectRatio(16f / 9f)
                .fillMaxWidth()
        )
    }
}