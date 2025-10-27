package com.lyf.lingyingfacompose.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import androidx.compose.material3.adaptive.*
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy

// --- 导航键和场景键保持不变 ---
@Serializable data object MusicList : NavKey
@Serializable data class MusicDetail(val musicId: Int) : NavKey
@Serializable data object MusicListDetailSceneKey

/**
 * 主屏幕 - 演示了如何使用 `Levitate` 策略创建更现代的交互体验。
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(onLogout: () -> Unit) {


    // 获取自适应信息
//    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
//
//    // ✅ 安全地计算布局指令
//    val directive by remember(
//        windowAdaptiveInfo.widthSizeClass,
//        windowAdaptiveInfo.heightSizeClass
//    ) {
//        calculatePaneScaffoldDirective(windowAdaptiveInfo)
//    }


    // --- 关键：使用 `Levitate` 策略为详情页创建抽屉式体验 ---
//    val listDetailStrategy = rememberListDetailSceneStrategy<Any>(
//        directive = directive,
//        adaptStrategies = ThreePaneScaffoldAdaptStrategies(
//            // 列表窗格：在单窗格时隐藏，让位于详情
//            primaryPaneAdaptStrategy = AdaptStrategy.Hide,
//            // 详情窗格：在单窗格时作为悬浮的底部抽屉！
//            secondaryPaneAdaptStrategy = AdaptStrategy.Levitate(
//                alignment = Alignment.BottomCenter, // 悬浮在底部
//                scrim = { LevitatedPaneScrim() },   // 使用默认遮罩
//                // dragToResizeState = rememberDragToResizeState(...) // 可选：添加拖拽调整大小
//            ).onlyIfSinglePane(directive), // 仅在单窗格布局时悬浮！在大屏上正常展开。
//            // 第三窗格：不使用
//            tertiaryPaneAdaptStrategy = AdaptStrategy.Hide
//        )
//    )

    val backStack = rememberNavBackStack(MusicList)

//    NavDisplay(
//        backStack = backStack,
//        onBack = { backStack.removeLastOrNull() },
//        sceneStrategy = listDetailStrategy,
//        entryProvider = { key ->
//            when (key) {
//                is MusicList -> NavEntry(
//                    key = key,
//                    metadata = listDetailStrategy.listPane(sceneKey = MusicListDetailSceneKey),
//                    content = {
//                        MusicListScene(
//                            onMusicClick = { id ->
//                                backStack.add(
//                                    MusicDetail(id),
//                                    metadata = listDetailStrategy.detailPane(sceneKey = MusicListDetailSceneKey)
//                                )
//                            },
//                            onLogout = onLogout
//                        )
//                    }
//                )
//                else -> error("Unknown key: $key")
//            }
//        }
//    )
}

// --- 音乐列表场景 ---
@Composable
fun MusicListScene(onMusicClick: (Int) -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("🎵 音乐列表", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        (1..5).forEach { id ->
            Button(
                onClick = { onMusicClick(id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("播放 音乐 #$id")
            }
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("退出登录")
        }
    }
}

// --- 音乐详情场景 ---
@Composable
fun MusicDetailScene(id: Int) {
    // 注意：当使用 `Levitate` 策略时，这个 Composable 会渲染在悬浮的抽屉中
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 600.dp) // 限制最大高度，使其更像一个抽屉
            .padding(16.dp)
    ) {
        // 抽屉把手
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp)
                .height(4.dp)
                .background(MaterialTheme.colorScheme.outline)
                .padding(8.dp)
        )
        Spacer(Modifier.height(8.dp))

        Text("🎧 音乐详情：$id", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Text("这里可以展示封面、歌词、播放控制等内容。")
        Spacer(Modifier.height(8.dp))
        Button(onClick = { /* 播放逻辑 */ }) {
            Text("播放")
        }
    }
}