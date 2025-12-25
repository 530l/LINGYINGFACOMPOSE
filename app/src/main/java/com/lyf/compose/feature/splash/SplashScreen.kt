package com.lyf.compose.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.lyf.compose.R
import kotlinx.coroutines.delay

// 显示一张全屏的过渡图，然后延迟后导航到下一个页面（已移除动画和额外加载指示器）
@Composable
fun WelcomeScreen(
    navigateToNext: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1500)
        navigateToNext()
    }

    Image(
        painter = painterResource(id = R.drawable.bg_splash_theme),
        contentDescription = "Splash Background",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}
