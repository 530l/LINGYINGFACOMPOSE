package com.lyf.compose.router

import com.lyf.compose.core.nav3.NavRegistry
import com.lyf.compose.feature.HomeScreen
import com.lyf.compose.feature.asset.SideEffectScreen
import com.lyf.compose.feature.login.LoginScreen

/**
 *  路由注册
 */
object RouterRegistrations {
    fun registerAll() {
        NavRegistry.register(HomeScreenRouter::class) { _ ->
            HomeScreen()
        }
        NavRegistry.register(LoginRouter::class) { _ ->
            LoginScreen(onLoginSuccess = { })
        }
        NavRegistry.register(SideEffectRouter::class) { _ ->
            SideEffectScreen()
        }
    }
}

