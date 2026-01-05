package com.lyf.compose.router

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

/**
 *  路由注册
 */
object RouterRegistrations {
    fun registerAll() {
        NavRegistry.register(HomeScreenRouter::class, requiresLogin = true) { _ ->
            HomeScreen()
        }
        NavRegistry.register(LoginRouter::class) { _ ->
            LoginScreen(onLoginSuccess = { })
        }
        NavRegistry.register(SideEffectRouter::class) { _ ->
            SideEffectScreen()
        }
        NavRegistry.register(LaunchedEffectRouter::class) { _ ->
            LaunchedEffectScreen()
        }

        NavRegistry.register(DisposableEffectRouter::class) { _ ->
            DisposableEffectScreen()
        }
        NavRegistry.register(RememberUpdatedStateRouter::class) { _ ->
            RememberUpdatedStateScreen()
        }
        NavRegistry.register(RefreshRouter::class) { _ ->
            RefreshScreen()
        }
        NavRegistry.register(AnimatedVisibilityRouter::class) { _ ->
            AnimatedVisibilityScreen()
        }
        NavRegistry.register(AnimateContentSizeRouter::class) { _ ->
            AnimateContentSizeScreen()
        }

        NavRegistry.register(AnimatedContentRouter::class) { _ ->
            AnimatedContentScreen()
        }

        NavRegistry.register(AnimateAsStateRouter::class) { _ ->
            AnimateAsStateScreen()
        }

        NavRegistry.register(AnimatableRouter::class) { _ ->
            AnimatableScreen()
        }
    }
}

