package com.lyf.compose.nav

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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.hjq.toast.Toaster
import com.lyf.compose.core.data.session.SessionManager
import com.lyf.compose.feature.HomeScreen
import com.lyf.compose.feature.animated.AnimatableScreen
import com.lyf.compose.feature.animated.AnimateAsStateScreen
import com.lyf.compose.feature.animated.AnimateContentSizeScreen
import com.lyf.compose.feature.animated.AnimatedContentScreen
import com.lyf.compose.feature.animated.AnimatedVisibilityScreen
import com.lyf.compose.feature.asset.DisposableEffectScreen
import com.lyf.compose.feature.asset.LaunchedEffectScreen
import com.lyf.compose.feature.asset.RememberUpdatedStateScreen
import com.lyf.compose.feature.asset.SideEffectScreen
import com.lyf.compose.feature.login.LoginScreen
import com.lyf.compose.feature.refresh.RefreshScreen
import com.lyf.compose.feature.splash.WelcomeScreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavHost(startDestination: NavKey = SplashRouter) {
    // 1. 定义顶级页面（Tab/Root）
    val topLevelKeys = remember { setOf(SplashRouter, LoginRouter, HomeScreenRouter) }

    // 2. 初始化状态和控制器
    val navState = rememberNavigationState(startKey = startDestination, topLevelKeys = topLevelKeys)
    val navigator = remember(navState) { MultiStackNavigator(navState) }

    // 3. Splash 状态管理
    var splashFinished by remember { mutableStateOf(false) }

    // 4. 处理全局逻辑（登录跳转、Splash）
    HandleAuthNavigation(navState, navigator, splashFinished)

    // 5. 处理返回键逻辑
    val handleBack = rememberBackPressHandler(navState, navigator)

    // 6. 渲染导航
    val entryProvider = entryProvider {
        entry<SplashRouter> {
            WelcomeScreen(
                navigateToNext = { splashFinished = true }
            )
        }
        entry<HomeScreenRouter> { HomeScreen() }
        entry<LoginRouter> { LoginScreen(onLoginSuccess = { }) }
        entry<SideEffectRouter> { SideEffectScreen() }
        entry<LaunchedEffectRouter> { LaunchedEffectScreen() }
        entry<DisposableEffectRouter> { DisposableEffectScreen() }
        entry<RememberUpdatedStateRouter> { RememberUpdatedStateScreen() }
        entry<RefreshRouter> { RefreshScreen() }
        entry<AnimatedVisibilityRouter> { AnimatedVisibilityScreen() }
        entry<AnimateContentSizeRouter> { AnimateContentSizeScreen() }
        entry<AnimatedContentRouter> { AnimatedContentScreen() }
        entry<AnimateAsStateRouter> { AnimateAsStateScreen() }
        entry<AnimatableRouter> { AnimatableScreen() }
    }

    val entries = navState.toEntries(entryProvider)

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavDisplay(
            entries = entries,
            onBack = handleBack
        )
    }
}

/**
 * 处理认证和 Splash 跳转逻辑
 */
@Composable
private fun HandleAuthNavigation(
    navState: NavigationState,
    navigator: MultiStackNavigator,
    splashFinished: Boolean
) {
    val token by SessionManager.tokenFlow.collectAsState()
    val isLoggedIn = token.isNotBlank()

    // 监听登录状态和当前页面的变化
    LaunchedEffect(isLoggedIn, splashFinished, navState.currentKey) {
        val current = navState.currentKey

        // 1. Splash 逻辑
        if (current == SplashRouter) {
            if (splashFinished) {
                // Splash 结束，根据登录态跳转
                navigator.navigateRoot(if (isLoggedIn) HomeScreenRouter else LoginRouter)
            }
            return@LaunchedEffect
        }

        // 2. 登出/Session过期逻辑：如果在需要登录的页面但未登录 -> 去登录页
        if (!isLoggedIn && current.requiresLogin()) {
            navigator.navigateRoot(LoginRouter)
            return@LaunchedEffect
        }

        // 3. 登录成功逻辑：如果在登录页但已登录 -> 去主页
        if (isLoggedIn && current == LoginRouter) {
            navigator.navigateRoot(HomeScreenRouter)
            return@LaunchedEffect
        }
    }
}


/**
 * 处理物理返回键逻辑
 * @return 返回处理函数，供 NavDisplay 使用
 */
@Composable
private fun rememberBackPressHandler(
    navState: NavigationState,
    navigator: MultiStackNavigator
): () -> Unit {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(false) }

    // 双击返回计时器
    LaunchedEffect(backPressedState) {
        if (backPressedState) {
            delay(2000)
            backPressedState = false
        }
    }

    val handleBack = remember(navState, navigator, backPressedState) {
        {
            val currentKey = navState.currentKey
            val isHome = currentKey == HomeScreenRouter
            val isLogin = currentKey == LoginRouter

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

                // Login 页面按返回：退出应用
                isLogin -> {
                    (context as? Activity)?.finish()
                }

                // 其它页面：正常返回
                else -> {
                    // 如果是顶级栈的最后一个元素，且当前就在该顶级页面，则退出应用
                    if (navState.topLevelStack.size <= 1 && currentKey == navState.currentTopLevelKey) {
                        (context as? Activity)?.finish()
                    } else {
                        navigator.pop()
                    }
                }
            }
            Unit
        }
    }

    // 注册系统返回键回调
    BackHandler(enabled = true, onBack = handleBack)

    return handleBack
}
