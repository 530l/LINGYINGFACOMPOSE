package com.lyf.lingyingfacompose.ui.wx

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import kotlinx.coroutines.awaitCancellation

/**
 * @author: njb
 * @date:   2025/8/18 18:27
 * @desc:   描述
 */
class PlayVideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoUrl = intent.getStringExtra("url") ?: ""
        setContent {
            VideoPlayerScreen(videoUrl = videoUrl)
        }
    }

    @Composable
    fun VideoPlayerScreen(videoUrl: String) {
        val context = LocalContext.current
        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(videoUrl)
                // val mediaItem = MediaItem.Builder().setUri(videoUrl).build()
                setMediaItem(mediaItem)
                prepare() // 准备播放
                playWhenReady = true // 自动播放
            }
        }

        AndroidView(
            factory = { ctx ->
                StyledPlayerView(ctx).apply {
                    player = exoPlayer
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        LaunchedEffect(Unit) {
            try {
                awaitCancellation()
            } finally {
                // 协程取消时释放播放器
                exoPlayer.release()
            }
        }
    }

}