package com.lyf.compose.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lyf.compose.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
// Compose 使用 Kotlin 编译器插件，首次运行需要：
// 编译 Compose 运行时代码
// 生成和缓存 Compose 类
// 预热 Compose 运行环境 ,延迟1-2s，回到主页，后debug模式就不会太卡了
@Composable
fun WelcomeScreen(
    navigateToNext : () -> Unit
) {
    // 动画状态控制
    val logoScale = remember { Animatable(0.8f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val backgroundAlpha = remember { Animatable(0f) }
    val particleAlpha = remember { Animatable(0f) }

    // 粒子效果数据
    val particles = remember {
        List(20) {
            Particle(
                id = it,
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 10 + 5,
                speed = Random.nextFloat() * 2 + 1,
                alpha = Random.nextFloat() * 0.5f + 0.3f
            )
        }
    }

    // 彩色光晕动画
    val hueRotation = remember { Animatable(0f) }

    // 启动动画序列
    LaunchedEffect(Unit) {
        // 第一步：背景渐入（0.3秒）
        launch {
            backgroundAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(300)
            )
        }

        // 第二步：Logo缩放和淡入（0.5秒，延迟0.1秒开始）
        delay(100)
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(500)
            )
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        }

        // 第三步：彩色光晕旋转动画（循环）
        launch {
            hueRotation.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }

        // 第四步：文字淡入（0.4秒，延迟0.6秒开始）
        delay(600)
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(400)
            )
        }

        // 第五步：粒子效果（0.3秒，延迟0.8秒开始）
        delay(800)
        launch {
            particleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(300)
            )
        }

        // 等待2秒后跳转
        delay(1500)
        navigateToNext ()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 渐变背景
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(backgroundAlpha.value)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF6A11CB),
                            Color(0xFF2575FC)
                        ),
                        center = Offset.Unspecified,
                        radius = 800f
                    )
                )
        )

        // 动态粒子背景
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .alpha(particleAlpha.value)
        ) {
            particles.forEach { particle ->
                drawCircle(
                    color = Color.White.copy(alpha = particle.alpha),
                    center = Offset(
                        x = particle.x * size.width,
                        y = particle.y * size.height
                    ),
                    radius = particle.size,
                    alpha = particle.alpha
                )
            }
        }

        // 彩色光晕背景
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(30.dp)
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFF416C),
                        Color(0xFFFF4B2B),
                        Color(0xFF11998E),
                        Color(0xFF38EF7D)
                    ),
                    center = center,
                    radius = size.minDimension / 2
                ),
                center = center,
                radius = size.minDimension / 2,
                alpha = 0.3f
            )
        }

        // 主要内容
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            // Logo或图标
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .graphicsLayer {
                        rotationZ = hueRotation.value
                    }
            ) {
                // 如果有应用图标，使用Image
                Image(
                    painter = painterResource(id = R.drawable.icon_logo), // 替换为你的图标
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize()
                )

                // 如果没有图标，使用彩色圆形替代
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFF416C),
                                Color(0xFFFF4B2B)
                            )
                        ),
                        center = center,
                        radius = size.minDimension / 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 应用名称
            Text(
                text = "Music AI",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .graphicsLayer {
                        translationY = textAlpha.value * 20
                    }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 副标题
            Text(
                text = "compose your music with AI",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value * 0.8f)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // 加载进度指示器
            LoadingIndicator(
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .height(4.dp)
            )
        }

        // 底部版权信息
        Text(
            text = "Custom Music AI App © 2024",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
                .alpha(textAlpha.value * 0.6f)
        )
    }
}

// 加载进度指示器
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = keyframes {
                durationMillis = 2000
                0f at 0
                0.3f at 600
                0.7f at 1200
                1f at 2000
            }
        )
    }

    Box(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .height(4.dp)
                .scale(progress.value, 1f)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF11998E),
                            Color(0xFF38EF7D)
                        )
                    )
                )
        )
    }
}

// 粒子数据类
data class Particle(
    val id: Int,
    var x: Float,
    var y: Float,
    val size: Float,
    val speed: Float,
    val alpha: Float
)