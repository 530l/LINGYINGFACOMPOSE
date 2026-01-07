package com.lyf.compose.feature.animated

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.lyf.compose.core.ui.components.scaffold.AppScaffold
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 *  animate*AsState  属性动画
 */
//todo animateAsState = ValueAnimator 类似属性动画
@Composable
fun AnimateAsStateScreen(onBack: () -> Unit) {
    AppScaffold(onBackClick = onBack) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 主标题
            Text(
                text = "animate*AsState 示例",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // 进度条演示：放在 Card 中以增强可视层级
            SectionCard(title = "下载进度（示例）") {
                AnimationProgressBar()
            }

            // 各类 animateAsState 示例分区
            SectionCard(title = "数值/颜色/尺寸动画示例") {
                AnimateAsStateSamples()
            }

            // 说明区（注释已在代码中）
            Text(
                text = "注：animateRectAsState/animateValueAsState 需要 TwoWayConverter，示例已在代码注释中说明",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            content()
        }
    }
}

@Composable
fun AnimationProgressBar(modifier: Modifier = Modifier) {
    // 进度与状态
    var progress by remember { mutableFloatStateOf(0f) }
    var isDownloading by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )
    val coroutine = rememberCoroutineScope()

    // 用 snapshotFlow 检测 animatedProgress 变化并触发脉冲动画（跳过首次）
    var percentPulse by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        snapshotFlow { animatedProgress }.collectIndexed { index, _ ->
            if (index == 0) return@collectIndexed
            percentPulse = true
            delay(180)
            percentPulse = false
        }
    }

    // 脉冲缩放由百分比脉冲状态驱动
    val percentScale by animateFloatAsState(
        targetValue = if (percentPulse) 1.08f else 1f,
        animationSpec = tween(durationMillis = 180)
    )

    // 平滑的整数显示，让数字变化更可感知
    val displayPercent by animateIntAsState(
        targetValue = (animatedProgress * 100).roundToInt(),
        animationSpec = tween(durationMillis = 300)
    )

    // 记录轨道宽度（像素），用于处理点击位置设置进度
    var trackWidth by remember { mutableStateOf(0) }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // 轨道（统一高度、圆角），支持点击设置进度
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .onSizeChanged { trackWidth = it.width }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        if (trackWidth > 0) {
                            val fraction = (tapOffset.x / trackWidth).coerceIn(0f, 1f)
                            progress = fraction
                        }
                    }
                }
        ) {
            // 根据 animatedProgress 渲染内层进度条：高度适配轨道
            if (animatedProgress > 0.001f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .height(14.dp)
                        .align(Alignment.CenterStart)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
        }

        // 状态行：左侧状态，右侧百分比（添加脉冲缩放）
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isDownloading) "Downloading…" else if (progress >= 1f) "Completed" else "Ready",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "${displayPercent}%",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.scale(percentScale)
            )
        }

        // 控制按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                if (!isDownloading) {
                    if (progress >= 1f) progress = 0f
                    isDownloading = true
                    coroutine.launch {
                        while (progress < 1f && isDownloading) {
                            val step = when {
                                progress < 0.2f -> 0.06f
                                progress < 0.6f -> 0.035f
                                progress < 0.9f -> 0.02f
                                else -> 0.008f
                            }
                            delay(200L)
                            progress = (progress + step).coerceAtMost(1f)
                        }
                        isDownloading = false
                    }
                } else {
                    isDownloading = false
                }
            }) { Text(text = if (isDownloading) "Cancel" else "Start") }

            Button(onClick = { isDownloading = false; progress = 0f }) { Text(text = "Reset") }

            // 快速 +10% 按钮，便于调试
            Button(onClick = {
                progress = (progress + 0.1f).coerceAtMost(1f)
            }) { Text(text = "+10%") }
        }
    }
}


// ------------------ 各类 animate*AsState 示例 ------------------

@Composable
fun AnimateAsStateSamples(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // 每个小示例放入 Card 内，统一间距，便于视觉区分
        SmallExampleCard { FloatExample() }
        SmallExampleCard { ColorExample() }
        SmallExampleCard { DpExample() }
        SmallExampleCard { IntExample() }
        SmallExampleCard { OffsetExample() }
        SmallExampleCard { IntOffsetExample() }
        SmallExampleCard { SizeExamples() }
    }
}

@Composable
private fun SmallExampleCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) { content() }
    }
}

/**
 * animationSpec
 * 绝大部分的Compose动画函数都支持传入一个AnimationSpec参数。

 * 这个参数的作用很厉害，
 * 它可以定制这些动画函数的默认行为，将一些预设的动画效果调整成我们想要的样子。

 * 比如说，如果想要调整动画的持续时间、动画的加速模式等等。
 *
 * Compose给我们提供了多种预设的AnimationSpec类型，
 * 但我认为最常用的就只有tween和spring这两种。毕竟要受篇幅的影响，
 * 这里我们只讲解最常用的类型。
 *
//tween是补间的意思，当然现在的动画基本上都是补间动画了。在Compose当中，
//tween函数可以用于指定动画的时长、延迟、加速模式这几个属性。
//
//tween函数默认会使用FastOutSlowInEasing，
//也就是先快后慢的动画加速模式。我们一共有以下加速模式可供选择：
//tween(durationMillis = 2000, easing = LinearEasing)
//FastOutSlowInEasing
//LinearOutSlowInEasing
//FastOutLinearInEasing
//LinearEasing

spring在这里弹簧的意思，所以它是用于实现回弹动画效果的。
spring函数支持传入dampingRatio和stiffness这两个参数。
dampingRatio用于设置弹簧的弹性，有以下几种值可选：
DampingRatioNoBouncy
DampingRatioLowBouncy
DampingRatioMediumBouncy
DampingRatioHighBouncy
///////////////
.animateContentSize(
animationSpec = spring(
dampingRatio = Spring.DampingRatioMediumBouncy,
stiffness = Spring.StiffnessMediumLow
)
)

 */
