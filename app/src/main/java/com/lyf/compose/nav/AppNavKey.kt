package com.lyf.compose.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


/* ------------------------- */
/* NavKey 定义 */
/* ------------------------- */

@Serializable
data object SplashNavKey : NavKey

@Serializable
data object HomeScreenNavKey : NavKey

@Serializable
data object LoginNavKey : NavKey

@Serializable
data object SideEffectNavKey : NavKey

@Serializable
data object LaunchedEffectNavKey : NavKey

@Serializable
data object DisposableEffectNavKey : NavKey

@Serializable
data object RememberUpdatedStateNavKey : NavKey

@Serializable
data object ProduceStateNavKey : NavKey

@Serializable
data object RememberCoroutineScopeNavKey : NavKey

@Serializable
data object SnapshotFlowNavKey : NavKey


@Serializable
data object RefreshNavKey : NavKey


@Serializable
data object AnimatedVisibilityNavKey : NavKey


@Serializable
data object AnimateContentSizeNavKey : NavKey


@Serializable
data object AnimatedContentNavKey : NavKey

@Serializable
data object AnimateAsStateNavKey : NavKey


@Serializable
data object AnimatableRouter : NavKey

fun NavKey.requiresLogin(): Boolean {
    return this is HomeScreenNavKey
}
