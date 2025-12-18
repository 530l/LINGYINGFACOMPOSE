package com.lyf.lingyingfacompose


import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.lyf.lingyingfacompose.router.DesignScreenRouter
import com.lyf.lingyingfacompose.router.HomeScreenRouter
import com.lyf.lingyingfacompose.ui.HomeScreen
import com.lyf.lingyingfacompose.ui.design.DesignScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AppNavigation(startDestination: NavKey = HomeScreenRouter) {
    val backStack = rememberNavBackStack(startDestination)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is HomeScreenRouter -> NavEntry(key) {
                    HomeScreen()
                }
                is DesignScreenRouter -> NavEntry(key) {
                    DesignScreen()
                }
                else -> error("Unknown key: $key")
            }
        }
    )
}
