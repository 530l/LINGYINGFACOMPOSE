package com.lyf.compose.feature.animated


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import kotlinx.coroutines.launch

/**
 * Animatable 和 animate*AsState（如 animateFloatAsState、animateIntAsState 等）是两种常用的动画 API，
 * 它们都用于实现状态驱动的动画，但在使用方式、控制粒度和适用场景上有显著区别。以下是它们的核心差异：
 *
 * 1. 使用方式与简洁性
 *  1.1-animate*AsState：
 *      是声明式、自动管理生命周期的动画。
 *      只需传入目标值（target value），Compose 会自动从当前值平滑过渡到目标值。
 *      非常适合简单状态切换（如淡入淡出、颜色变化、尺寸缩放等）。
 * 1.2Animatable：
 *      是一个可变的状态容器，需要手动启动动画（通过 animateTo()）。
 *      提供更细粒度的控制，比如可以暂停、取消、查询当前值或速度。
 *      更适合复杂交互逻辑（如拖拽反馈、连续动画、中断/重启动画等）。
 */

@Composable
fun AnimatableScreen() {
    AppScaffold {
        val animatableSize = remember {
            Animatable(initialValue = 200.dp, typeConverter = Dp.VectorConverter)
        }
        val animatableRoundCornerSize = remember {
            Animatable(initialValue = 0.dp, typeConverter = Dp.VectorConverter)
        }

        val coroutine = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(animatableSize.value)
                    .clip(RoundedCornerShape(animatableRoundCornerSize.value))
                    .background(MaterialTheme.colorScheme.primary)
            )
            Button(onClick = {
                coroutine.launch {
                    //animatableSize.animateTo(targetValue = 200.dp)
                    // 确保圆角动画在 1s（1000ms）内完成
                    animatableRoundCornerSize.animateTo(
                        targetValue = 20.dp,
                        animationSpec = tween(durationMillis = 1000)
                    )
                }
            }) {
                Text(text = "圆")
            }
            Button(onClick = {
                coroutine.launch {
                    //animatableSize.animateTo(targetValue = 200.dp)
                    // 确保圆角动画在 1s（1000ms）内完成
                    animatableRoundCornerSize.animateTo(
                        targetValue = 0.dp,
                        animationSpec = tween(durationMillis = 1000)
                    )
                }
            }) {
                Text(text = "方")
            }
        }
    }
}