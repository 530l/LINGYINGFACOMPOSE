package com.lyf.lingyingfacompose.components


import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lyf.lingyingfacompose.R

import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 加载中动画图标
 *
 * @param size 图标大小，默认16.dp
 * @param color 图标颜色，默认使用未指定颜色
 * @param isRotating 是否启用旋转动画，默认为true
 */
@Composable
fun WeLoading(size: Dp = 16.dp, color: Color = Color.Unspecified, isRotating: Boolean = true) {
    val angle by if (isRotating) {
        val transition = rememberInfiniteTransition(label = "")
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1000, easing = LinearEasing),
                RepeatMode.Restart
            ),
            label = "WeLoadingAnimation"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    Icon(
        painter = painterResource(id = R.drawable.icon_data_loading),
        contentDescription ="加载中",
        modifier = Modifier
            .size(size)
            .rotate(angle),
        tint = color
    )
}





