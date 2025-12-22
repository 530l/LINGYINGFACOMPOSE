package com.lyf.compose

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.lyf.compose.core.data.session.SessionManager
import com.lyf.compose.core.nav3.LocalNavigator
import com.lyf.compose.core.nav3.NavRegistry
import com.lyf.compose.core.nav3.createNavigator
import com.lyf.compose.core.nav3.navigateRootNonEmpty
import com.lyf.compose.feature.splash.WelcomeScreen
import com.lyf.compose.router.HomeScreenRouter
import com.lyf.compose.router.LoginRouter
import com.lyf.compose.router.RouterRegistrations
import com.lyf.compose.router.SplashRouter
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavHost(startDestination: NavKey = SplashRouter) {
    val backStack = rememberNavBackStack(startDestination)
    val context = LocalContext.current

    // NavDisplay要求返回栈非空。
    if (backStack.isEmpty()) {
        // 如果启动目的地就是 Login 或者 Splash（欢迎页），直接退出应用而不是临时添加，
        // 以避免出现空栈时 NavDisplay 抛异常的同时在逻辑上你希望关闭应用的情形。
        if (startDestination == LoginRouter || startDestination == SplashRouter) {
            (context as? Activity)?.finish()
            return
        } else {
            backStack.add(startDestination)
        }
    }

    // 注册应用内所有模块路由（集中式注册在 router 包）
    RouterRegistrations.registerAll()

    // 全局登录态：token 变化会驱动自动分流
    val token by SessionManager.tokenFlow.collectAsState()
    val isLoggedIn = token.isNotBlank()

    //是否已经播放完 Splash 动画。
    var splashFinished by remember { mutableStateOf(false) }

    // 登录态分流：Splash 未结束时锁定 Splash；结束后跳转到 Home/Login
    LaunchedEffect(isLoggedIn, splashFinished) {
        val current = backStack.lastOrNull()

        // 只在“仍停留在 Splash 且 Splash 未结束”时阻断跳转，保证首次启动 Splash 一定完整展示。
        // 一旦离开 Splash（比如已经进入 Home/Mine），后续 token 变化（退出登录/401）必须立即生效。
        if (current == SplashRouter && !splashFinished) return@LaunchedEffect

        // token 变化后强制回到对应根页面，避免出现“Login 闪一下又被覆盖回去”的情况。
        backStack.navigateRootNonEmpty(if (isLoggedIn) HomeScreenRouter else LoginRouter)
    }

    // =============== 返回键：主页双击返回回到桌面 ===============
    var backPressedState by remember { mutableStateOf(false) }
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
                context.startActivity(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }

            // Home 第一次按返回：提示
            isHome -> {
                backPressedState = true
                Toaster.show("再按一次退出应用")
            }

            // 其它页面：正常返回
            else -> backStack.removeLastOrNull()
        }
    }

    // 系统返回键（物理/手势）优先走这里
    BackHandler(enabled = true) { handleBack() }

    //创建一个由导航返回堆栈支持的导航器，并通过CompositionLocal提供它
    //其实就是抽取行为，通过回调的形式，入栈 和出栈
    val navigator = remember(backStack) {
        createNavigator(
            navigateAdd = { backStack.add(it) },
            navigateRootNonEmpty = { backStack.navigateRootNonEmpty(it) },
            popLast = { backStack.removeLastOrNull() }
        )
    }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            backStack = backStack,
            // NavDisplay.onBack：用于你自己在 UI 内部触发的“返回”
            // （比如 TopBar 返回按钮）
            onBack = { handleBack() },
            entryProvider = { key ->
                when (key) {
                    is SplashRouter -> NavEntry(key) {
                        WelcomeScreen(
                            navigateToNext = {
                                // Splash 动画结束：只标记完成。
                                // 具体跳转到 Home/Login 由 LaunchedEffect(isLoggedIn, splashFinished) 统一分流。
                                splashFinished = true
                            }
                        )
                    }

                    else -> NavRegistry.createEntry(key)
                }
            }
        )
    }
}