@Composable
private fun FloatExample() {
    var toggled by remember { mutableStateOf(false) }
    // 使用 float 动画作为主要数值，同时加入大小/缩放动画以增强视觉反馈
    val animated by animateFloatAsState(
        targetValue = if (toggled) 1f else 0.2f,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
    )
    val boxSize by animateDpAsState(
        targetValue = if (toggled) 56.dp else 40.dp,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing)
    )
    val textScale by animateFloatAsState(
        targetValue = if (toggled) 1.06f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    // 平滑显示整数百分比（更容易感知变化）
    val displayInt by animateIntAsState(
        targetValue = (animated * 100).roundToInt(),
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
        )
        Button(onClick = { toggled = !toggled }) {
            Text(
                text = "Float ${displayInt}%",
                modifier = Modifier.scale(textScale)
            )
        }
    }
}

@Composable
private fun ColorExample() {
    var toggled by remember { mutableStateOf(false) }
    // 颜色动画 + 大小动画，颜色变化更明显
    val color by animateColorAsState(
        targetValue = if (toggled) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary,
        animationSpec = tween(
            durationMillis = 420,
            easing = FastOutSlowInEasing
        )
    )
    val boxSize by animateDpAsState(
        targetValue = if (toggled) 64.dp else 40.dp,
        animationSpec = tween(
            durationMillis = 420,
            easing = FastOutSlowInEasing
        )
    )
    val boxScale by animateFloatAsState(
        targetValue = if (toggled) 1.06f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .scale(boxScale)
                .background(color, shape = RoundedCornerShape(6.dp))
        )
        Button(onClick = { toggled = !toggled }) { Text(text = "Toggle Color") }
    }
}

@Composable
private fun DpExample() {
    var large by remember { mutableStateOf(false) }
    val sizeDp: Dp by animateDpAsState(
        targetValue = if (large) 64.dp else 28.dp,
        animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(sizeDp)
                .background(MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(6.dp))
        )
        Button(onClick = { large = !large }) { Text(text = "Toggle Size") }
    }
}

@Composable
private fun IntExample() {
    var target by remember { mutableIntStateOf(0) }
    val animated by animateIntAsState(
        targetValue = target,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Count: $animated")
        Button(onClick = { target += 10 }) { Text(text = "+10") }
    }
}

@Composable
private fun OffsetExample() {
    var toggled by remember { mutableStateOf(false) }
    val offset by animateOffsetAsState(
        targetValue = if (toggled) Offset(0f, 0f) else Offset(
            40f,
            0f
        ), animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(6.dp))
        )
        Button(onClick = { toggled = !toggled }) { Text(text = "Float Slide") }
    }
}

@Composable
private fun IntOffsetExample() {
    var toggled by remember { mutableStateOf(false) }
    val offset by animateIntOffsetAsState(
        targetValue = if (toggled) IntOffset(0, 0) else IntOffset(
            40,
            0
        ), animationSpec = tween(durationMillis = 450, easing = FastOutSlowInEasing)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .offset { offset }
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(6.dp)))
        Button(onClick = { toggled = !toggled }) { Text(text = "Int Slide") }
    }
}

@Composable
private fun SizeExamples() {
    var large by remember { mutableStateOf(false) }
    val sizeF by animateSizeAsState(
        targetValue = if (large) Size(120f, 40f) else Size(40f, 40f),
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )
    val sizeI by animateIntSizeAsState(
        targetValue = if (large) IntSize(120, 40) else IntSize(
            40,
            40
        ), animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size((sizeF.width / 4).dp, (sizeF.height / 4).dp)
                .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(6.dp))
        )
        Box(
            modifier = Modifier
                .size((sizeI.width).dp, (sizeI.height).dp)
                .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(6.dp))
        )
        Button(onClick = { large = !large }) { Text(text = "Toggle SizeF/SizeI") }
    }
}

/*
 注：下面是对 animateRectAsState 与 animateValueAsState 的说明示例（注释形式，便于理解）：

 // animateRectAsState 示例（如果你同时希望动画化一个矩形的位置信息与尺寸信息）
 // val rect by animateRectAsState(targetValue = if (expanded) Rect(0f,0f, 100f, 100f) else Rect(0f,0f, 40f,40f))
 // 然后根据 rect 的 left/top/width/height 来摆放或绘制元素。

 // animateValueAsState 泛型示例（需要 TwoWayConverter 来告诉动画系统如何在类型之间插值）：
 // val myConverter = TwoWayConverter({ myType -> /* toVector */ }, { v -> /* fromVector */ })
 // val animated by animateValueAsState(targetValue = myTarget, typeConverter = myConverter)

 以上两个 API 在需要同时动画化多个维度或者自定义类型时非常有用，但要实现类型转换器（TwoWayConverter）以便动画引擎能在数值空间插值。
*/
