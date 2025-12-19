package com.lyf.lingyingfacompose

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lyf.lingyingfacompose.router.HomeScreenRouter
import com.lyf.lingyingfacompose.router.SplashRouter
import com.lyf.lingyingfacompose.ui.HomeScreen
import com.lyf.lingyingfacompose.ui.splash.WelcomeScreen

// 如果需要其他屏幕，可以在这里定义
sealed class AppDestination(val key: NavKey) {
    object Splash : AppDestination(SplashRouter)
    object Home : AppDestination(HomeScreenRouter)
    // 可以在这里添加更多目的地
    // object Profile : AppDestination(ProfileRouter)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation(
    startDestination: NavKey = SplashRouter
) {
    // 返回栈，初始页面是启动页
    val backStack = rememberNavBackStack(startDestination)

    NavDisplay(
        backStack = backStack,
        onBack = {
            // 返回逻辑：移除栈顶元素
            // 如果栈为空，可以在这里处理退出应用逻辑
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            } else {
                // 处理根页面的返回，比如退出应用或显示确认对话框
                // 这里可以添加退出应用的逻辑
            }
        },
        entryProvider = { key ->
            when (key) {
                // Compose 使用 Kotlin 编译器插件，首次运行需要：
                // 编译 Compose 运行时代码
                // 生成和缓存 Compose 类
                // 预热 Compose 运行环境 ,延迟1-2s，回到主页，后debug模式就不会太卡了
                is SplashRouter -> NavEntry(key) {
                    WelcomeScreen(
                        navigateToHome = {
                             backStack.add(HomeScreenRouter)
                        },
                    )
                }

                is HomeScreenRouter -> NavEntry(key) {
                    HomeScreen()
                }

                else -> error("Unknown navigation key: $key")
            }
        }
    )
}
