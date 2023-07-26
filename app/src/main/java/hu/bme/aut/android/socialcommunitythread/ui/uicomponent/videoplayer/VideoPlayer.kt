package hu.bme.aut.android.socialcommunitythread.ui.uicomponent

import android.annotation.SuppressLint
import android.os.Handler
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/*
@Composable
fun VideoPlayer(videoUrl: String) {
    val context = LocalContext.current
    val player = SimpleExoPlayer.Builder(context).build()
    val playerView = PlayerView(context)
    val mediaItem = MediaItem.fromUri(videoUrl)
    val playWhenReady by rememberSaveable {
        mutableStateOf(true)
    }
    player.setMediaItem(mediaItem)
    playerView.player = player
    LaunchedEffect(player) {
        player.prepare()
        player.playWhenReady = playWhenReady

    }
    AndroidView(factory = {
        playerView
    })
}

 */


@SuppressLint("RememberReturnType")
@Composable
fun VideoPlayer(videoUrl: String, isDisabled: Boolean = true) {
    val context = LocalContext.current
    var currentDuration = 0
    val exoPlayer = remember(context) {
        SimpleExoPlayer.Builder(context).build().apply {
            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                context, Util.getUserAgent(context, context.packageName)
            )

            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(videoUrl))

            this.prepare(source)
        }
    }

    LaunchedEffect(key1 = "key"){
        if (exoPlayer != null) {
            exoPlayer.playWhenReady = false;
            exoPlayer.stop();

            exoPlayer.seekTo(0);
        }
    }

        Column(
            modifier = Modifier
                .width(480.dp)
                .height(270.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AndroidView(
                factory = { context ->
                    //PlayerControlView(context).apply {
                      //  player = exoPlayer
                     //   this.showTimeoutMs = 0
                   // }
                    PlayerView(context).apply {
                        player = exoPlayer
                        this.controllerShowTimeoutMs = 0
                        val handler = Handler()
                        var runnable = Runnable {  }

                    }
                }
            )
        }
    }