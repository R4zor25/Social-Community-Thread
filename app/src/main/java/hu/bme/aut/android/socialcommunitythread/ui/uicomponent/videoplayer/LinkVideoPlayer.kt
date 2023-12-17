package hu.bme.aut.android.socialcommunitythread.ui.uicomponent.videoplayer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer.*
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor
import com.google.android.exoplayer2.offline.DownloadHelper.createMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.ByteArrayDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@Composable
fun VideoPlayer(uri: Uri, modifier : Modifier) {
    val context = LocalContext.current
    val videoUri by rememberSaveable {
        mutableStateOf(uri)
    }

    val exoPlayer by remember {
        mutableStateOf(Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(videoUri))

                setMediaSource(source)
                prepare()
            })
    }


    exoPlayer.playWhenReady = false
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                hideController()
                useController = true
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            }
        }, modifier = modifier)
    ) {
        onDispose { exoPlayer.release() }
    }
}




@SuppressLint("RememberReturnType")
@Composable
fun LinkVideoPlayer(video: ByteArray, isDisabled: Boolean = true) {
    val context = LocalContext.current
    var currentDuration = 0
    val exoPlayer = remember(context) {
        mutableStateOf(Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )

                val tempVideo = File.createTempFile("tempVideo${UUID.nameUUIDFromBytes(video)}", "mp4")
                tempVideo.deleteOnExit()
                val fos = FileOutputStream(tempVideo)
                fos.write(video)
                fos.close()

                var mediaItem = MediaItem.fromUri(tempVideo.toURI().toString())
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem)
                setMediaSource(source)
                prepare()
            })
    }



    LaunchedEffect(key1 = "key"){
        if (exoPlayer != null) {
            exoPlayer.value.playWhenReady = false;
            exoPlayer.value.stop();

            exoPlayer.value.seekTo(0);
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
                        player = exoPlayer.value
                        this.controllerShowTimeoutMs = 0
                        val handler = Handler()
                        var runnable = Runnable {  }

                    }
                }
            )
        }
    }

fun createMediaSource(bytes: ByteArray): MediaSource {
    val byteArrayDataSource = ByteArrayDataSource(bytes)
    val factory = DataSource.Factory {
        return@Factory byteArrayDataSource
    }
    return ProgressiveMediaSource.Factory(factory,
        ExtractorsFactory { return@ExtractorsFactory arrayOf(Mp3Extractor()) })
        .createMediaSource(
            MediaItem.fromUri(
                Uri.EMPTY
            )
        )
}