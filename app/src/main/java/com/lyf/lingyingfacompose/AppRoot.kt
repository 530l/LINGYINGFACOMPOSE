package com.lyf.lingyingfacompose


import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lyf.lingyingfacompose.ui.login.LoginScreen
import com.lyf.lingyingfacompose.ui.main.MainScreen
import com.lyf.lingyingfacompose.ui.mastermode.MasterModeScreen
import com.lyf.lingyingfacompose.ui.splash.SplashScreen
import com.lyf.lingyingfacompose.ui.wx.home.WxHomeScreen
import com.lyf.lingyingfacompose.ui.wx.ui.WxMainScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation(startDestination: NavKey = WxHomeScreenRouter) {
    val backStack = rememberNavBackStack(startDestination)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is WxHomeScreenRouter -> NavEntry(key) {
                    WxHomeScreen()
                }

                is MasterModeScreenRouter -> NavEntry(key) {
                    MasterModeScreen()
                }

                is WxMainScreenRouter -> NavEntry(key) {
                    WxMainScreen()
                }

                is SplashRouter -> NavEntry(key) {
                    SplashScreen(onFinished = {
                        val isLogin = false
                        if (isLogin) backStack.add(MainRouter)
                        else backStack.add(LoginRouter)
                    })
                }

                is LoginRouter -> NavEntry(key) {
                    LoginScreen(onLoginSuccess = {
                        backStack.add(MainRouter)
                    })
                }

                is MainRouter -> NavEntry(key) {
                    MainScreen(onLogout = {
                        backStack.removeLastOrNull()
                        backStack.add(LoginRouter)
                    })
                }

                else -> error("Unknown key: $key")
            }
        }
    )
}
