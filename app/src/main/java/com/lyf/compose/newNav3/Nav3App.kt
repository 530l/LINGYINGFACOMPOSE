package com.lyf.compose.newNav3

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.*
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import com.lyf.compose.feature.splash.WelcomeScreen
import com.lyf.compose.feature.login.LoginScreen
import com.lyf.compose.feature.HomeScreen
import com.lyf.compose.feature.refresh.RefreshScreen
import com.lyf.compose.feature.asset.SideEffectScreen
import com.lyf.compose.feature.asset.LaunchedEffectScreen
import com.lyf.compose.feature.asset.DisposableEffectScreen
import com.lyf.compose.feature.asset.RememberUpdatedStateScreen
import com.lyf.compose.feature.animated.AnimatableScreen
import com.lyf.compose.feature.animated.AnimateAsStateScreen
import com.lyf.compose.feature.animated.AnimateContentSizeScreen
import com.lyf.compose.feature.animated.AnimatedContentScreen
import com.lyf.compose.feature.animated.AnimatedVisibilityScreen


/**
 * @param startRoute : 起始路由
 * @param topLevelRoutes : 顶级路由集合
 * @return NavigationState: 导航状态
 */
@Composable
fun rememberNavigationState(startRoute: NavKey, topLevelRoutes: Set<NavKey>): NavigationState {
    val topLevelRoute = rememberSerializable(
        startRoute,
        serializer = MutableStateSerializer(NavKeySerializer())
    ) {
        mutableStateOf(startRoute)
    }

    val backStacks = topLevelRoutes.associateWith { route ->
        rememberNavBackStack(route)
    }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRouteState = topLevelRoute,
            backStacks = backStacks
        )
    }
}

/* ------------------------- */
/* NavigationState 导航状态   */
/* ------------------------- */

class NavigationState(
    val startRoute: NavKey,
    topLevelRouteState: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>
) {
    var topLevelRoute: NavKey by topLevelRouteState

    val stacksInUse: List<NavKey>
        get() = if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
}

/* ------------------------- */
/* NavigationState → NavEntries */
/* ------------------------- */

@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): SnapshotStateList<NavEntry<NavKey>> {

    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider
        )
    }

    return stacksInUse
        .flatMap { decoratedEntries[it].orEmpty() }
        .toMutableStateList()
}

/* ------------------------- */
/* Navigator 导航员 */
/* ------------------------- */

class Navigator(private val state: NavigationState) {

    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: return

        val current = currentStack.last()
        if (current == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }

    fun onBack() {
        goBack()
    }
}


/* ------------------------- */
/* App 入口 */
/* ------------------------- */

@Composable
fun Nav3App() {

    val navigationState = rememberNavigationState(
        startRoute = SplashNavKey,
        topLevelRoutes = setOf(SplashNavKey, HomeScreenNavKey)
    )

    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {

        entry<SplashNavKey> {
            WelcomeScreen(
                navigateToNext = {
                    navigator.navigate(HomeScreenNavKey)
                }
            )
        }

        entry<HomeScreenNavKey> {
            HomeScreen(
                onNavigate = { key ->
                    navigator.navigate(key)
                },
                onLogout = {
                    navigator.navigate(LoginNavKey)
                }
            )
        }

        entry<LoginNavKey> {
            LoginScreen(
                onLoginSuccess = {
                    navigator.navigate(HomeScreenNavKey)
                }
            )
        }

        entry<RefreshNavKey> {
            RefreshScreen(onBack = { navigator.onBack() })
        }

        entry<SideEffectNavKey> {
            SideEffectScreen(onBack = { navigator.onBack() })
        }

        entry<LaunchedEffectNavKey> {
            LaunchedEffectScreen(onBack = { navigator.onBack() })
        }

        entry<DisposableEffectNavKey> {
            DisposableEffectScreen(onBack = { navigator.onBack() })
        }

        entry<RememberUpdatedStateNavKey> {
            RememberUpdatedStateScreen(onBack = { navigator.onBack() })
        }

        entry<AnimatableRouter> {
            AnimatableScreen(onBack = { navigator.onBack() })
        }

        entry<AnimateAsStateNavKey> {
            AnimateAsStateScreen(onBack = { navigator.onBack() })
        }

        entry<AnimateContentSizeNavKey> {
            AnimateContentSizeScreen(onBack = { navigator.onBack() })
        }

        entry<AnimatedContentNavKey> {
            AnimatedContentScreen(onBack = { navigator.onBack() })
        }

        entry<AnimatedVisibilityNavKey> {
            AnimatedVisibilityScreen(
                onBack = { navigator.onBack() }
            )
        }
    }


    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() }
    )
}
