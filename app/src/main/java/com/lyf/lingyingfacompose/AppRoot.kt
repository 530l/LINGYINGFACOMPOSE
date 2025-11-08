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
import com.lyf.lingyingfacompose.ui.wx.ui.WxMainScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation(startDestination: NavKey = MasterModeScreen) {
    val backStack = rememberNavBackStack(startDestination)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {

                is MasterModeScreen -> NavEntry(key) {
                    MasterModeScreen()
                }

                is WxMainScreen -> NavEntry(key) {
                    WxMainScreen()
                }

                is Splash -> NavEntry(key) {
                    SplashScreen(onFinished = {
                        val isLogin = false
                        if (isLogin) backStack.add(Main)
                        else backStack.add(Login)
                    })
                }

                is Login -> NavEntry(key) {
                    LoginScreen(onLoginSuccess = {
                        backStack.add(Main)
                    })
                }

                is Main -> NavEntry(key) {
                    MainScreen(onLogout = {
                        backStack.removeLastOrNull()
                        backStack.add(Login)
                    })
                }

                else -> error("Unknown key: $key")
            }
        }
    )
}
