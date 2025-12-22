package com.lyf.compose

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.hjq.toast.Toaster
import com.lyf.compose.feature.HomeScreen
import com.lyf.compose.feature.login.LoginScreen
import com.lyf.compose.feature.splash.WelcomeScreen
import com.lyf.compose.router.HomeScreenRouter
import com.lyf.compose.router.LoginRouter
import com.lyf.compose.router.SplashRouter
import kotlinx.coroutines.delay

// Compose 使用 Kotlin 编译器插件，首次运行需要：
// 编译 Compose 运行时代码
// 生成和缓存 Compose 类
// 预热 Compose 运行环境 ,延迟1-2s，回到主页，后debug模式就不会太卡了
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavHost(startDestination: NavKey = SplashRouter) {
    // 返回栈，初始页面是启动页
    val backStack = rememberNavBackStack(startDestination)

    val context = LocalContext.current

    // true 表示“等待二次返回以退出应用”的状态
    var backPressedState by remember { mutableStateOf(false) }

    // 当 backPressedState 为 true 时，启动 2 秒倒计时，超时后自动重置
    // 用于实现“再按一次退出应用”的交互
    LaunchedEffect(backPressedState) {
        if (backPressedState) {
            delay(2000)
            backPressedState = false
        }
    }


    fun handleBack() {
        val isHome = backStack.lastOrNull() == HomeScreenRouter
        when {
            // Home 第二次按返回：回到桌面
            isHome && backPressedState -> {
                //(context as? Activity)?.finish()
                val intent = Intent(Intent.ACTION_MAIN).apply {
                    addCategory(Intent.CATEGORY_HOME)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }

            // Home 第一次按返回：只提示不退出
            isHome -> {
                backPressedState = true
                Toaster.show("再按一次退出应用")
            }

            // 其它页面：正常返回
            else -> {
                backStack.removeLastOrNull()
            }
        }
    }

    //BackHandler 拦截 系统返回键（物理/手势）；
    BackHandler(enabled = true) {
        handleBack()
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            //NavDisplay.onBack 可能用于 内部自定义返回按钮（如 TopBar 的返回箭头）；
            handleBack()
        },
        entryProvider = { key ->
            when (key) {
                is SplashRouter -> NavEntry(key) {
                    WelcomeScreen(
                        navigateToNext = {
                            // 在 Splash 跳转时：清空栈并加入 Login
                            backStack.clear()
                            backStack.add(LoginRouter)
                        },
                    )
                }

                is LoginRouter -> NavEntry(key) {
                    LoginScreen(onLoginSuccess = { _ ->
                        // 登录成功后，清除整个栈，只保留 Home
                        backStack.clear()
                        backStack.add(HomeScreenRouter)
                    })
                }

                is HomeScreenRouter -> NavEntry(key) {
                    HomeScreen()
                }

                else -> error("Unknown navigation key: $key")
            }
        }
    )
}
